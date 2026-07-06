package com.game.entities;

import java.awt.*;
import java.util.Random;

public class ShooterEnemy extends Enemy {

    private Random random;
    private long lastShotTime;
    private static final int SHOOT_INTERVAL = 3000; // هر ۲.۵ ثانیه حداکثر یک شلیک

    public ShooterEnemy(int hp, int levelEggInterval) {
        super(hp, levelEggInterval);

        pointValue = 25;

        random = new Random();
        lastShotTime = System.currentTimeMillis();
    }

    @Override
    public void update(int direction, int gridSpeed) {

        if (isReplacement) {
            moveToCell();
        }
    }
    public EnemyBullet shoot(int playerX) {

        long currentTime = System.currentTimeMillis();

        if (currentTime - lastShotTime >= SHOOT_INTERVAL) {

            // احتمال ۳۰ درصد برای شلیک
            if (random.nextInt(100) < 30) {

                lastShotTime = currentTime;

                return new EnemyBullet(
                        x + width / 2,
                        y + height / 2,
                        playerX
                );
            }
        }

        return null;
    }

    @Override
    protected Color getColor() {
        return Color.MAGENTA;
    }
}