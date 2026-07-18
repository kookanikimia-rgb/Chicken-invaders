package com.game.entities;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class EnemyGrid {

    private float gridX,gridY;

    private int direction;

    private float gridSpeed;
    private int dropStep;

    public int[][] cellHits;

    private int currentLevel;
    public LevelConfig config;

    public ArrayList<Enemy>  gridEnemies;
    protected int replacementDirection;

    public EnemyGrid(int level){
        this.currentLevel = level;

        this.gridEnemies = new ArrayList<>();

        this.gridX = 0;
        this.gridY = 50;

        this.direction = 1;

        config = LevelConfig.getLevel(level);
        this.gridSpeed = config.speed;
        this.dropStep = config.dropStep;

        this.cellHits = new int[5][8];


        initGrid(level);
    }


    private void initGrid(int level){

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 8; col++) {

                cellHits[row][col] = config.initialCellHits;


                Enemy enemy = createEnemyForLevel(level, row, col);

                enemy.row = row;
                enemy.col = col;

                enemy.x = gridX + col * 40;
                enemy.y = gridY + row * 40;

                gridEnemies.add(enemy);
            }
        }
    }

    private boolean hasReplacement(int row, int col) {

        for (Enemy e : gridEnemies) {
            if (e.isReplacement && e.row == row && e.col == col) {
                return true;
            }
        }
        return false;
    }

    public void spawnReplacementEnemy(int row, int col) {

        if (hasReplacement(row, col))
            return;

        // استفاده از متد قبلی برای ساخت مرغ مناسب سطح
        Enemy replacement = createEnemyForLevel(this.currentLevel, row, col);

        replacement.isReplacement = true; // علامت‌گذاری به عنوان جایگزین
        replacement.row = row;
        replacement.col = col;

        // تعیین نقطه شروع: تصادفی از گوشه بالا-چپ یا بالا-راست
        Random rand = new Random();
        if (rand.nextBoolean()) {
            replacement.x = 0;
            // گوشه چپ
        } else {
            replacement.x = 580;
            // گوشه راست (عرض صفحه)
        }
        replacement.y = 0; // از بالای صفحه شروع می‌کند

        replacement.targetX=gridX+col*40;
        replacement.targetY=gridY+row*40;

        gridEnemies.add(replacement);
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

            return (rand.nextInt(2) == 0) ? new ZigzagEnemy(config.initialCellHits,config.eggInterval) : new ShooterEnemy(config.initialCellHits,config.eggInterval);

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

    public void update(int screenWidth){

        boolean hitEdge = false;

        // بررسی برخورد
        for(Enemy e : gridEnemies){

            if(e.isReplacement)
                continue;

            float nextX = e.x + direction * gridSpeed;

            if(nextX < 0 || nextX + e.width > screenWidth){
                hitEdge = true;
                break;
            }
        }

        // برخورد
        if(hitEdge){

            direction *= -1;
            gridY += dropStep;
            for(Enemy e : gridEnemies){
                if (e.isReplacement) {
                    e.targetY += dropStep;
                } else {
                    e.y += dropStep;
                }
            }
        }
        // حرکت
        for (Enemy e : gridEnemies) {

            if (e.isReplacement) {

                // مقصد مرغ جایگزین همیشه جای سلول باشد
                e.targetX = gridX + e.col * 40;
                e.targetY = gridY + e.row * 40;

                e.update(direction, gridSpeed);

            } else {

                e.x += direction * gridSpeed;
                e.update(direction, gridSpeed);

            }
        }
        gridX+=direction*gridSpeed;
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
