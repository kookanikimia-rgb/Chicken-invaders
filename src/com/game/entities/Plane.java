package com.game.entities;

import javax.swing.*;
import java.awt.*;

public class Plane {
    public int x,y;
    public int width;
    public int height;
    public int hp;
    public int speed;

    public Image image;

    public PlaneInfo planeInfo;
    public boolean dead = false;

    public Plane(PlaneInfo planeInfo){

        this.planeInfo = planeInfo;

        this.speed = planeInfo.speed;
        this.hp = planeInfo.hp;

        this.width = 80;
        this.height = 80;
        this.x = 600/2 - width/2;
        this.y = 800-100;


        String imagePath;

        switch (planeInfo.name){

            case "FAST":
                imagePath = "/Assets/images/airplan/5.png";
                break;

            case "HEAVY":
                imagePath = "/Assets/images/airplan/3.png";
                break;

            case "SNIPER":
                imagePath = "/Assets/images/airplan/6.png";
                break;

            default:
                imagePath = "/Assets/images/airplan/2.png";
                break;
        }

        try{
            image = new ImageIcon(
                    getClass().getResource(imagePath)
            ).getImage();
        }catch(Exception e){
            System.out.println("Plane image not found!");
        }

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

    public void takeDamage(){
        hp--;
        if (hp < 0){
            hp = 0;
            dead = true;
        }
    }

    public boolean isDead() {
        return dead;
    }

    public void keepInBounds(int screenWidth,int screenHeight) {
        if (x < 0) x = 0;

        if (x > screenWidth - width) x = screenWidth - width;

        if (y < 0) y = 0;

        if (y > screenHeight - height) y = screenHeight - height;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
