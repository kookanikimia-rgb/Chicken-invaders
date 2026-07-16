package com.game.entities;

import javax.swing.*;
import java.awt.*;

public class PowerUp {
    public float x, y;

    public int width;
    public int height;

    private float speed;
    public int type;

    public Image image;

    public static final int ADD_FIRE = 0;
    public static final int RAPID_FIRE = 1;
    public static final int EXTRA_LIFE = 2;
    public static final int SHIELD = 3;
    public static final int FREEZE_BOMB = 4;

    public PowerUp(float x, float y,int type) {
        this.x = x;
        this.y = y;
        this.width = 40;
        this.height = 40;
        this.speed = 2;
        this.type = type;

        loadPowerUpImage();
    }

    private void loadPowerUpImage() {
        String path = "";
        switch (type) {
            case ADD_FIRE:    path = "/Assets/images/powerup1/add_shot.png"; break;
            case RAPID_FIRE:  path = "/Assets/images/powerup1/fast_shot.png"; break;
            case EXTRA_LIFE:  path = "/Assets/images/powerup1/heal.png"; break;
            case SHIELD:      path = "/Assets/images/powerup1/sheild.png"; break;
            case FREEZE_BOMB: path = "/Assets/images/powerup1/freeze.png"; break;
        }

        try {
            this.image = new ImageIcon(getClass().getResource(path)).getImage();
        } catch (Exception e) {
            System.out.println("PowerUp image not found: " + path);
        }
    }

    public void update() {
        y += speed;
    }

    public void draw(Graphics g) {
        // ۳. رسم عکس به جای مربع رنگی
        if (image != null) {
            g.drawImage(image, (int)x, (int)y, width, height, null);
        } else {
            // اگر عکس لود نشد، برای اینکه بازیکن بفهمد چه پاورآپی است، رنگ قدیمی را می‌کشیم
            switch (type) {
                case ADD_FIRE: g.setColor(Color.ORANGE); break;
                case RAPID_FIRE: g.setColor(Color.YELLOW); break;
                case EXTRA_LIFE: g.setColor(Color.GREEN); break;
                case SHIELD: g.setColor(Color.CYAN); break;
                case FREEZE_BOMB: g.setColor(Color.BLUE); break;
            }
            g.fillRect((int)x, (int)y, width, height);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle((int)x,(int)y,width,height);
    }
}
