package com.game.entities;

import javax.swing.*;
import java.awt.*;

public class ZigzagEnemy extends Enemy{
    private int timer = 0;


    public ZigzagEnemy(int hp, int levelEggInterval){
        super(hp,levelEggInterval);
        pointValue = 20;

        try {
            this.image = new ImageIcon(getClass().getResource("/Assets/images/chicken/zigzag_chicken.png")).getImage();
        } catch (Exception e) {
            System.out.println("ZigZagEnemy image not found!");
        }
    }


    @Override
    public void update(int direction,float speed){

        if(isReplacement){

            timer++;

            x += Math.sin(timer * 0.3) * 3;

            moveToCell();

        }else{

            timer++;

            //x += direction * speed;
            x += Math.sin(timer * 0.2) * 2;
        }
    }

    @Override
    protected Color getColor() {
        return Color.GREEN;
    }
}
