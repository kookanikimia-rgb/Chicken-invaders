package com.game.entities;

import java.awt.*;

public class ShooterEnemy extends Enemy{
    public ShooterEnemy(int hp){
        super(hp);
    }

    @Override
    public void update(int dir,int speed,int gx,int gy){
        x += dir*speed;
    }

    @Override
    protected Color getColor() {
        return Color.MAGENTA;
    }
}
