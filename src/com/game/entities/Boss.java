package com.game.entities;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Boss {
    public float x, y;
    public int width;
    public int height;

    public int hp;
    public int maxHp;

    public int level;
    public int pointValue;

    private float speedX;
    private int direction;
    private long lastAttackTime;

    public Image image;


    private float angleY;

    public Boss(int level) {
        this.level = level;

        this.width = 120;
        this.height = 120;
        this.x = 300 - width / 2; // شروع از وسط صفحه
        this.y = 50;

        this.direction = 1;
        this.lastAttackTime = 0;
        this.angleY = 0;

        if (level == 4) {
            this.maxHp = 50;
            this.hp = 50;
            this.speedX = 1.5f;
            this.pointValue = 500;

            try {
                this.image = new ImageIcon(getClass().getResource("/Assets/images/chicken/boss1.png")).getImage();
            } catch (Exception e) {
                System.out.println("Boss1Enemy image not found!");
            }

        } else {
            this.maxHp = 100;
            this.hp = 100;
            this.speedX = 2.0f;
            this.pointValue = 1000;

            try {
                this.image = new ImageIcon(getClass().getResource("/Assets/images/chicken/boss2.png")).getImage();
            } catch (Exception e) {
                System.out.println("Boos2Enemy image not found!");
            }

        }

    }

    public void update(int screenWidth){
        x += speedX*direction;
        if (x <= 0) {
            x = 0;
            direction = 1;
        }

        if (x + width >= screenWidth) {
            x = screenWidth - width;
            direction = -1;
        }
        if(level == 8){
            angleY += 0.05f;
            y = 50 + (float) Math.sin(angleY) * 50;
        }
    }

    public List<Egg> attack(){
        List<Egg> newEggs = new ArrayList<>();
        long currentTime = System.currentTimeMillis();

        int interval = (level == 4) ? 1500 : 1000;

        if(currentTime-lastAttackTime >= interval) {
            lastAttackTime = currentTime;
            if (level == 4) {
                // شلیک در ۴ جهت: بالا، پایین، چپ، راست
                newEggs.add(new Egg(x + width / 2, y + height / 2, 0, -4)); // بالا
                newEggs.add(new Egg(x + width / 2, y + height / 2, 0, 4));  // پایین
                newEggs.add(new Egg(x + width / 2, y + height / 2, -4, 0)); // چپ
                newEggs.add(new Egg(x + width / 2, y + height / 2, 4, 0));  // راست
            } else {
                // شلیک در ۸ جهت (زاویه ۴۵ درجه)
                for (int i = 0; i < 360; i += 45) {
                    double rad = Math.toRadians(i);
                    float vx = (float) (Math.cos(rad) * 5);
                    float vy = (float) (Math.sin(rad) * 5);
                    newEggs.add(new Egg(x + width / 2, y + height / 2, vx, vy));
                }
            }
        }
        return newEggs;
    }

    public void draw(Graphics g){
        // ۱. رسم عکس غول به جای مستطیل نارنجی
        if (image != null) {
            g.drawImage(image, (int)x, (int)y, width, height, null);
        } else {
            // اگر عکس لود نشد، همان مستطیل نارنجی قدیمی را بکش
            g.setColor(new Color(220,89,50));
            g.fillRect((int)x, (int)y, width, height);
        }

        // ۲. رسم نوار سلامت
        int barWidth = 100;
        int barHeight = 10;
        int barX = (int)x + (width / 2) - (barWidth / 2);
        int barY = (int)y - 20;

        // پس‌زمینه نوار (خاکستری)
        g.setColor(Color.GRAY);
        g.fillRect(barX, barY, barWidth, barHeight);

        // مقدار سلامت (سبز یا قرمز)
        float healthPercent = (float) hp / maxHp;
        g.setColor(healthPercent > 0.3f ? Color.GREEN : Color.RED);
        g.fillRect(barX, barY, (int) (barWidth * healthPercent), barHeight);

        // حاشیه نوار
        g.setColor(Color.BLACK);
        g.drawRect(barX, barY, barWidth, barHeight);
    }
    public void takeDamage(){
        hp--;
    }
    public boolean isDead(){
        return hp <= 0;
    }
    public Rectangle getBounds(){
        return new Rectangle((int)x,(int)y,width,height);
    }

}