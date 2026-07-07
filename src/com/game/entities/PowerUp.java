package com.game.entities;

import java.awt.*;

public class PowerUp {
    public float x, y;

    public int width;
    public int height;

    private float speed;
    public int type;

    public static final int ADD_FIRE = 0;
    public static final int RAPID_FIRE = 1;
    public static final int EXTRA_LIFE = 2;
    public static final int SHIELD = 3;
    public static final int FREEZE_BOMB = 4;

    public PowerUp(float x, float y,int type) {
        this.x = x;
        this.y = y;
        this.width = 20;
        this.height = 20;
        this.speed = 2;
        this.type = type;
    }

    public void update() {
        y += speed;
    }

    public void draw(Graphics g) {

        switch (type) {

            case ADD_FIRE:
                g.setColor(Color.ORANGE);
                break;

            case RAPID_FIRE:
                g.setColor(Color.YELLOW);
                break;

            case EXTRA_LIFE:
                g.setColor(Color.GREEN);
                break;

            case SHIELD:
                g.setColor(Color.CYAN);
                break;

            case FREEZE_BOMB:
                g.setColor(Color.BLUE);
                break;
        }

        g.fillRect((int)x,(int)y,width,height);
    }

    public Rectangle getBounds() {
        return new Rectangle((int)x,(int)y,width,height);
    }
}
