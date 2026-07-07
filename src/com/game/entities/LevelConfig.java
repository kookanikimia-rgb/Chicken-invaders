package com.game.entities;

public class LevelConfig {

    public float speed;
    public int dropStep;
    public int eggInterval;
    public int initialCellHits;
    public String enemyType;

    public LevelConfig(float speed,int drop,int interval,int hits,String type){
        this.speed = speed;
        this.dropStep = drop;
        this.eggInterval = interval;
        this.initialCellHits = hits;
        this.enemyType = type;
    }

    public static LevelConfig getLevel(int level){
        switch (level){
            case 1: return new LevelConfig(1.0f,20,3000,2,"Normal");
            case 2: return new LevelConfig(1.5f,20,2000,2,"Normal+Fast");
            case 3: return new LevelConfig(2.0f,25,1500,3,"Normal+Zigzag");
            case 5: return new LevelConfig(2.5f,25,1000,3,"Shooter+Fast");
            case 6: return new LevelConfig(3.0f,30,800,4,"Zigzag+Shooter");
            case 7: return new LevelConfig(3.5f, 30, 700, 4, "All");
            default: return new LevelConfig(1.0f, 20, 3000, 2, "Normal");
        }
    }
}
