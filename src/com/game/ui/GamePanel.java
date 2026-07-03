package com.game.ui;

import com.game.entities.Bullet;
import com.game.entities.Plane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class GamePanel extends JPanel implements KeyListener {

    private Plane player;
    private Timer gameTimer;

    private boolean rightPressed;
    private boolean leftPressed;
    private boolean upPressed;
    private boolean downPressed;
    private boolean spacePressed;

    private ArrayList<Bullet> bullets ;
    private int shootCooldown ;

    public GamePanel(MainFrame frame){
        bullets = new ArrayList<>();
        shootCooldown = 0;

        setPreferredSize(new Dimension(600,800));
        setBackground(Color.BLACK);
        setFocusable(true);

        player = new Plane();

        gameTimer = new Timer(16,e -> {
           update();
           repaint();
        });
        gameTimer.start();

        this.addKeyListener(this);
    }

    private void update(){
        if (leftPressed)
            player.moveLeft();

        if (rightPressed)
            player.moveRight();

        if (upPressed)
            player.moveUp();

        if (downPressed)
            player.moveDown();

        player.keepInBounds(getWidth(),getHeight());

        if(spacePressed && shootCooldown <=0){
            int centerX = player.x +(player.width/2);
            int bulletX = centerX-(5/2);
            bullets.add(new Bullet(bulletX,player.y));
            shootCooldown= 15;
        }

        if (shootCooldown > 0)
            shootCooldown--;

        for (int i = 0; i < bullets.size(); i++) {
            Bullet b = bullets.get(i);
            b.move();

            if (b.y < 0) {
                bullets.remove(i);
                i--;
            }
        }
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
    }

    @Override
    public void keyPressed(KeyEvent e){
        int key = e.getKeyCode();

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
}
