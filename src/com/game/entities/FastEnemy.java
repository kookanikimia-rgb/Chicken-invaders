package com.game.entities;

import java.awt.*;

public class FastEnemy extends Enemy{

    public FastEnemy(int hp, int levelEggInterval){
        super(hp,levelEggInterval);
        pointValue = 15;
    }

    @Override
    public void update(int dir,int speed,int gx,int gy,int screenWidth ){
        if(this.isReplacement) {
            int targetX = gx + this.col * 60;
            int targetY = gy + this.row * 60;

            if (this.x < targetX) this.x += 2;
            if (this.x > targetX) this.x -= 2;
            if (this.y < targetY) this.y += 2;
            if (this.y > targetY) this.y -= 2;
            if (Math.abs(this.x - targetX) < 5 && Math.abs(this.y - targetY) < 5) {
                this.x = targetX;
                this.y = targetY;
                this.isReplacement = false;
            }
        }else {
            x += dir * speed *2;
        }
    }

    @Override
    protected Color getColor() {
        return Color.yellow;
    }
}
