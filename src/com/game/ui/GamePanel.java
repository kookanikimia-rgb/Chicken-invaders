package com.game.ui;

import com.game.entities.Plane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GamePanel extends JPanel implements KeyListener {

    private Plane player;
    private Timer gameTimer;

    private boolean rightPressed;
    private boolean leftPressed;
    private boolean upPressed;
    private boolean downPressed;

    public GamePanel(MainFrame frame){
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

        player.keepInBounds();
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.CYAN);
        g2d.fillRect(player.x,player.y,player.width,player.height);

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
    }

    @Override
    public void keyTyped(KeyEvent e){

    }
}
