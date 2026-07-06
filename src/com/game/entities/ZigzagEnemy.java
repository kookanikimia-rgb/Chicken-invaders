package com.game.entities;

import java.awt.*;

public class ZigzagEnemy extends Enemy{
    private int timer = 0;


    public ZigzagEnemy(int hp, int levelEggInterval){
        super(hp,levelEggInterval);
        pointValue = 20;
    }


    @Override
    public void update(int direction,int speed){

        if(isReplacement){

            timer++;

            x += (int)(Math.sin(timer * 0.3) * 3);

            moveToCell();

        }else{

            timer++;

            //x += direction * speed;
            x += (int)(Math.sin(timer * 0.2) * 2);
        }
    }

    @Override
    protected Color getColor() {
        return Color.GREEN;
    }
}
