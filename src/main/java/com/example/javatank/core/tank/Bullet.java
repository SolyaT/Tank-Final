package com.example.javatank.core.tank;

import com.example.javatank.core.GameCore;
import com.example.javatank.core.Map;

import java.awt.*;

public class Bullet {
    private int x;
    private int y;
    private int direction;
    private int speed;

    private Map currentMap;
    private int bulletSize = 5;

    public Bullet(int x, int y, int direction, int speed, Map map) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.speed = speed;

        currentMap = map;
    }

    public void updatePosition() {
        switch (direction) {
            case 0 -> y -= speed;
            case 1 -> y += speed;
            case 2 -> x -= speed;
            case 3 -> x += speed;
        }
    }

    public void draw(Graphics graphics) {
        graphics.setColor(Color.yellow);
        graphics.fillRect(x, y, bulletSize, bulletSize);
    }

    public boolean isOutOfBounds() {
        int gameAreaWidth = currentMap.getWidth();
        int gameAreaHeight = currentMap.getHeight();

        return x < 0 || y < 0 || x >= gameAreaWidth || y >= gameAreaHeight;
    }

    public boolean hasCollided() {
        int cellX = x / GameCore.CELL_SIZE;
        int cellY = y / GameCore.CELL_SIZE;

        return currentMap.isWall(cellX, cellY);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
