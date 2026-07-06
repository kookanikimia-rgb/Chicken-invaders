package com.game.entities;

import java.awt.*;

public class FastEnemy extends Enemy{

    public FastEnemy(int hp, int levelEggInterval){
        super(hp,levelEggInterval);
        pointValue = 15;
    }
    @Override
    protected int getReplacementSpeed() {
        return 4;
    }
    @Override
    public void update(int direction,int speed){
        if(isReplacement){

            moveToCell();

        }
    }

    @Override
    protected Color getColor() {
        return Color.yellow;
    }
}
