package com.game.ui;

import com.game.entities.Bullet;
import com.game.entities.Enemy;
import com.game.entities.EnemyGrid;
import com.game.entities.Plane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class GamePanel extends JPanel implements KeyListener {

    private Plane player;
    private Timer gameTimer;
    private boolean isGameOver;
    private boolean isWin;
    private boolean rightPressed;
    private boolean leftPressed;
    private boolean upPressed;
    private boolean downPressed;
    private boolean spacePressed;

    private ArrayList<Bullet> bullets ;
    private int shootCooldown ;
    private EnemyGrid enemyGrid;


    public GamePanel(MainFrame frame){
        isGameOver = false;
        bullets = new ArrayList<>();
        shootCooldown = 0;

        enemyGrid = new EnemyGrid(1);


        setPreferredSize(new Dimension(600,800));
        setBackground(Color.BLACK);
        setFocusable(true);

        player = new Plane();

        gameTimer = new Timer(16, e -> {
            update();
            repaint();
        });


        this.addKeyListener(this);
    }

    private void update() {
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
        if (spacePressed && shootCooldown <= 0) {
            // تیر اول: سمت چپ پلیر
            bullets.add(new Bullet(player.x + 5, player.y));

            // تیر دوم: وسط پلیر
            int centerX = player.x + (player.width / 2) - 2;
            bullets.add(new Bullet(centerX, player.y));

            // تیر سوم: سمت راست پلیر
            bullets.add(new Bullet(player.x + player.width - 10, player.y));

            shootCooldown = 20; // چون حالا ۳ تا تیر می‌زند، کول‌داون را کمی بیشتر کردیم تا بازی خیلی راحت نشود
        }

        if (shootCooldown > 0)
            shootCooldown--;
        //  آپدیت تیر ها و برخورد با دشمنان
        for (int i = 0; i < bullets.size(); i++) {
            Bullet b = bullets.get(i);
            b.move();

            if (b.y < 0) {
                bullets.remove(i);
                i--;
                continue;
            }

        for (Enemy e : enemyGrid.gridEnemies)
            if (b.getBounds().intersects(e.getBounds())) {
                e.takeDamage();
                bullets.remove(i);
                i--;
                break;
            }
        }
        //برخورد پلیر با دشمن
        for(Enemy e : enemyGrid.gridEnemies)
            if(player.getBounds().intersects(e.getBounds())){
                player.takeDamage();
                e.hp = 0;
                if(player.hp <=0 )
                    gameOver();
            }

        for (int i = 0; i < enemyGrid.gridEnemies.size(); i++) {
            Enemy e = enemyGrid.gridEnemies.get(i);
            if (e.isDead()) {
                // ۱. کم کردن از شمارنده خانه
                enemyGrid.cellHits[e.row][e.col]--;

                // ۲. اگر هنوز حق جایگزینی هست، مرغ جدید بساز
                if (enemyGrid.cellHits[e.row][e.col] > 0) {
                    enemyGrid.spawnReplacementEnemy(e.row, e.col);
                }

                // ۳. حذف مرغ مرده از لیست
                enemyGrid.gridEnemies.remove(i);
                i--;
            }
        }

        enemyGrid.update(getWidth());

        if (enemyGrid.isBottomReached()) {
            gameOver();}
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.CYAN);
        g2d.fillRect(player.x,player.y,player.width,player.height);

        for (Bullet b : bullets) {
            b.draw(g);
        }
        enemyGrid.draw(g);

        if (isGameOver) {
            g2d.setColor(new Color(0,0,0,150));
            g2d.fillRect(0,0,getWidth(),getHeight());
            g2d.setColor(new Color(10,200,98));
            g2d.setFont(new Font("Arial", Font.BOLD, 60));
            String msg = "GAME OVER!";
            FontMetrics fm = g2d.getFontMetrics();
            g2d.drawString(msg, (getWidth() - fm.stringWidth(msg)) / 2, getHeight() / 2);

            g2d.setColor(new Color(100,30,68));
            g2d.setFont(new Font("Arial", Font.PLAIN, 20));
            String subMsg = "Press 'R' to Restart";
            g2d.drawString(subMsg, (getWidth() - g2d.getFontMetrics().stringWidth(subMsg)) / 2, (getHeight() / 2) + 60);
        }
        if (enemyGrid.gridEnemies.isEmpty()) {
            isWin = true;
            g2d.setColor(new Color(0,0,0,150));
            g2d.fillRect(0,0,getWidth(),getHeight());
            g2d.setColor(new Color(189,46,80));
            g2d.setFont(new Font("Arial", Font.BOLD, 60));
            String msg = "YOU WIN!";
            FontMetrics fm = g2d.getFontMetrics();
            g2d.drawString(msg, (getWidth() - fm.stringWidth(msg)) / 2, getHeight() / 2);

            g2d.setColor(new Color(100,30,69));
            g2d.setFont(new Font("Arial", Font.PLAIN, 20));
            String subMsg = "Press 'R' to Restart";
            g2d.drawString(subMsg, (getWidth() - g2d.getFontMetrics().stringWidth(subMsg)) / 2, (getHeight() / 2) + 60);
        }

}

    @Override
    public void keyPressed(KeyEvent e){
        int key = e.getKeyCode();

        if (isGameOver || isWin) {
            if (key == KeyEvent.VK_R) {
                startGame(); // ریستارت بازی با دکمه R
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

        if (!gameTimer.isRunning()) {
            gameTimer.start();
        }
    }


    private void resetGame() {
        player = new Plane();
        enemyGrid = new EnemyGrid(1);
        bullets.clear();
        shootCooldown = 0;
        isGameOver = false;
    }

    private void gameOver() {
        isGameOver = true;
        gameTimer.stop();
    }

}
