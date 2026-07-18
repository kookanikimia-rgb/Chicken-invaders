package com.game.entities;

import javax.swing.*;
import java.awt.*;

public class Explosion {

    private int x, y;
    private int maxSize;
    private long startTime;
    private final long duration = 300; // مدت زمان انفجار به میلی ثانیه
    private Image image;

    public Explosion(int x, int y, int maxSize,long gameTime) {
        this.x = x;
        this.y = y;
        this.maxSize = maxSize;
        this.startTime = gameTime;

        try {
            this.image = new ImageIcon(getClass().getResource("/Assets/images/explosion.png")).getImage();
        } catch (Exception e) {
            System.out.println("Explosion image not found!");
        }
    }

    public boolean isFinished(long gameTime) {
        return gameTime - startTime > duration;
    }

    public void draw(Graphics g,long gameTime) {
        long passedTime = gameTime - startTime;
        double progress = passedTime / (double) duration;

        // اندازه عکس از ۲۰ پیکسل شروع شده و تا maxSize بزرگ می‌شود
        int size = (int) (20 + (maxSize - 20) * progress);

        if (image != null) {
            g.drawImage(image, x - size / 2, y - size / 2, size, size, null);
        } else {
            // اگر عکس نبود، یک دایره نارنجی رسم کن
            g.setColor(Color.ORANGE);
            g.fillOval(x - size / 2, y - size / 2, size, size);
        }
    }
}
