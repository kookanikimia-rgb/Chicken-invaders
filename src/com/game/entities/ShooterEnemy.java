package com.game.entities;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class ShooterEnemy extends Enemy {

    private Random random;
    private long lastShotTime;
    private static final int SHOOT_INTERVAL = 3000;

    public ShooterEnemy(int hp, int levelEggInterval) {
        super(hp, levelEggInterval);

        pointValue = 25;

        random = new Random();
        lastShotTime = System.currentTimeMillis();

        try {
            this.image = new ImageIcon(getClass().getResource("/Assets/images/chicken/shooter_chicken.png")).getImage();
        } catch (Exception e) {
            System.out.println("ShooterEnemy image not found!");
        }
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