package com.game.entities;

import java.awt.*;

public class NormalEnemy extends Enemy{

    public NormalEnemy(int hp,int levelEggInterval){
        super(hp,levelEggInterval);
        pointValue = 10;
    }

    @Override
    public void update(int direction,int speed){

        if(isReplacement){

            moveToCell();

        }
    }

    @Override
    protected Color getColor(){
        return Color.red;
    }
}

