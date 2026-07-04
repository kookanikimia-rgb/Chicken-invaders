package com.game.entities;

import java.awt.*;
import java.util.ArrayList;

public class EnemyGrid {

    public ArrayList<Enemy>  gridEnemies;
    private int gridX,gridY;
    private int direction;
    private int gridSpeed;
    private int dropStep;
    private int[][] cellHits;

    public EnemyGrid(int level){
        this.gridEnemies = new ArrayList<>();
        this.gridX = 0;
        this.gridY = 50;
        this.direction = 1;
        this.gridSpeed = 1;
        this.dropStep = 20;
        this.cellHits = new int[5][8];
        initGrid(level);
    }

    private void initGrid(int level){
        int hp = (level <= 3) ? 2 : 3;
        for (int row = 0; row < 5 ;row++){
            for (int col = 0 ; col < 8 ; col++){
                cellHits[row][col] = hp;
                gridEnemies.add(new NormalEnemy(gridX + col * 50, gridY + row * 50, hp));
            }
        }
    }

    public void update(int screenWidth){
        boolean hitEdge = false;

        for(Enemy e : gridEnemies){
            e.update(direction,gridSpeed);
            if (e.x + e.width > screenWidth || e.x < 0)
                hitEdge = true;
        }

        if(hitEdge){
            direction *= -1;
            gridY += dropStep;
            for (Enemy e: gridEnemies)
                e.y += dropStep;
        }
    }

    public void draw(Graphics g) {
        for (Enemy e : gridEnemies) {
            e.draw(g);
        }
    }
}
