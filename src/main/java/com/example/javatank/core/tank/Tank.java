package com.example.javatank.core.tank;

import com.example.javatank.core.GameCore;
import com.example.javatank.core.Map;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.Timer;

public class Tank {
    private int x;
    private int y;

    private int speed;
    private int direction;

    private int currentHealth;
    private int maxHealth;

    private BufferedImage tankImage;
    private boolean isShooting;

    private final int tankSize = 50;
    private final Map currentMap;
    private final TankType tankType;

    public Tank(int x, int y, TankType tankType, Map map) {
        this.x = x;
        this.y = y;
        this.tankType = tankType;

        speed = 5;
        currentMap = map;
        loadTankImage();

        maxHealth = 100;
        currentHealth = maxHealth;
    }

    private void loadTankImage() {
        String imageName = switch (tankType) {
            case RED -> "/tank_red.png";
            case BLUE -> "/tank_blue.png";
        };

        try {
            tankImage = ImageIO.read(Objects.requireNonNull(getClass().getResource(imageName)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics graphics) {
        if (tankImage != null) {
            Graphics2D graphics2D = (Graphics2D) graphics;

            AffineTransform previousTransform = graphics2D.getTransform();

            graphics2D.translate(x, y);
            double rotationAngle = switch (direction) {
                case 1 -> Math.PI;
                case 2 -> Math.PI / 2;
                case 3 -> 3 * Math.PI / 2;
                default -> 0;
            };

            graphics2D.rotate(rotationAngle);

            graphics2D.drawImage(tankImage, -tankSize / 2, -tankSize / 2, tankSize, tankSize, null);
            graphics2D.setTransform(previousTransform);

            drawHealthBar(graphics2D);

        } else {
            graphics.setColor(Color.RED);
            graphics.fillRect(x, y, 50, 50);
        }

        if (isShooting) {
            graphics.setColor(Color.YELLOW);
            graphics.fillOval(x + 20, y - 10, 10, 10);
        }

    }

    private void drawHealthBar(Graphics2D graphics2D) {
        int barWidth = 50;
        int barHeight = 5;
        int barX = x - barWidth / 2;
        int barY = y + 30;

        double fillPercentage = (double) currentHealth / maxHealth;
        int fillWidth = (int) (fillPercentage * barWidth);

        graphics2D.setColor(Color.GRAY);
        graphics2D.fillRect(barX, barY, barWidth, barHeight);

        graphics2D.setColor(Color.GREEN);
        graphics2D.fillRect(barX, barY, fillWidth, barHeight);
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

    public void applyDamage(int damage){
        if (currentHealth <= 0){
            //Todo: Destroy and loose
        } else {
            currentHealth -= damage;
        }
    }

    private boolean checkWallCollision(int x, int y) {
        int cellX = x / GameCore.CELL_SIZE;
        int cellY = y / GameCore.CELL_SIZE;

        if (cellX >= 0 && cellX < currentMap.getWidth() && cellY >= 0 && cellY < currentMap.getHeight()) {
            if (currentMap.isWall(cellX, cellY)) {
                int wallX = cellX * GameCore.CELL_SIZE;
                int wallY = cellY * GameCore.CELL_SIZE;
                Rectangle tankBounds = new Rectangle(x, y, tankSize, tankSize);
                Rectangle wallBounds = new Rectangle(wallX, wallY, GameCore.CELL_SIZE, GameCore.CELL_SIZE);

                return !tankBounds.intersects(wallBounds);
            }
        }

        return true;
    }

    public void moveUp() {
        if (checkWallCollision(x, y - speed)) {
            y -= speed;
            direction = 1;
        }
    }

    public void moveDown() {
        if (checkWallCollision(x, y + speed)) {
            y += speed;
            direction = 0;
        }
    }

    public void moveRight() {
        if (checkWallCollision(x + speed, y)) {
            x += speed;
            direction = 3;
        }
    }

    public void moveLeft() {
        if (checkWallCollision(x - speed, y)) {
            x -= speed;
            direction = 2;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
