package com.game.entities;

import java.awt.*;

public class Egg {
    public int x, y, width = 8, height = 12;
    public int speed = 4;

    public Egg(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void move() {
        y += speed; // تخم به پایین می‌رود
    }

    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillOval(x, y, width, height); // تخم‌ها بیضی شکل باشند
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
