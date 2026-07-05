package com.game.entities;

import java.awt.*;

public class ZigzagEnemy extends Enemy{
    private int timer = 0;


    public ZigzagEnemy(int hp, int levelEggInterval){
        super(hp,levelEggInterval);
        pointValue = 20;
    }


    @Override

    public void update(int dir,int speed,int gx,int gy,int screenWidth ) {

        if (this.isReplacement) {
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
        } else {
            timer++;
            int offset = (int) (Math.sin(timer * 0.1) * 30);
            this.x = gx + (this.col * 60) + offset;
            this.y = gy + (this.row * 60);

            if (this.x < 0) {
                this.x = 0;
            }

            if (this.x + this.width > 595) {
                this.x = 595 - this.width;
            }
        }

    }
    @Override
    protected Color getColor() {
        return Color.GREEN;
    }
}
