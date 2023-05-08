package com.example.javatank;

import com.example.javatank.model.Tank;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameCore extends JPanel implements KeyListener {
    private Tank tank;
    private Tank secondTank;

    public GameCore() {
        tank = new Tank(100, 100);
        secondTank = new Tank(100, 250);

        setFocusable(true);
        addKeyListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        tank.draw(g);
        secondTank.draw(g);
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
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Tank Game");
        GameCore game = new GameCore();
        frame.add(game);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
