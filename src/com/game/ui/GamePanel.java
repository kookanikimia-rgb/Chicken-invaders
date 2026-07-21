package com.game.ui;

import com.game.audio.SoundManager;
import com.game.database.DatabaseManager;
import com.game.entities.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GamePanel extends JPanel implements KeyListener {

    private MainFrame frame;

    private Plane player;

    private Timer gameTimer;
    private long pausedDuration;
    private long pauseStartTime;

    private long playerDeathTime;

    private boolean isGameOver;
    private boolean isWin;

    private boolean rightPressed;
    private boolean leftPressed;
    private boolean upPressed;
    private boolean downPressed;
    private boolean spacePressed;
    private boolean isPaused;

    private ArrayList<Bullet> bullets ;
    private int bulletCount;
    private long lastShotTime;

    private ArrayList<PowerUp> powerUps;
    private long rapidFireEndTime;
    private long shieldEndTime;
    private long freezeEndTime;

    private EnemyGrid enemyGrid;
    private Boss boss;
    private long bossDeathTime;
    private ArrayList<Egg> eggs ;
    private ArrayList<EnemyBullet> enemyBullets;

    private ArrayList<Explosion> explosions;


    private int score;
    private int currentLevel;


    private Image backgroundImage;
    private Image shieldIcon;
    private Image freezeIcon;
    private Image rapidFireIcon;

    private boolean gameResultSaved;

    public GamePanel(MainFrame frame){

        this.frame = frame;

        isGameOver = false;
        isPaused = true;

        bullets = new ArrayList<>();

        powerUps = new ArrayList<>();
        rapidFireEndTime = 0;
        shieldEndTime = 0;
        freezeEndTime = 0;
        lastShotTime = 0;

        eggs = new ArrayList<>();
        enemyBullets = new ArrayList<>();
        enemyGrid = new EnemyGrid(1);
        bossDeathTime = 0;

        explosions = new ArrayList<>();

        pausedDuration = 0;
        pauseStartTime = 0;
        playerDeathTime = 0;


        String planeName = DatabaseManager.getSelectedPlane(UserSession.getUserName());
        PlaneInfo planeInfo = DatabaseManager.getPlaneInfo(planeName);
        player = new Plane(planeInfo);

        score = 0;
        currentLevel = 1;
        bulletCount = 1;

        setPreferredSize(new Dimension(600,800));
        setBackground(Color.BLACK);
        setFocusable(true);

        gameTimer = new Timer(16, e -> {
            update();
            repaint();
        });

        addKeyListener(this);


        shieldIcon = new ImageIcon(getClass().getResource("/Assets/images/powerup1/sheild.png")).getImage();
        rapidFireIcon = new ImageIcon(getClass().getResource("/Assets/images/powerup1/fast_shot.png")).getImage();
        freezeIcon = new ImageIcon(getClass().getResource("/Assets/images/powerup1/freeze.png")).getImage();

        try {
            backgroundImage = new ImageIcon(getClass().getResource("/Assets/images/main-menu-background.jpg")).getImage();
        } catch (Exception e) {
            System.out.println("MainMenu background image not found, using default color.");
        }

    }

    private void update() {

        if (isPaused) {
            repaint();
            return;
        }

        if (updateGameOver()) {
            return;
        }

        if (updatePlayerDeath()) {
            return;
        }

        updatePlayerMovement();

        handlePlayerShooting();

        updatePlayerBullets();

        updateEnemiesOrBoss();

        updatePowerUps();

        updateEggs();

        updateEnemyBullets();

        checkLevelCompletion();

        updateBossDeath();

    }
    private void updatePlayerMovement(){
        if (leftPressed)
            player.moveLeft();

        if (rightPressed)
            player.moveRight();

        if (upPressed)
            player.moveUp();

        if (downPressed)
            player.moveDown();

        player.keepInBounds(getWidth(), getHeight());
    }

    private void handlePlayerShooting(){
        if (spacePressed) {

            long currentTime = gameTime();

            int delay;

            if (currentTime < rapidFireEndTime)
                delay = 80;
            else
                delay = player.planeInfo.shootDelay;

            if (currentTime - lastShotTime >= delay) {

                for (int i = 0; i < bulletCount; i++) {

                    int offset = (i - bulletCount / 2) * 10;

                    bullets.add(new Bullet(
                            player.x + player.width / 2 + offset,
                            player.y
                    ));
                }

                SoundManager.playShot();

                lastShotTime = currentTime;
            }
        }
    }

    private void updatePlayerBullets(){

        for (int i = 0; i < bullets.size(); i++) {
            Bullet b = bullets.get(i);
            b.move();

            if (b.y < 0) {
                bullets.remove(i);
                i--;
                continue;
            }
            if (enemyGrid != null && (currentLevel != 4 && currentLevel != 8)) {
                for (Enemy e : enemyGrid.gridEnemies)
                    if (b.getBounds().intersects(e.getBounds())) {
                        e.takeDamage();
                        bullets.remove(i);
                        i--;
                        break;
                    }
            } else if (boss != null) {
                // اگر غول روی صفحه است، چک کن تیر به او خورده یا نه
                if (b.getBounds().intersects(boss.getBounds())) {

                    int damage = 1;
                    if (player.planeInfo.doubleBossDamage) {
                        damage = 2;
                    }

                    boss.takeDamage(damage);
                    bullets.remove(i--);
                }
            }
        }
    }

    private void updateEnemiesOrBoss() {

        updatePlayerEnemyCollision();

        removeDeadEnemies();

        if (currentLevel == 4 || currentLevel == 8) {
            updateBoss();
        } else {
            updateEnemyGrid();
        }
    }

    private void updatePlayerEnemyCollision() {

        if (enemyGrid == null)
            return;

        for (Enemy enemy : enemyGrid.gridEnemies) {

            if (player.getBounds().intersects(enemy.getBounds())) {

                damagePlayer();
                enemy.hp = 0;
            }
        }
    }

    private void removeDeadEnemies() {

        if (enemyGrid == null)
            return;

        for (int i = 0; i < enemyGrid.gridEnemies.size(); i++) {

            Enemy enemy = enemyGrid.gridEnemies.get(i);

            if (!enemy.isDead())
                continue;

            handleEnemyDeath(enemy);

            enemyGrid.gridEnemies.remove(i--);
        }
    }

    private void handleEnemyDeath(Enemy enemy) {

        SoundManager.playExplosion();

        int centerX = (int) enemy.x + (enemy.width / 2); // پیدا کردن مرکز افقی مرغ
        int centerY = (int) enemy.y + (enemy.height / 2); // پیدا کردن مرکز عمودی مرغ
        explosions.add(new Explosion(centerX, centerY, 60,gameTime(),Explosion.ENEMY));

        addScore(enemy.pointValue);

        spawnPowerUp(enemy);

        if (!enemy.isReplacement) {

            enemyGrid.cellHits[enemy.row][enemy.col]--;

            if (enemyGrid.cellHits[enemy.row][enemy.col] > 0) {
                enemyGrid.spawnReplacementEnemy(enemy.row, enemy.col,getWidth());
            }
        }
    }

    private void spawnPowerUp(Enemy enemy) {

        Random random = new Random();

        if (random.nextInt(100) >= 20)
            return;

        int type = random.nextInt(5);

        powerUps.add(new PowerUp(enemy.x, enemy.y, type));
    }

    private void updateBoss() {

        if (boss == null) {

            enemyGrid = null;

            if(currentLevel == 4){
                boss = new BossLevel4();
            }

            if(currentLevel == 8){
                boss = new BossLevel8();
            }
        }

        if (gameTime() <= freezeEndTime || boss.isDead())
            return;

        boss.update(getWidth());

        eggs.addAll(boss.attack(gameTime()));
    }

    private void updateEnemyGrid() {

        if (gameTime() > freezeEndTime)
            enemyGrid.update(getWidth());

        if (enemyGrid.isBottomReached()) {

            gameOver();
            return;
        }

        spawnEnemyEggs();

        updateShooterEnemies();
    }

    private void spawnEnemyEggs() {

        for (Enemy enemy : enemyGrid.gridEnemies) {

            if (!enemyGrid.canDropEgg(enemy)) {
                continue;
            }

            Egg egg = enemy.dropEgg(gameTime());

            if (egg != null) {
                eggs.add(egg);
            }
        }
    }

    private void updateShooterEnemies() {

        if (gameTime() <= freezeEndTime) {
            return;
        }

        for (Enemy enemy : enemyGrid.gridEnemies) {

            if (!(enemy instanceof ShooterEnemy)) {
                continue;
            }

            EnemyBullet bullet =
                    ((ShooterEnemy) enemy).shoot(player.x, gameTime());

            if (bullet != null) {
                enemyBullets.add(bullet);
            }
        }
    }

    private void updatePowerUps(){

        for (int i = 0; i < powerUps.size(); i++) {

            PowerUp p = powerUps.get(i);

            p.update();

            if (p.y > getHeight()) {

                powerUps.remove(i--);
                continue;
            }

            if (p.getBounds().intersects(player.getBounds())) {

                switch (p.type) {

                    case PowerUp.ADD_FIRE:
                        if (bulletCount < 5)
                            bulletCount++;
                        break;

                    case PowerUp.RAPID_FIRE:
                        rapidFireEndTime = gameTime() + 8000;
                        break;

                    case PowerUp.EXTRA_LIFE:
                        if (player.hp < 5)
                            player.hp++;
                        break;

                    case PowerUp.SHIELD:
                        shieldEndTime = gameTime() + 10000;
                        break;

                    case PowerUp.FREEZE_BOMB:
                        freezeEndTime = gameTime() + 3000;
                        break;
                }

                powerUps.remove(i--);
            }
        }
    }

    private void updateEggs() {
        for (int i = 0; i < eggs.size(); i++) {
            Egg egg = eggs.get(i);
            if (gameTime() > freezeEndTime) {
                egg.move();
            }
            if (isOutsideScreen(egg)) {
                eggs.remove(i--);
                continue;
            }
            if (egg.getBounds().intersects(player.getBounds())) {
                damagePlayer();
                eggs.remove(i--);
            }
        }
    }
    private void updateEnemyBullets() {

        for (int i = 0; i < enemyBullets.size(); i++) {

            EnemyBullet bullet = enemyBullets.get(i);

            if (gameTime() > freezeEndTime) {
                bullet.move();
            }

            if (bullet.getBounds().intersects(player.getBounds())) {

                damagePlayer();

                enemyBullets.remove(i--);
                continue;
            }

            if (bullet.x < 0 || bullet.x > getWidth())
                enemyBullets.remove(i--);
        }
    }
    private void damagePlayer() {

        if (gameTime() <= shieldEndTime || playerDeathTime != 0)
            return;

        player.takeDamage();

            SoundManager.playExplosion();

            int centerX = player.x + player.width / 2;
            int centerY = player.y + player.height / 2;

            explosions.add(new Explosion(
                    centerX,
                    centerY,
                    80,
                    gameTime(),
                    Explosion.PLAYER));
        if (player.isDead()) {
            playerDeathTime = gameTime();
        }
    }
    private boolean updatePlayerDeath() {

        if (playerDeathTime == 0) {
            return false;
        }

        if (gameTime() - playerDeathTime >= 600) {
            gameOver();
            playerDeathTime = 0;
        } else {
            repaint();
        }

        return true;
    }

    private boolean isOutsideScreen(Egg egg) {

        return egg.x + egg.width < 0
                || egg.x > getWidth()
                || egg.y + egg.height < 0
                || egg.y > getHeight();
    }

    private void updateBossDeath() {
        if (boss == null || !boss.isDead()) {
            return;
        }
        if (bossDeathTime == 0) {
            bossDeathTime = gameTime();
            SoundManager.playExplosion();
            int centerX = (int) (boss.x + boss.width / 2f);
            int centerY = (int) (boss.y + boss.height / 2f);
            explosions.add(new Explosion(
                    centerX, centerY, 100, gameTime(), Explosion.ENEMY));
            return;
        }
        if (gameTime() - bossDeathTime < 800) {
            return;
        }
        addScore(currentLevel == 4 ? 500 : 1000);
        if (currentLevel == 8) {
            finishWin();
            return;
        }
        currentLevel++;
        clearLevelObjects();
        boss = null;
        bossDeathTime = 0;
        enemyGrid = new EnemyGrid(currentLevel);
    }

    private void checkLevelCompletion(){
        if (enemyGrid != null && enemyGrid.gridEnemies.isEmpty()) {
            if (currentLevel < 8) {
                clearLevelObjects();
                currentLevel++;
                enemyGrid = new EnemyGrid(currentLevel);
                addScore(200);
            }
        }
    }

    public void addScore(int points) {
        this.score += points;
    }

    private void finishWin() {

        isWin = true;

        SoundManager.playWin();

        savePlayerProgress();

        clearLevelObjects();

        gameTimer.stop();
    }
    private boolean updateGameOver() {

        if (!isGameOver) {
            return false;
        }

        boolean finished = true;

        for (Explosion e : explosions) {
            if (!e.isFinished(gameTime())) {
                finished = false;
                break;
            }
        }

        if (finished) {
            gameTimer.stop();
        }

        return true;
    }

    private void savePlayerProgress() {

        if (gameResultSaved || UserSession.getUserName() == null) {
            return;
        }

        DatabaseManager.saveGame(
                UserSession.getUserName(),
                score,
                currentLevel,
                DatabaseManager.getSoundSetting(UserSession.getUserName(), "bg_music"),
                DatabaseManager.getSoundSetting(UserSession.getUserName(), "shot_sound"),
                DatabaseManager.getSoundSetting(UserSession.getUserName(), "crash_sound"),
                DatabaseManager.getSoundSetting(UserSession.getUserName(), "game_over_sound")
        );

        DatabaseManager.updateUserStats(
                UserSession.getUserName(),
                score,
                currentLevel
        );

        gameResultSaved = true;
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        drawBackground(g);
        drawHud(g2d);

        drawPlayer(g2d);
        drawEnemies(g);
        drawProjectiles(g);
        drawExplosions(g);

        drawPauseScreen(g2d);
        if (isGameOver) {
           drawGameOverScreen(g2d);
        }
        if (isWin && !isGameOver) {
          drawWinScreen(g2d);
        }
    }

    private void drawBackground(Graphics g){
        if (backgroundImage != null) {
            // عکس را به اندازه کل پنل می‌کشد
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private void drawHud(Graphics2D g2d){

        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRoundRect(8, 8, 170, 180, 20, 20);

        g2d.setColor(new Color(80, 180, 255));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(8, 8, 170, 180, 20, 20);

        g2d.setFont(new Font("Arial", Font.BOLD, 15));

        int x = 20;
        int y = 30;

        String currentUserName = UserSession.getUserName();
        g2d.setColor(Color.WHITE);
        g2d.drawString("Player: " + currentUserName, x, y);

        y += 25;
        g2d.setColor(new Color(100, 100, 200));
        g2d.drawString("Score: " + score, x, y);

        y += 25;
        g2d.setColor(new Color(190, 40, 130));
        g2d.drawString("Level: " + currentLevel, x, y);

        y += 25;
        g2d.setColor(Color.GREEN);
        g2d.drawString("Lives: " + player.hp, x, y);

        y += 25;
        g2d.setColor(new Color(189, 46, 80));
        g2d.drawString("Bullets: " + bulletCount, x, y);


        if (gameTime() < rapidFireEndTime)
            g2d.drawImage(rapidFireIcon, 20, 145, 32, 32, null);

        if (gameTime() < shieldEndTime)
            g2d.drawImage(shieldIcon, 52, 145, 32, 32, null);

        if (gameTime() < freezeEndTime)
            g2d.drawImage(freezeIcon, 84, 145, 32, 32, null);
    }

    private void drawPlayer(Graphics2D g2d){
        if (playerDeathTime == 0) {

            if (player.image != null) {
                g2d.drawImage(player.image, player.x, player.y, player.width, player.height, null);
            } else {
                g2d.setColor(Color.CYAN);
                g2d.fillRect(player.x, player.y, player.width, player.height);
            }
        }

        if(playerDeathTime == 0 && gameTime() < shieldEndTime){

            g2d.setColor(new Color(250,20,100));

            g2d.drawOval(
                    player.x - 8,
                    player.y - 8,
                    player.width + 16,
                    player.height + 16
            );
        }
    }

    private void drawEnemies(Graphics g){

        if (currentLevel == 4 || currentLevel == 8) {

            if (boss != null && bossDeathTime == 0)
                boss.draw(g);

        } else {

            enemyGrid.draw(g);
        }

    }

    private void drawExplosions(Graphics g){

        for (int i = explosions.size() - 1; i >= 0; i--) {
            Explosion exp = explosions.get(i);
            if (exp.isFinished(gameTime())) {
                explosions.remove(i); // اگر زمانش تمام شد، از لیست پاک کن
            } else {
                exp.draw(g,gameTime()); // در غیر این صورت رسمش کن
            }
        }

    }

    private void drawProjectiles(Graphics g){

        for(Bullet b : bullets)
            b.draw(g);

        for(Egg egg : eggs)
            egg.draw(g);

        for(EnemyBullet b : enemyBullets)
            b.draw(g);

        for(PowerUp p : powerUps)
            p.draw(g);
    }

    private void drawPauseScreen(Graphics2D g2d) {

        if (isPaused) {

            g2d.setColor(new Color(0, 0, 0, 170));
            g2d.fillRect(0, 0, getWidth(), getHeight());

            g2d.setColor(Color.blue);
            g2d.setFont(new Font("Arial", Font.BOLD, 60));

            String msg = "PAUSED";

            FontMetrics fm = g2d.getFontMetrics();

            g2d.drawString(
                    msg,
                    (getWidth() - fm.stringWidth(msg)) / 2,
                    getHeight() / 2
            );

            g2d.setFont(new Font("Arial", Font.PLAIN, 22));

            String sub = "Press P to Continue";

            g2d.drawString(
                    sub,
                    (getWidth() - g2d.getFontMetrics().stringWidth(sub)) / 2,
                    getHeight() / 2 + 45
            );
        }

    }

    private void drawResultScreen(Graphics2D g2d, String title, Color borderColor, Color titleColor) {

        int boxWidth = 450;
        int boxHeight = 220;

        int boxX = (getWidth() - boxWidth) / 2;
        int boxY = (getHeight() - boxHeight) / 2 - 30;

        // پس‌زمینه تیره کل صفحه
        g2d.setColor(new Color(0, 0, 0, 170));
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // باکس
        g2d.setColor(new Color(20, 20, 40, 220));
        g2d.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 25, 25);

        // حاشیه
        g2d.setColor(borderColor);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(boxX, boxY, boxWidth, boxHeight, 25, 25);

        // عنوان
        g2d.setColor(titleColor);
        g2d.setFont(new Font("Arial", Font.BOLD, 60));

        FontMetrics fm = g2d.getFontMetrics();

        g2d.drawString(
                title,
                (getWidth() - fm.stringWidth(title)) / 2,
                getHeight() / 2
        );

        // امتیاز
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 28));

        String scoreMsg = "Score: " + score;

        FontMetrics scoreFm = g2d.getFontMetrics();

        g2d.drawString(
                scoreMsg,
                (getWidth() - scoreFm.stringWidth(scoreMsg)) / 2,
                getHeight() / 2 + 40
        );

        // متن پایین
        g2d.setColor(new Color(250, 10, 250));
        g2d.setFont(new Font("Arial", Font.PLAIN, 20));

        String subMsg = "Press ESC to return to Menu";

        g2d.drawString(
                subMsg,
                (getWidth() - g2d.getFontMetrics().stringWidth(subMsg)) / 2,
                getHeight() / 2 + 110
        );
    }

    private void drawGameOverScreen(Graphics2D g2d) {

        drawResultScreen(
                g2d,
                "GAME OVER!",
                new Color(255, 80, 80),
                new Color(189, 46, 80)
        );
    }

    private void drawWinScreen(Graphics2D g2d) {

        drawResultScreen(
                g2d,
                "YOU WIN!",
                new Color(100, 180, 80),
                new Color(10, 200, 98)
        );
    }

    @Override
    public void keyPressed(KeyEvent e){
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_ESCAPE) {
            savePlayerProgress();
            gameTimer.stop();
            resetGame();
            frame.showPage("MENU");

            return;
        }

        if(key==KeyEvent.VK_LEFT || key == KeyEvent.VK_A)
            leftPressed = true;

        if(key==KeyEvent.VK_RIGHT || key == KeyEvent.VK_D)
            rightPressed = true;

        if (key==KeyEvent.VK_UP || key == KeyEvent.VK_W)
            upPressed = true;

        if (key==KeyEvent.VK_DOWN || key == KeyEvent.VK_S)
            downPressed = true;

        if (key==KeyEvent.VK_SPACE)
            spacePressed = true;

        if (key == KeyEvent.VK_P)
            if (!isPaused) {

                isPaused = true;
                pauseStartTime = System.currentTimeMillis();

            } else {

                isPaused = false;
                pausedDuration += System.currentTimeMillis() - pauseStartTime;

            }


    }

    @Override
    public void keyReleased(KeyEvent e){
        int key = e.getKeyCode();

        if(key==KeyEvent.VK_LEFT || key == KeyEvent.VK_A)
            leftPressed = false;

        if(key==KeyEvent.VK_RIGHT || key == KeyEvent.VK_D)
            rightPressed = false;

        if (key==KeyEvent.VK_UP || key == KeyEvent.VK_W)
            upPressed = false;

        if (key==KeyEvent.VK_DOWN || key == KeyEvent.VK_S)
            downPressed = false;

        if (key==KeyEvent.VK_SPACE)
            spacePressed = false;
    }

    @Override
    public void keyTyped(KeyEvent e){

    }

    public void startGame() {
        resetGame();
        isPaused = false;

        requestFocusInWindow();


        if (!gameTimer.isRunning()) {
            gameTimer.start();
        }
    }


    private void resetGame() {
        // بارگذاری اطلاعات هواپیما
        String planeName = DatabaseManager.getSelectedPlane(UserSession.getUserName());
        PlaneInfo planeInfo = DatabaseManager.getPlaneInfo(planeName);
        player = new Plane(planeInfo);

        // ریست متغیرهای اصلی بازی
        currentLevel = 1;
        score = 0;
        bulletCount = 1;

        // ریست تایمرها
        lastShotTime = 0;
        rapidFireEndTime = 0;
        shieldEndTime = 0;
        freezeEndTime = 0;
        bossDeathTime = 0;
        playerDeathTime = 0;

        // ریست وضعیت بازی
        isGameOver = false;
        isWin = false;
        isPaused = false;

        // ریست تایمر پاز
        pausedDuration = 0;
        pauseStartTime = 0;

        // پاک کردن آبجکت‌های داخل بازی
        clearLevelObjects();

        // ساخت دوباره دشمن‌ها
        boss = null;
        enemyGrid = new EnemyGrid(currentLevel);

        gameResultSaved = false;
    }

    private void clearLevelObjects() {
        bullets.clear();
        eggs.clear();
        enemyBullets.clear();
        explosions.clear();
        powerUps.clear();
    }

    private void gameOver() {

        if (isGameOver)
            return;

        if (playerDeathTime == 0) {

            playerDeathTime = gameTime();

            SoundManager.playExplosion();

            int centerX = player.x + player.width / 2;
            int centerY = player.y + player.height / 2;

            explosions.add(new Explosion(centerX, centerY, 80, gameTime(),Explosion.PLAYER));

            return;
        }

        explosions.add(new Explosion(
                player.x + player.width / 2,
                player.y + player.height / 2,
                80,
                gameTime()
                ,Explosion.ENEMY));

        SoundManager.playGameOver();

        isGameOver = true ;
        gameTimer.stop();

        savePlayerProgress();

        playerDeathTime = 0;
    }

    private long gameTime() {
        long now = isPaused ? pauseStartTime : System.currentTimeMillis();
        return now - pausedDuration;
    }

}
