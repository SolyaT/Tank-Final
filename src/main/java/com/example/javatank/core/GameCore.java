package com.example.javatank.core;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameCore extends JPanel implements KeyListener {
    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;
    private static final int CELL_SIZE = 50;

    private Tank tank;
    private Tank secondTank;
    private final GameType gameType;

    private Map map;

    public GameCore(GameType gameType) {
        this.gameType = gameType;

        int mapWidth = SCREEN_WIDTH / CELL_SIZE;
        int mapHeight = SCREEN_HEIGHT / CELL_SIZE;

        map = new Map(mapWidth, mapHeight);

        initTank();
        setFocusable(true);
        addKeyListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawTanks(g);
        drawMap(g);
    }

    private void drawMap(Graphics graphics) {
        for (int i = 0; i < map.getWidth(); i++) {
            for (int j = 0; j < map.getHeight(); j++) {
                if (map.isWall(i, j)) {
                    graphics.setColor(Color.GRAY);
                    graphics.fillRect(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
            }
        }
    }

    private void drawTanks(Graphics g) {
        if (gameType == GameType.SINGLE_PLAY) {
            tank.draw(g);
        }

        if (gameType == GameType.MULTI_PLAY) {
            tank.draw(g);
            secondTank.draw(g);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W) {
            tank.moveUp();
        } else if (key == KeyEvent.VK_S) {
            tank.moveDown();
        } else if (key == KeyEvent.VK_A) {
            tank.moveLeft();
        } else if (key == KeyEvent.VK_D) {
            tank.moveRight();
        } else if (key == KeyEvent.VK_UP) {
            secondTank.moveUp();
        } else if (key == KeyEvent.VK_DOWN) {
            secondTank.moveDown();
        } else if (key == KeyEvent.VK_LEFT) {
            secondTank.moveLeft();
        } else if (key == KeyEvent.VK_RIGHT) {
            secondTank.moveRight();
        }

        if (key == KeyEvent.VK_SPACE){
            tank.shoot();
        }

        if (key == KeyEvent.VK_ENTER){
            secondTank.shoot();
        }

        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    private void initTank() {
        int tankX = (SCREEN_WIDTH / CELL_SIZE) / 2;
        int tankY = (SCREEN_HEIGHT / CELL_SIZE) / 2;

        if (gameType == GameType.MULTI_PLAY) {
            tank = new Tank(tankX * CELL_SIZE, tankY * CELL_SIZE);
            secondTank = new Tank(tankX * CELL_SIZE, tankY * CELL_SIZE);
        }

        if (gameType == GameType.SINGLE_PLAY) {
            tank = new Tank(tankX * CELL_SIZE, tankY * CELL_SIZE);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Tank Game");
        GameCore game = new GameCore(GameType.MULTI_PLAY);
        frame.add(game);
        frame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
