package com.game.entities;

import java.awt.*;

public class Egg {
    public float x, y;
    public int width, height;
    public int speed;
    public float vx, vy;

    public Egg(int x, int y) {
        this.x = x;
        this.y = y;
        this.vx = 0;
        this.vy = 4;
        this.width = 8;
        this.height = 12;
        this.speed = 4;
    }
    public Egg(float x, float y, float vx, float vy) {
        this.x = x;
        this.y = y;
        this.width = 8;
        this.height = 12;
        this.speed = 4;
        this.vx = vx;
        this.vy = vy;
    }


    public void move() {
        x += vx;
        y += vy;
    }

    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillOval((int)x,(int)y, width, height);
    }

    public Rectangle getBounds() {
        return new Rectangle((int)x,(int)y, width, height);
    }
}
