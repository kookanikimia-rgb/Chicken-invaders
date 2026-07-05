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
    public LevelConfig config;

    public EnemyGrid(int level){
        this.currentLevel = level;
        this.gridEnemies = new ArrayList<>();
        this.gridX = 0;
        this.gridY = 50;
        this.direction = 1;

        config = LevelConfig.getLevel(level);
        this.gridSpeed = (int)config.speed;
        this.dropStep = config.dropStep;

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

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 8; col++) {

                cellHits[row][col] = config.initialCellHits;


                Enemy enemy = createEnemyForLevel(level, row, col);
                enemy.x = gridX + col*50;
                enemy.y = gridY + row*50;
                enemy.row = row;
                enemy.col = col;

                gridEnemies.add(enemy);
            }
        }
    }

    private Enemy createEnemyForLevel(int level,int row,int col) {
        String type = config.enemyType;
        Random rand = new Random();

        if (type.equals("Normal")) {

            return new NormalEnemy(config.initialCellHits,config.eggInterval); // فقط Normal

        } else if (type.equals("Normal+Fast")) {

            return (rand.nextInt(2) == 0) ? new FastEnemy(config.initialCellHits,config.eggInterval) : new NormalEnemy(config.initialCellHits,config.eggInterval);

        } else if (type.equals("Normal+Zigzag")) {

            return (rand.nextInt(2) == 0) ? new ZigzagEnemy(config.initialCellHits,config.eggInterval) : new NormalEnemy(config.initialCellHits,config.eggInterval);

        } else if (type.equals("Shooter+Fast")) {

            return (rand.nextInt(2) == 0) ? new ShooterEnemy(config.initialCellHits,config.eggInterval) : new FastEnemy(config.initialCellHits,config.eggInterval);

        }else if(type.equals("Zigzag+Shooter")){

            return (rand.nextInt(2) == 0) ? new ZigzagEnemy(config.initialCellHits,config.eggInterval) : new FastEnemy(config.initialCellHits,config.eggInterval);

        }else if (type.equals("All")) {

            int allTypes = rand.nextInt(4);
            switch (allTypes) {
                case 0: return new NormalEnemy(config.initialCellHits,config.eggInterval);
                case 1: return new FastEnemy(config.initialCellHits,config.eggInterval);
                case 2: return new ZigzagEnemy(config.initialCellHits,config.eggInterval);
                default: return new ShooterEnemy(config.initialCellHits,config.eggInterval);
            }

        }
        return new NormalEnemy(config.initialCellHits,config.eggInterval);
    }


    public void update(int screenWidth) {
        boolean hitEdge = false;

        for (Enemy e : gridEnemies) {
            e.update(direction, gridSpeed, this.gridX, this.gridY,screenWidth);
            if (!e.isReplacement) {
                if (e.x + e.width > screenWidth || e.x < 0)
                    hitEdge = true;
            }
        }

        if (hitEdge) {
            direction *= -1;
            gridY += dropStep;
            for (Enemy e : gridEnemies)
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
