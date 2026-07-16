package com.game.entities;

import javax.swing.*;
import java.awt.*;

public class FastEnemy extends Enemy{

    public FastEnemy(int hp, int levelEggInterval){
        super(hp,levelEggInterval);
        pointValue = 15;

        try {
            this.image = new ImageIcon(getClass().getResource("/Assets/images/chicken/fast_chicken.png")).getImage();
        } catch (Exception e) {
            System.out.println("FastEnemy image not found!");
        }
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
