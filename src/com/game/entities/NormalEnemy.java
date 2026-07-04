package com.game.entities;

import java.awt.*;

public class NormalEnemy extends Enemy{

    public NormalEnemy(int x,int y, int hp){
        super(x,y,hp);
    }
    @Override
    public void update(int dir,int speed){
        x += dir*speed;
    }

    @Override
    protected Color getColor(){
        return Color.red;
    }
}
