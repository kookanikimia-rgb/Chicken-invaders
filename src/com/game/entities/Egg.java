package com.game.entities;

import javax.swing.*;
import java.awt.*;

public class Egg {
    public float x, y;
    public int width, height;
    public int speed;
    public float vx, vy;
    public Image image;

    public Egg(int x, int y) {
        this.x = x;
        this.y = y;
        this.vx = 0;
        this.vy = 4;
        this.width = 20;
        this.height = 32;
        this.speed = 4;

        loadEggImage();
    }
    public Egg(float x, float y, float vx, float vy) {
        this.x = x;
        this.y = y;
        this.width = 20;
        this.height = 32;
        this.speed = 4;
        this.vx = vx;
        this.vy = vy;

        loadEggImage();
    }

    private void loadEggImage() {
        try {
            this.image = new ImageIcon(getClass().getResource("/Assets/images/chicken/egg.png")).getImage();
        } catch (Exception e) {
            System.out.println("Egg image not found!");
        }
    }

    public void move() {
        x += vx;
        y += vy;
    }

    public void draw(Graphics g) {
        if (image != null) {
            g.drawImage(image, (int)x, (int)y, width, height, null);
        } else {
            // اگر عکس لود نشد، همان دایره سفید قدیمی را بکش
            g.setColor(Color.WHITE);
            g.fillOval((int)x, (int)y, width, height);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle((int)x,(int)y, width, height);
    }
}
