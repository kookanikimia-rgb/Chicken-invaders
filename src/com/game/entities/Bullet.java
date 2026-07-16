package com.game.entities;

import javax.swing.*;
import java.awt.*;

public class Bullet {
    public int x,y;
    public int width;
    public int height;
    public int speed ;

    public Image image;

    public Bullet(int startX,int startY){
        this.x = startX;
        this.y = startY;
        this.width = 20;
        this.height = 35;
        this.speed = 10;

        try {
            this.image = new ImageIcon(getClass().getResource("/Assets/images/airplan/shot.png")).getImage();
        } catch (Exception e) {
            System.out.println("Bullet image not found!");
        }
    }

    public void move(){
        y -= speed;
    }

    public void draw(Graphics g){
        if (image != null) {
            g.drawImage(image, x, y, width, height, null);
        } else {
            g.setColor(new Color(50, 50, 120));
            g.fillRect(x, y, width, height);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
