package com.game.entities;

import java.awt.*;

public abstract class Enemy {

    public int x,y;
    public int width;
    public int height;
    public int hp;
    public int row, col;


    public int pointValue;
    public boolean isReplacement;
    public int eggInterval;
    private long lastEggDropTime;


    public Enemy(int hp,int levelEggInterval){
        width = 30;
        height = 30;
        this.hp = hp;
        this.eggInterval = levelEggInterval;
        this.lastEggDropTime = System.currentTimeMillis();

    }

    public void takeDamage(){
    hp--;
}

    public boolean isDead(){
    return (hp <= 0);
}

    public abstract void update(int direction, int gridSpeed ,int gx ,int gy,int screenWidth );


    public void draw(Graphics g){

        g.setColor(getColor());
        g.fillRect(x,y,width,height);

    }

    protected abstract Color getColor();

    public Rectangle getBounds(){
    return new Rectangle(x,y,width,height);
}


     public Egg dropEgg() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastEggDropTime >= this.eggInterval) { // استفاده از متغیر به جای عدد ثابت
            lastEggDropTime = currentTime;
            return new Egg(this.x + this.width/2, this.y + this.height);
        }
        return null;
    }
}
