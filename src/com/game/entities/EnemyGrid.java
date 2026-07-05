package com.game.entities;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class EnemyGrid {

    public ArrayList<Enemy>  gridEnemies;
    private int gridX,gridY;
    private int direction;
    private int gridSpeed;
    private int dropStep;
    public int[][] cellHits;
    private int currentLevel;

    public EnemyGrid(int level){
        this.currentLevel = level;
        this.gridEnemies = new ArrayList<>();
        this.gridX = 0;
        this.gridY = 50;
        this.direction = 1;
        this.gridSpeed = 1;
        this.dropStep = 20;
        this.cellHits = new int[5][8];
        initGrid(level);
    }

    public void spawnReplacementEnemy(int row, int col) {
        // استفاده از متد قبلی برای ساخت مرغ مناسب سطح
        Enemy replacement = createEnemyForLevel(this.currentLevel, row, col);

        replacement.isReplacement = true; // علامت‌گذاری به عنوان جایگزین
        replacement.row = row;
        replacement.col = col;

        // تعیین نقطه شروع: تصادفی از گوشه بالا-چپ یا بالا-راست
        Random rand = new Random();
        if (rand.nextBoolean()) {
            replacement.x = 0; // گوشه چپ
        } else {
            replacement.x = 600; // گوشه راست (عرض صفحه)
        }
        replacement.y = 0; // از بالای صفحه شروع می‌کند

        gridEnemies.add(replacement);
    }

    private void initGrid(int level){


        int initialCellHits = 2;
        if (level >= 5 && level <= 7) {
            initialCellHits = (level == 5) ? 3 : 4;
        } else if (level == 3) {
            initialCellHits = 3;
        }

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 8; col++) {

                cellHits[row][col] = initialCellHits;


                Enemy enemy = createEnemyForLevel(level, row, col);


                enemy.x = gridX + col*60;
                enemy.y = gridY + row*60;
                enemy.row = row;
                enemy.col = col;

                gridEnemies.add(enemy);
            }
        }
    }

    private Enemy createEnemyForLevel(int level,int row,int col){
        Random rand = new Random();
        int type = rand.nextInt(3); // یک عدد تصادفی برای تنوع بخشیدن

        if (level == 1) {
            return new NormalEnemy(2); // فقط Normal

        } else if (level == 2) {
            // ترکیبی از Normal و Fast
            return (type == 0) ? new FastEnemy(1) : new NormalEnemy(2);

        } else if (level == 3) {
            // ترکیبی از Normal و Zigzag
            return (type == 0) ? new ZigzagEnemy(3) : new NormalEnemy(3);

        } else if (level >= 5 && level <= 6) {
            // ترکیبی از Shooter و Fast یا Zigzag
            if (level == 5) return (type == 0) ? new ShooterEnemy(3) : new FastEnemy(2);
            else return (type == 0) ? new ShooterEnemy(4) : new ZigzagEnemy(4);

        } else if (level == 7) {
            // همه انواع
            int allTypes = rand.nextInt(4);
            switch (allTypes) {
                case 0: return new NormalEnemy(4);
                case 1: return new FastEnemy(4);
                case 2: return new ZigzagEnemy(4);
                default: return new ShooterEnemy(4);
            }
        }
        return new NormalEnemy(2); // پیش‌فرض
    }


    public void update(int screenWidth){
        boolean hitEdge = false;

        for(Enemy e : gridEnemies){
            e.update(direction,gridSpeed,this.gridX,this.gridY);
            if (!e.isReplacement) {
                if (e.x + e.width > screenWidth || e.x < 0)
                    hitEdge = true;
            }
        }

        if(hitEdge){
            direction *= -1;
            gridY += dropStep;
            for (Enemy e: gridEnemies)
                if (!e.isReplacement)
                    e.y += dropStep;
        }
    }

    public void draw(Graphics g) {
        for (Enemy e : gridEnemies) {
            e.draw(g);
        }
    }

    public boolean isBottomReached() {
        for (Enemy e : gridEnemies) {
            // اگر هر مرغی به پیکسل ۷۵۰ رسید (کمی قبل از لبه پایین صفحه)
            if (e.y + e.height > 750) {
                return true;
            }
        }
        return false;
    }
}
