package com.game.entities;

import javax.swing.*;
import java.awt.*;

public class EnemyBullet {

    public int x,y;
    public int width = 40;
    public int height = 35;

    private int dx;

    public Image image;

    public EnemyBullet(int x,int y,int playerX){

        this.x = x;
        this.y = y;

        if(playerX > x)
            dx = -6;
        else
            dx = 6;

        try {
            this.image = new ImageIcon(getClass().getResource("/Assets/images/enemy bullet.png")).getImage();
        } catch (Exception e) {
            System.out.println("Bullet image not found!");
        }
    }

    public void move(){
        x -=dx;
    }

    public void draw(Graphics g){

        if (image != null) {
            g.drawImage(image, x, y, width, height, null);
        } else {
            g.setColor(Color.ORANGE);
            g.fillRect(x, y, width, height);
        }
    }

    public Rectangle getBounds(){
        return new Rectangle(x,y,width,height);
    }
}