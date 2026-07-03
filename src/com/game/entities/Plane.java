package com.game.entities;

public class Plane {
    public int x,y;
    public int width = 50;
    public int height = 50;
    public int speed = 5;

    public Plane(){
        this.x = 600/2 - width/2;
        this.y = 800-100;
    }

    public void moveLeft(){
        x -= speed;
    }

    public void moveRight(){
        x += speed;
    }
    public void moveUp() {
        y -= speed;
    }

    public void moveDown() {
        y += speed;
    }


    public void keepInBounds(int screenWidth,int screenHeight) {
        if (x < 0) x = 0;

        if (x > screenWidth - width) x = screenWidth - width;

        if (y < 0) y = 0;

        if (y > screenHeight - height) y = screenHeight - height;
    }
}
