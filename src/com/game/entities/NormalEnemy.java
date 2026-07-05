package com.game.entities;

import java.awt.*;

public class NormalEnemy extends Enemy{

    public NormalEnemy(int hp){
        super(hp);
        eggInterval = 3000;
        pointValue = 10;
    }

    @Override
    public void update(int dir,int speed,int gx,int gy){
        if(this.isReplacement) {
            int targetX = gx + this.col * 60;
            int targetY = gy + this.col * 60;

            if (this.x < targetX) this.x += 2;
            if (this.x > targetX) this.x -= 2;
            if (this.y < targetY) this.y += 2;
            if (Math.abs(this.x - targetX) < 5 && Math.abs(this.y - targetY) < 5) {
                this.x = targetX;
                this.y = targetY;
                this.isReplacement = false;
            }
        }else {
                x += dir * speed;
            }
        }

    @Override
    protected Color getColor(){
        return Color.red;
    }
}

