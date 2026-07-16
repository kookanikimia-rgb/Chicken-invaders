package com.game.entities;

import javax.swing.*;
import java.awt.*;

public class Plane {
    public int x,y;
    public int width = 80;
    public int height = 80;
    public int hp = 3;
    public int speed = 5;

    public Image image;

    public Plane(){
        this.x = 600/2 - width/2;
        this.y = 800-100;

        try {
            this.image = new ImageIcon(getClass().getResource("/Assets/images/airplan/1.png")).getImage();

        } catch (Exception e) {
            System.out.println("Plane image not found! Using default rectangle.");
        }

    }

    public void moveLeft(){
        x -= speed;
    }
    public void moveRight(){
        x += speed;
    }
    public void moveUp() {
        y -= speed;
    }
    public void moveDown() {
        y += speed;
    }

    public void takeDamage(){
        hp--;
        if (hp < 0)
            hp = 0;
    }


    public void keepInBounds(int screenWidth,int screenHeight) {
        if (x < 0) x = 0;

        if (x > screenWidth - width) x = screenWidth - width;

        if (y < 0) y = 0;

        if (y > screenHeight - height) y = screenHeight - height;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
