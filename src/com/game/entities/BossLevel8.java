package com.game.entities;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class BossLevel8 extends Boss {

    private float angleY;

    public BossLevel8() {
        super();

        width = 250;
        height = 200;

        maxHp = 100;
        hp = 100;

        speedX = 2.0f;
        pointValue = 1000;

        try {
            this.image = new ImageIcon(getClass().getResource("/Assets/images/chicken/boss2.png")).getImage();
        } catch (Exception e) {
            System.out.println("Boos2Enemy image not found!");
        }

    }

    @Override
    public void update(int screenWidth){

        horizontalMove(screenWidth);

        angleY += 0.05f;

        y = 50 + (float)Math.sin(angleY) * 50;
    }

    @Override
    public List<Egg> attack(long gameTime) {
        List<Egg> newEggs = new ArrayList<>();
        long currentTime = gameTime;

        int interval = 1000;

        if (gameTime - lastAttackTime >= interval) {
            lastAttackTime = currentTime;
            // شلیک در ۸ جهت (زاویه ۴۵ درجه)
            for (int i = 0; i < 360; i += 45) {
                double rad = Math.toRadians(i);
                float vx = (float) (Math.cos(rad) * 5);
                float vy = (float) (Math.sin(rad) * 5);
                newEggs.add(new Egg(x + width / 2, y + height / 2, vx, vy));
            }
        }
        return newEggs;
    }
}
