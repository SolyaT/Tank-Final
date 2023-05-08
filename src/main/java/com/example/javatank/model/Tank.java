package com.example.javatank.model;

import java.awt.*;

public class Tank {
    private int x;
    private int y;
    private int speed;

    public Tank(int x, int y) {
        this.x = x;
        this.y = y;

        speed = 5;
    }

    public void draw(Graphics graphics) {
        graphics.setColor(Color.RED);
        graphics.fillRect(x, y, 50, 50);
    }

    public void moveUp(){
        y += speed;
    }

    public void moveDown(){
        y -= speed;
    }

    public void moveRight(){
        x += speed;
    }

    public void moveLeft(){
        x -= speed;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
