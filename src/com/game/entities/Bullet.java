package com.game.entities;

import java.awt.*;

public class Bullet {
    public int x,y;
    public int width;
    public int height;
    public int speed ;

    public Bullet(int startX,int startY){
        this.x = startX;
        this.y = startY;
        this.width = 5;
        this.height = 15;
        this.speed = 10;
    }

    public void move(){
        y -= speed;
    }

    public void draw(Graphics g){
        g.setColor(new Color(50,50,120));
        g.fillRect(x,y,width,height);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
