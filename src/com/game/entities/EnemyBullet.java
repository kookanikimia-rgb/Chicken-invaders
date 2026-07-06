package com.game.entities;

import java.awt.*;

public class EnemyBullet {

    public int x,y;
    public int width = 12;
    public int height = 4;

    private int dx;

    public EnemyBullet(int x,int y,int playerX){

        this.x = x;
        this.y = y;

        if(playerX > x)
            dx = -6;
        else
            dx = 6;
    }

    public void move(){
        x -=dx;
    }

    public void draw(Graphics g){
        g.setColor(Color.ORANGE);
        g.fillRect(x,y,width,height);
    }

    public Rectangle getBounds(){
        return new Rectangle(x,y,width,height);
    }
}