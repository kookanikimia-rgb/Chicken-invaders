package com.game.entities;

import java.awt.*;

public abstract class Enemy {

public int x,y;
public int width;
public int height;
public int hp;
public int row, col;
public boolean isReplacement;

public Enemy(int hp){
    width = 40;
    height = 40;
    this.hp = hp;
}

public void takeDamage(){
    hp--;
}

public boolean isDead(){
    return (hp <= 0);
}
public abstract void update(int direction, int gridSpeed ,int gx ,int gy);

public void draw(Graphics g){
    g.setColor(getColor());
    g.fillRect(x,y,width,height);
}

protected abstract Color getColor();

public Rectangle getBounds(){
    return new Rectangle(x,y,width,height);
}
}
