package com.game.entities;

import java.awt.*;

public abstract class Enemy {

    public float x,y;
    public int width;
    public int height;

    public int hp;
    public int row, col;

    public float targetX;
    public float targetY;

    public int pointValue;

    public boolean isReplacement;

    public Image image;

    public int eggInterval;
    private long lastEggDropTime;


    public Enemy(int hp,int levelEggInterval){
        width = 40;
        height = 40;

        this.hp = hp;

        this.eggInterval = levelEggInterval;
        lastEggDropTime = 0;
    }

    public abstract void update(int direction, float gridSpeed);

    public void takeDamage(){hp--;}

    public boolean isDead(){
    return (hp <= 0);
}

    protected void moveToCell() {

        float speed = getReplacementSpeed();
        // محور X
        if (Math.abs(targetX - x) <= speed) {
            x = targetX;
        } else if (x < targetX) {
            x += speed;
        } else {
            x -= speed;
        }

        // محور Y
        if (Math.abs(targetY - y) <= speed) {
            y = targetY;
        } else if (y < targetY) {
            y += speed;
        } else {
            y -= speed;
        }
        if(isReplacement){
            if (x <= 0)
                x = 0;

            if (x + width >= 580)
                x = 580 - width;
        }

        if (Math.abs(x - targetX) < 0.1f &&
                Math.abs(y - targetY) < 0.1f) {

            x = targetX;
            y = targetY;
            isReplacement = false;
        }
    }

    protected float getReplacementSpeed() {
        return 2f;
    }

    public Egg dropEgg(long gameTime) {

        if (lastEggDropTime == 0)
            lastEggDropTime = gameTime;

        if (gameTime - lastEggDropTime >= this.eggInterval) {
            lastEggDropTime = gameTime;
            return new Egg((int)this.x + this.width/2, (int)this.y + this.height);
        }
        return null;
    }


    public void draw(Graphics g) {
        if (image != null) {
            g.drawImage(image, (int)x, (int)y, width, height, null);
        } else {
            // اگر عکس لود نشده باشد، همان حالت رنگی را می‌کشد تا بازی خراب نشود
            g.setColor(getColor());
            g.fillRect((int)x, (int)y, width, height);
        }
    }

    protected abstract Color getColor();

    public Rectangle getBounds(){
    return new Rectangle((int)x,(int)y,width,height);
}

}
