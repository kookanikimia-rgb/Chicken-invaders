package com.game.entities;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class BossLevel4 extends Boss{

    public BossLevel4() {
        super();

        width = 170;
        height = 140;

        maxHp = 50;
        hp = 50;

        speedX = 1.5f;

        pointValue = 500;

        try {
            this.image = new ImageIcon(getClass().getResource("/Assets/images/chicken/boss1.png")).getImage();
        } catch (Exception e) {
            System.out.println("Boss1Enemy image not found!");
        }
    }

    @Override
    public void update(int screenWidth) {

        horizontalMove(screenWidth);

    }

    @Override
    public List<Egg> attack(long gameTime) {

        List<Egg> newEggs = new ArrayList<>();
        long currentTime = gameTime;

        int interval = 1500;

        if(gameTime-lastAttackTime >= interval) {
            lastAttackTime = currentTime;
            // شلیک در ۴ جهت: بالا، پایین، چپ، راست
            newEggs.add(new Egg(x + width / 2, y + height / 2, 0, -4)); // بالا
            newEggs.add(new Egg(x + width / 2, y + height / 2, 0, 4));  // پایین
            newEggs.add(new Egg(x + width / 2, y + height / 2, -4, 0)); // چپ
            newEggs.add(new Egg(x + width / 2, y + height / 2, 4, 0));  // راست

        }
        return newEggs;
    }
}
