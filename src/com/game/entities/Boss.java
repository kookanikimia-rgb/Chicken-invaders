package com.game.entities;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Boss {
    public float x, y;
    public int width;
    public int height;

    public int hp;
    public int maxHp;

    public int pointValue;

    protected float speedX;
    protected int direction;
    protected long lastAttackTime;

    public Image image;

    protected int angleY;

    public Boss() {

        x = 300 - width / 2; // شروع از وسط صفحه
        y = 50;

        direction = 1;
        lastAttackTime = 0;
        angleY = 0;

        }

    protected void horizontalMove(int screenWidth){

        x += speedX * direction;

        if(x <= 0){
            x = 0;
            direction = 1;
        }

        if(x + width >= screenWidth){
            x = screenWidth - width;
            direction = -1;
        }
    }

    public abstract void update(int screenWidth);

    public abstract List<Egg> attack(long gameTime);

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
    public void takeDamage(int damage){
        hp -=damage;
    }
    public boolean isDead(){
        return hp <= 0;
    }
    public Rectangle getBounds(){
        return new Rectangle((int)x,(int)y,width,height);
    }

}