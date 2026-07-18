package com.game.entities;

public class PlaneInfo {

    public String name;
    public int price;
    public int speed;
    public int shootDelay;
    public int hp;
    public boolean doubleBossDamage;

    public PlaneInfo(String name,
                     int price,
                     int speed,
                     int shootDelay,
                     int hp,
                     boolean doubleBossDamage) {

        this.name = name;
        this.price = price;
        this.speed = speed;
        this.shootDelay = shootDelay;
        this.hp = hp;
        this.doubleBossDamage = doubleBossDamage;
    }
}
