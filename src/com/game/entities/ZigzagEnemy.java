package com.game.entities;

import java.awt.*;

public class ZigzagEnemy extends Enemy{
    private int timer = 0;


    public ZigzagEnemy(int hp){
        super(hp);
        pointValue = 20;
    }


    @Override

    public void update(int dir,int speed,int gx,int gy){
        timer++;
        int offset = (int)(Math.sin(timer*0.1)*2);
        x += dir*speed + offset;
    }


    @Override
    protected Color getColor() {
        return Color.GREEN;
    }
}
