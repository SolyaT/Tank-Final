package com.example.javatank.core.tank;

public class Bullet {
    private int x;
    private int y;
    private int direction;
    private int speed;
    private int size;

    public Bullet(int x, int y, int direction, int speed) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.speed = speed;
        this.size = 10;
    }

    private void move() {

    }
}
