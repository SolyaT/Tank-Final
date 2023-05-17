package com.example.javatank.core.bot;

import com.example.javatank.core.GameCore;
import com.example.javatank.core.tank.Tank;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class TankBot {
    private final Tank tank;
    private final GameCore gameCore;

    public TankBot(Tank tank, GameCore gameCore) {
        this.tank = tank;
        this.gameCore = gameCore;

        startBotAction();
    }

    private void startBotAction() {
        Timer actionTimer = new Timer();
        actionTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (tank.isAlive)
                    performAction();
            }
        }, 0, 400);
    }

    private void performAction() {
        Random random = new Random();
        int action = random.nextInt(4);

        switch (action) {
            case 1 -> moveUp();
            case 0 -> moveDown();
            case 2 -> moveLeft();
            case 3 -> moveRight();
            default -> {
            }
        }

        if (random.nextInt(100) < 70) {
            tank.shoot();
        }
    }

    private void moveUp() {
        tank.moveUp();
    }

    private void moveDown() {
        tank.moveDown();
    }

    private void moveLeft() {
        tank.moveLeft();
    }

    private void moveRight() {
        tank.moveRight();
    }
}
