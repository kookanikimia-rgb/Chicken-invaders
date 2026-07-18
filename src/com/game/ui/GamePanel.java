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

    private boolean isGameOver;
    private boolean isWin;

    private boolean rightPressed;
    private boolean leftPressed;
    private boolean upPressed;
    private boolean downPressed;
    private boolean spacePressed;
    private boolean isPaused;

    private ArrayList<Bullet> bullets ;
    private long lastShotTime;
    private ArrayList<PowerUp> powerUps;

    private long rapidFireEndTime = 0;
    private long shieldEndTime = 0;
    private long freezeEndTime = 0;

    private EnemyGrid enemyGrid;
    private Boss boss;
    private long bossDeathTime;
    private ArrayList<Egg> eggs ;
    private ArrayList<EnemyBullet> enemyBullets;
    private ArrayList<Explosion> explosions;


    private int score;
    private int currentLevel;
    private int bulletCount;

    private Image backgroundImage;
    private Image shieldIcon;
    private Image freezeIcon;
    private Image rapidFireIcon;


    public GamePanel(MainFrame frame){

        this.frame = frame;
        this.isGameOver = false;
        this.isPaused = true;

        this.bullets = new ArrayList<>();
        this.powerUps = new ArrayList<>();
        this.rapidFireEndTime = 0;
        this.shieldEndTime = 0;
        this.freezeEndTime = 0;
        this.lastShotTime = 0;

        this.eggs = new ArrayList<>();
        this.enemyBullets = new ArrayList<>();
        this.explosions = new ArrayList<>();
        this.enemyGrid = new EnemyGrid(1);
        this.bossDeathTime = 0;


        String planeName = DatabaseManager.getSelectedPlane(UserSession.getUserName());
        PlaneInfo planeInfo = DatabaseManager.getPlaneInfo(planeName);
        this.player = new Plane(planeInfo);

        this.score = 0;
        this.currentLevel = 1;
        this.bulletCount = 1;

        setPreferredSize(new Dimension(600,800));
        setBackground(Color.BLACK);
        setFocusable(true);

        gameTimer = new Timer(16, e -> {
            update();
            repaint();
        });

        this.addKeyListener(this);

        SoundManager.startBackgroundMusic();

        this.shieldIcon = new ImageIcon(getClass().getResource("/Assets/images/powerup1/sheild.png")).getImage();
        this.rapidFireIcon = new ImageIcon(getClass().getResource("/Assets/images/powerup1/fast_shot.png")).getImage();
        this.freezeIcon = new ImageIcon(getClass().getResource("/Assets/images/powerup1/freeze.png")).getImage();

        try {
            backgroundImage = new ImageIcon(getClass().getResource("/Assets/images/main-menu-background.jpg")).getImage();
        } catch (Exception e) {
            System.out.println("MainMenu background image not found, using default color.");
        }

    }

    private void update() {

        if (isPaused)
            return;

        //حرکت پلیر
        if (isGameOver) return;

        if (leftPressed)
            player.moveLeft();

        if (rightPressed)
            player.moveRight();

        if (upPressed)
            player.moveUp();

        if (downPressed)
            player.moveDown();


        player.keepInBounds(getWidth(), getHeight());
        //شلیک
        if (spacePressed) {

            long currentTime = System.currentTimeMillis();

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
        //  آپدیت تیر ها و برخورد با دشمنان
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
                    if(player.planeInfo.doubleBossDamage){
                        damage = 2;
                    }

                    boss.takeDamage(damage);
                    bullets.remove(i--);
                }
            }
        }

        //برخورد پلیر با دشمن
        for (Enemy e : enemyGrid.gridEnemies)
            if (player.getBounds().intersects(e.getBounds())) {
                if (System.currentTimeMillis() > shieldEndTime)
                    player.takeDamage();
                e.hp = 0;
                if (player.hp <= 0)
                    gameOver();
            }

        for (int i = 0; i < enemyGrid.gridEnemies.size(); i++) {
            Enemy e = enemyGrid.gridEnemies.get(i);
            if (e.isDead()) {
                SoundManager.playExplosion();
                int centerX = (int)e.x + (e.width / 2); // پیدا کردن مرکز افقی مرغ
                int centerY = (int)e.y + (e.height / 2); // پیدا کردن مرکز عمودی مرغ
                explosions.add(new Explosion(centerX, centerY, 60));
                addScore(e.pointValue);

                Random rand = new Random();

                if (rand.nextInt(100) < 20) {
                    int type = rand.nextInt(5);

                    powerUps.add(new PowerUp(e.x, e.y, type));
                }

                if (!e.isReplacement) {

                    // ۱. کم کردن از شمارنده خانه
                    enemyGrid.cellHits[e.row][e.col]--;

                    // ۲. اگر هنوز حق جایگزینی هست، مرغ جدید بساز
                    if (enemyGrid.cellHits[e.row][e.col] > 0) {
                        enemyGrid.spawnReplacementEnemy(e.row, e.col);
                    }
                }

                // ۳. حذف مرغ مرده از لیست
                enemyGrid.gridEnemies.remove(i);
                i--;
            }
        }
        if (currentLevel == 4 || currentLevel == 8) {
            // اگر لول ۴ یا ۸ است، غول را مدیریت کن
            if (boss == null) boss = new Boss(currentLevel);

            if (System.currentTimeMillis() > freezeEndTime){
            boss.update(getWidth());

            // تخم‌های غول را به لیست تخم‌های بازی اضافه کن
            List<Egg> bossEggs = boss.attack();
            eggs.addAll(bossEggs);}

            if (boss.isDead()) {
                addScore((currentLevel == 4) ? 500 : 1000);
                if (currentLevel == 8) {
                    isWin = true;
                } else {
                    currentLevel++; // رفتن به لول ۵
                    boss = null;
                    enemyGrid = new EnemyGrid(currentLevel); // ساخت شبکه لول ۵
                }
            }
        } else {

            if (System.currentTimeMillis() > freezeEndTime)
                enemyGrid.update(getWidth());

            if (enemyGrid.isBottomReached()) {
                gameOver();
            }

            for (Enemy e : enemyGrid.gridEnemies) {
                if (e.row == 4) {
                    Egg newEgg = e.dropEgg();
                    if (newEgg != null) {
                        eggs.add(newEgg);
                    }
                }
                if (e instanceof ShooterEnemy) {

                    if (System.currentTimeMillis() > freezeEndTime) {
                        EnemyBullet b = ((ShooterEnemy) e).shoot(player.x);

                        if (b != null)
                            enemyBullets.add(b);
                    }
                }
            }
        }
        //آپدیت پاور آپ ها
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
                        rapidFireEndTime = System.currentTimeMillis() + 8000;
                        break;

                    case PowerUp.EXTRA_LIFE:
                        if (player.hp < 5)
                            player.hp++;
                        break;

                    case PowerUp.SHIELD:
                        shieldEndTime = System.currentTimeMillis() + 10000;
                        break;

                    case PowerUp.FREEZE_BOMB:
                        freezeEndTime = System.currentTimeMillis() + 3000;
                        break;
                }

                powerUps.remove(i--);
            }
        }


        // آپدیت حرکت تخم‌ها و برخورد با پلیر
        for (int i = 0; i < eggs.size(); i++) {
            Egg egg = eggs.get(i);
            if (System.currentTimeMillis() > freezeEndTime)
                egg.move();

            if (egg.y > getHeight()) {
                eggs.remove(i--);
                continue;
            }

            if (egg.getBounds().intersects(player.getBounds())) {

                if (System.currentTimeMillis() > shieldEndTime)
                    player.takeDamage();

                eggs.remove(i--);
                if (player.hp <= 0)
                    gameOver();
            }
        }

        for (int i = 0; i < enemyBullets.size(); i++) {

            EnemyBullet b = enemyBullets.get(i);

            if (System.currentTimeMillis() > freezeEndTime)
                b.move();

            if (b.getBounds().intersects(player.getBounds())) {

                if (System.currentTimeMillis() > shieldEndTime)
                    player.takeDamage();

                enemyBullets.remove(i--);

                if (player.hp <= 0)
                    gameOver();

                continue;
            }

            if (b.x < 0 || b.x > getWidth())
                enemyBullets.remove(i--);
        }

        if (enemyGrid.gridEnemies.isEmpty()) {
            if (currentLevel < 8) {
                currentLevel++;
                enemyGrid = new EnemyGrid(currentLevel);
                addScore(200);
            }
        }
        if (boss != null && boss.isDead()) {

            bossDeathTime = System.currentTimeMillis();
            SoundManager.playExplosion();

            // افکت
            int centerX = (int)(boss.x + boss.width / 2);
            int centerY = (int)(boss.y + boss.height / 2);
            explosions.add(new Explosion(centerX, centerY, 100));

            if (currentLevel == 8) {
                isWin = true;
                SoundManager.playWin();

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

            eggs.clear();
            enemyBullets.clear();
            gameTimer.stop();
            return;
        }
            if (bossDeathTime != 0 && System.currentTimeMillis() - bossDeathTime > 500) {
                currentLevel++;
                boss = null;
                bossDeathTime = 0;
                enemyGrid = new EnemyGrid(currentLevel);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if (backgroundImage != null) {
            // عکس را به اندازه کل پنل می‌کشد
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

        g2d.setColor(new Color(0,0,0,150));
        g2d.fillRoundRect(8,8,170,180,20,20);

        g2d.setColor(new Color(80,180,255));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(8,8,170,180,20,20);

        g.setFont(new Font("Arial", Font.BOLD, 15));

        int x = 20;
        int y = 30;

        String currentUserName = UserSession.getUserName();
        g.setColor(Color.WHITE);
        g.drawString("Player: " + currentUserName, x, y);

        y += 25;
        g.setColor(new Color(100,100,200));
        g.drawString("Score: " + score, x, y);

        y += 25;
        g.setColor(new Color(190,40,130));
        g.drawString("Level: " + currentLevel, x, y);

        y += 25;
        g.setColor(Color.GREEN);
        g.drawString("Lives: " + player.hp, x, y);

        y += 25;
        g.setColor(new Color(189,46,80));
        g.drawString("Bullets: " + bulletCount, x,y);


        if(System.currentTimeMillis() < rapidFireEndTime)
            g.drawImage(rapidFireIcon,20,145,32,32,null);

        if (System.currentTimeMillis() < shieldEndTime)
            g.drawImage(shieldIcon, 52, 145, 32, 32, null);

        if (System.currentTimeMillis() < freezeEndTime)
            g.drawImage(freezeIcon, 84, 145, 32, 32, null);


        if (player.image != null) {
            g2d.drawImage(player.image, player.x, player.y, player.width, player.height, null);
        } else {
            g2d.setColor(Color.CYAN);
            g2d.fillRect(player.x, player.y, player.width, player.height);
        }
        if(System.currentTimeMillis() < shieldEndTime){

            g2d.setColor(new Color(250,20,100));

            g2d.drawOval(
                    player.x - 8,
                    player.y - 8,
                    player.width + 16,
                    player.height + 16
            );
        }

        for (Bullet b : bullets) {
            b.draw(g);
        }
        if (currentLevel == 4 || currentLevel == 8) {
            if (boss != null) boss.draw(g);}
        else{
            enemyGrid.draw(g);
        }

        for (int i = explosions.size() - 1; i >= 0; i--) {
            Explosion exp = explosions.get(i);
            if (exp.isFinished()) {
                explosions.remove(i); // اگر زمانش تمام شد، از لیست پاک کن
            } else {
                exp.draw(g); // در غیر این صورت رسمش کن
            }
        }

        for (Egg egg : eggs) {
            egg.draw(g);
        }

        for(EnemyBullet b:enemyBullets){
            b.draw(g);
        }

        for(PowerUp p : powerUps){
            p.draw(g);
        }

        if (isPaused) {

            g2d.setColor(new Color(0,0,0,170));
            g2d.fillRect(0,0,getWidth(),getHeight());

            g2d.setColor(Color.blue);
            g2d.setFont(new Font("Arial", Font.BOLD, 60));

            String msg = "PAUSED";

            FontMetrics fm = g2d.getFontMetrics();

            g2d.drawString(
                    msg,
                    (getWidth()-fm.stringWidth(msg))/2,
                    getHeight()/2
            );

            g2d.setFont(new Font("Arial", Font.PLAIN, 22));

            String sub = "Press P to Continue";

            g2d.drawString(
                    sub,
                    (getWidth()-g2d.getFontMetrics().stringWidth(sub))/2,
                    getHeight()/2 + 45
            );
        }

        if (isGameOver) {
            int boxWidth = 450;
            int boxHeight = 220;

            int boxX = (getWidth() - boxWidth) / 2;
            int boxY = (getHeight() - boxHeight) / 2 - 30;

            // رنگ داخل باکس
            g2d.setColor(new Color(20, 20, 40, 220));
            g2d.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 25, 25);

            // رنگ حاشیه
            g2d.setColor(new Color(255, 80, 80));
            g2d.setStroke(new BasicStroke(3));
            g2d.drawRoundRect(boxX, boxY, boxWidth, boxHeight, 25, 25);



            g2d.setColor(new Color(0,0,0,150));
            g2d.fillRect(0,0,getWidth(),getHeight());
            g2d.setColor(new Color(189,46,80));
            g2d.setFont(new Font("Arial", Font.BOLD, 60));
            String msg = "GAME OVER!";
            FontMetrics fm = g2d.getFontMetrics();
            g2d.drawString(msg, (getWidth() - fm.stringWidth(msg)) / 2, getHeight() / 2);

            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 28));
            String scoreMsg = "Score: " + score;
            FontMetrics scoreFm = g2d.getFontMetrics();
            g2d.drawString(
                    scoreMsg,
                    (getWidth() - scoreFm.stringWidth(scoreMsg)) / 2,
                    (getHeight() / 2) + 40
            );

            g2d.setColor(new Color(250,10,250));
            g2d.setFont(new Font("Arial", Font.PLAIN, 20));
            String subMsg = "Press ESC to return to Menu";
            g2d.drawString(subMsg, (getWidth() - g2d.getFontMetrics().stringWidth(subMsg)) / 2, (getHeight() / 2) + 110);
        }
        if (isWin && !isGameOver) {
            int boxWidth = 450;
            int boxHeight = 220;

            int boxX = (getWidth() - boxWidth) / 2;
            int boxY = (getHeight() - boxHeight) / 2 - 30;

            // رنگ داخل باکس
            g2d.setColor(new Color(20, 20, 40, 220));
            g2d.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 25, 25);

            // رنگ حاشیه
            g2d.setColor(new Color(100, 180, 80));
            g2d.setStroke(new BasicStroke(3));
            g2d.drawRoundRect(boxX, boxY, boxWidth, boxHeight, 25, 25);



            g2d.setColor(new Color(0,0,0,150));
            g2d.fillRect(0,0,getWidth(),getHeight());
            g2d.setColor(new Color(10,200,98));
            g2d.setFont(new Font("Arial", Font.BOLD, 60));
            String msg = "YOU WIN!";
            FontMetrics fm = g2d.getFontMetrics();
            g2d.drawString(msg,
                    (getWidth() - fm.stringWidth(msg)) / 2,
                    getHeight() / 2);

            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 28));
            String scoreMsg = "Score: " + score;
            FontMetrics scoreFm = g2d.getFontMetrics();
            g2d.drawString(
                    scoreMsg,
                    (getWidth() - scoreFm.stringWidth(scoreMsg)) / 2,
                    (getHeight() / 2) + 40
            );

            g2d.setColor(new Color(250,10,250));
            g2d.setFont(new Font("Arial", Font.PLAIN, 20));
            String subMsg = "Press ESC to return to Menu";
            g2d.drawString(subMsg,
                    (getWidth() - g2d.getFontMetrics().stringWidth(subMsg)) / 2,
                    (getHeight() / 2) + 110);
        }
}

    @Override
    public void keyPressed(KeyEvent e){
        int key = e.getKeyCode();

        if (isGameOver || isWin) {
            if (key == KeyEvent.VK_ESCAPE) {

                    gameTimer.stop();
                    resetGame();
                    frame.showPage("MENU");

                return;
            }
            return;
        }

        if(key==KeyEvent.VK_LEFT)
            leftPressed = true;

        if(key==KeyEvent.VK_RIGHT)
            rightPressed = true;

        if (key==KeyEvent.VK_UP)
            upPressed = true;

        if (key==KeyEvent.VK_DOWN)
            downPressed = true;

        if (key==KeyEvent.VK_SPACE)
            spacePressed = true;

        if (key == KeyEvent.VK_P)
            isPaused = !isPaused;

    }

    @Override
    public void keyReleased(KeyEvent e){
        int key = e.getKeyCode();

        if(key==KeyEvent.VK_LEFT)
            leftPressed = false;

        if(key==KeyEvent.VK_RIGHT)
            rightPressed = false;

        if (key==KeyEvent.VK_UP)
            upPressed = false;

        if (key==KeyEvent.VK_DOWN)
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
        String planeName =
                DatabaseManager.getSelectedPlane(UserSession.getUserName());

        PlaneInfo planeInfo =
                DatabaseManager.getPlaneInfo(planeName);

        player = new Plane(planeInfo);
        lastShotTime = 0;
        currentLevel = 1;
        score = 0;
        bulletCount = 1;
        enemyGrid = new EnemyGrid(currentLevel);
        boss = null;
        bulletCount = 1;
        bullets.clear();
        enemyBullets.clear();
        powerUps.clear();
        eggs.clear();
        rapidFireEndTime = 0;
        shieldEndTime = 0;
        freezeEndTime = 0;
        isGameOver = false;
        isWin = false;
        isPaused = false;
    }

    private void gameOver() {

        SoundManager.playGameOver();

        isGameOver = true;
        gameTimer.stop();

        DatabaseManager.saveGame(
                UserSession.getUserName(),
                score,
                currentLevel,
                DatabaseManager.getSoundSetting(UserSession.getUserName(),"bg_music"),
                DatabaseManager.getSoundSetting(UserSession.getUserName(),"shot_sound"),
                DatabaseManager.getSoundSetting(UserSession.getUserName(),"crash_sound"),
                DatabaseManager.getSoundSetting(UserSession.getUserName(),"game_over_sound")
        );

        DatabaseManager.updateUserStats(
                UserSession.getUserName(),
                score,
                currentLevel
        );
    }

    public void addScore(int points) {
        this.score += points;
    }


}
