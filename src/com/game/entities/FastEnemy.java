package com.game.entities;

import java.awt.*;

public class FastEnemy extends Enemy{

    public FastEnemy(int x,int y,int hp){
        super(x,y,hp);
    }

    @Override
    public void update(int dir,int speed){
        x += dir*speed;
    }

    @Override
    protected Color getColor() {
        return Color.yellow;
    }
}
