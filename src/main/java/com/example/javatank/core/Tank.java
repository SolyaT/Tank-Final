package com.example.javatank.core;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Timer;

public class Tank {
    private int x;
    private int y;
    private int speed;
    private Image tankImage;
    private boolean isShooting;

    public Tank(int x, int y) {
        this.x = x;
        this.y = y;
        speed = 5;

        // loadTankImage();
    }

    private void loadTankImage() {
        try {
            tankImage = ImageIO.read(new File("tank_sprite.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics graphics) {
        if (tankImage != null) {
            graphics.drawImage(tankImage, x, y, null);
        } else {
            graphics.setColor(Color.RED);
            graphics.fillRect(x, y, 50, 50);
        }

        if (isShooting) {
            graphics.setColor(Color.YELLOW);
            graphics.fillOval(x + 20, y - 10, 10, 10);
        }
    }

    public void shoot() {
        isShooting = true;

        new Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        isShooting = false;
                    }
                },
                500
        );
    }

    public void moveUp() {
        y += speed;
    }

    public void moveDown() {
        y -= speed;
    }

    public void moveRight() {
        x += speed;
    }

    public void moveLeft() {
        x -= speed;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
