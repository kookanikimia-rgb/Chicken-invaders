package com.game.entities;

import javax.swing.*;
import java.awt.*;

public class NormalEnemy extends Enemy{

    public NormalEnemy(int hp,int levelEggInterval) {
        super(hp, levelEggInterval);
        pointValue = 10;

        try {
            this.image = new ImageIcon(getClass().getResource("/Assets/images/chicken/normal_chicken.png")).getImage();
        } catch (Exception e) {
            System.out.println("NormalEnemy image not found!");
        }
    }

    @Override
    public void update(int direction,float speed){

        if(isReplacement){

            moveToCell();

        }
    }

    @Override
    protected Color getColor(){
        return Color.red;
    }
}

