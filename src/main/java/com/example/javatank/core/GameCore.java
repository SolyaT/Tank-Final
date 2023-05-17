package com.example.javatank.core;

import com.example.javatank.core.bot.TankBot;
import com.example.javatank.core.tank.Tank;
import com.example.javatank.core.tank.TankType;
import javafx.application.Application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameCore extends JPanel implements KeyListener {
    public static final int SCREEN_WIDTH = 900;
    public static final int SCREEN_HEIGHT = 600;
    public static final int CELL_SIZE = 50;
    public static final int COUNT_ROUND = 3;

    private Tank tank;
    private Tank secondTank;
    private TankBot botTank;

    private ArrayList<Tank> tanks;

    private final GameType gameType;

    private Map map;
    private int currentRound = 1;

    private boolean tankMovingUp = false;
    private boolean tankMovingDown = false;
    private boolean tankMovingLeft = false;
    private boolean tankMovingRight = false;

    private boolean secondTankMovingUp = false;
    private boolean secondTankMovingDown = false;
    private boolean secondTankMovingLeft = false;
    private boolean secondTankMovingRight = false;


    public GameCore(GameType gameType) {
        this.gameType = gameType;

        int mapWidth = SCREEN_WIDTH / CELL_SIZE;
        int mapHeight = SCREEN_HEIGHT / CELL_SIZE;

        map = new Map(mapWidth, mapHeight);

        initTank();
        setFocusable(true);
        addKeyListener(this);

        Tank[] tempTank = {tank, secondTank};
        tanks = new ArrayList<>();

        for (Tank value : tempTank) {
            if (value != null) {
                tanks.add(value);
            }
        }

        gameLoop();
    }

    public ArrayList<Tank> getTanks() {
        return tanks;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawTanks(g);
        map.drawMap(g);

    }

    private void drawTanks(Graphics g) {
        if (gameType == GameType.SINGLE_PLAY) {
            if (tank.isAlive)
                tank.draw(g);

            if (secondTank.isAlive)
                secondTank.draw(g);

        }

        if (gameType == GameType.MULTI_PLAY) {
            if (tank.isAlive)
                tank.draw(g);

            if (secondTank.isAlive)
                secondTank.draw(g);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W) {
            tankMovingUp = true;
            tank.moveUp();
        } else if (key == KeyEvent.VK_S) {
            tankMovingDown = true;
            tank.moveDown();
        } else if (key == KeyEvent.VK_A) {
            tankMovingLeft = true;
            tank.moveLeft();
        } else if (key == KeyEvent.VK_D) {
            tankMovingRight = true;
            tank.moveRight();
        }

        if (secondTank.getTankType() != TankType.BOT) {
            if (key == KeyEvent.VK_UP) {
                secondTankMovingUp = true;
                secondTank.moveUp();
            } else if (key == KeyEvent.VK_DOWN) {
                secondTankMovingDown = true;
                secondTank.moveDown();
            } else if (key == KeyEvent.VK_LEFT) {
                secondTankMovingLeft = true;
                secondTank.moveLeft();
            } else if (key == KeyEvent.VK_RIGHT) {
                secondTankMovingRight = true;
                secondTank.moveRight();
            }
        }

        if (key == KeyEvent.VK_SPACE) {
            tank.shoot();
        }

        if (secondTank.getTankType() != TankType.BOT && key == KeyEvent.VK_F) {
            secondTank.shoot();
        }

        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W) {
            tankMovingUp = false;
        } else if (key == KeyEvent.VK_S) {
            tankMovingDown = false;
        } else if (key == KeyEvent.VK_A) {
            tankMovingLeft = false;
        } else if (key == KeyEvent.VK_D) {
            tankMovingRight = false;
        }

        if (secondTank.getTankType() != TankType.BOT) {
            if (key == KeyEvent.VK_UP) {
                secondTankMovingUp = false;
            } else if (key == KeyEvent.VK_DOWN) {
                secondTankMovingDown = false;
            } else if (key == KeyEvent.VK_LEFT) {
                secondTankMovingLeft = false;
            } else if (key == KeyEvent.VK_RIGHT) {
                secondTankMovingRight = false;
            }
        }
    }

    public void updateMovement() {
        if (tankMovingUp) {
            tank.moveUp();
        } else if (tankMovingDown) {
            tank.moveDown();
        } else if (tankMovingLeft) {
            tank.moveLeft();
        } else if (tankMovingRight) {
            tank.moveRight();
        }

        if (secondTank.getTankType() != TankType.BOT) {
            if (secondTankMovingUp) {
                secondTank.moveUp();
            } else if (secondTankMovingDown) {
                secondTank.moveDown();
            } else if (secondTankMovingLeft) {
                secondTank.moveLeft();
            } else if (secondTankMovingRight) {
                secondTank.moveRight();
            }
        }
    }

    public void updateGameState() {
        for (Tank tank : tanks) {
            if (!tank.isAlive) {
                if (currentRound < COUNT_ROUND) {
                ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
                executorService.schedule(() -> {
                    reset();
                    currentRound++;
                }, 3, TimeUnit.SECONDS);
                executorService.shutdown();
                break;
            } else {
                System.out.println("===============================");
                System.out.println("Hits: " + tank.getCountOfHit());
                System.out.println("Kills: " + tank.getCountOfKill());
                System.out.println("===============================");
            }
            }
        }
    }

    public void reset() {
        for (Tank tank : tanks) {
            tank.reset();
        }
    }

    private void initTank() {
        int tankX = (SCREEN_WIDTH / CELL_SIZE) / 2;
        int tankY = (SCREEN_HEIGHT / CELL_SIZE) / 2;

        if (gameType == GameType.MULTI_PLAY) {
            tank = new Tank(tankX * CELL_SIZE, (tankY + 5) * CELL_SIZE, TankType.RED, map, this);
            secondTank = new Tank(tankX * CELL_SIZE, (tankY - 5) * CELL_SIZE, TankType.BLUE, map, this);
        }

        if (gameType == GameType.SINGLE_PLAY) {
            tank = new Tank(tankX * CELL_SIZE, (tankY + 5) * CELL_SIZE, TankType.RED, map, this);
            secondTank = new Tank(tankX * CELL_SIZE, (tankY - 5) * CELL_SIZE, TankType.BOT, map, this);
            botTank = new TankBot(secondTank, this);
        }
    }

    public void gameLoop() {
        Thread gameThread = new Thread(() -> {
            while (true) {
                // Update movement and game logic
                updateMovement();

                // Render the game
                // ...

                // Add a delay to control the game's frame rate
                try {
                    Thread.sleep(110); // Adjust the delay duration as needed
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        gameThread.start();
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
