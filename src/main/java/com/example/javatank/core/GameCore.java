package com.example.javatank.core;

import com.example.javatank.core.bot.TankBot;
import com.example.javatank.core.tank.Tank;
import com.example.javatank.core.tank.TankType;
import com.example.tank_finalproject.DBUtils;
import com.example.tank_finalproject.HelloApplication;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameCore extends JPanel implements KeyListener {
    JFrame frame = new JFrame("Tank Game");
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

    public GameCore( GameType gameType, String firstTank) {
        this.gameType = gameType;
        int mapWidth = SCREEN_WIDTH / CELL_SIZE;
        int mapHeight = SCREEN_HEIGHT / CELL_SIZE;

        map = new Map(mapWidth, mapHeight);

        initSingleTank(firstTank);
        collectToArray();

        setFocusable(true);
        addKeyListener(this);

        gameLoop();
    }

    public GameCore(GameType gameType, String firstTankName, String secondTankName) {
        this.gameType = gameType;
        int mapWidth = SCREEN_WIDTH / CELL_SIZE;
        int mapHeight = SCREEN_HEIGHT / CELL_SIZE;

        map = new Map(mapWidth, mapHeight);

        initMultiTank(firstTankName, secondTankName);
        collectToArray();

        setFocusable(true);
        addKeyListener(this);

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

        if (key == KeyEvent.VK_F) {
            tank.shoot();
        }

        if (secondTank.getTankType() != TankType.BOT && key == KeyEvent.VK_SPACE) {
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
                } else {

                    Tank winner = null;
                    for (Tank aliveTank : tanks) {
                        if (aliveTank.isAlive) {
                            winner = aliveTank;
                            break;
                        }
                    }

                    if (winner == null) {
                        return;
                    }

                    System.out.println("===============================");
                    System.out.println("Name: " + winner.name);
                    System.out.println("Hits: " + tank.getCountOfHit());
                    System.out.println("Kills: " + tank.getCountOfKill());
                    System.out.println("===============================");


                    // Database connection parameters
                    String url = "jdbc:mysql://localhost:3306/tank-final";
                    String username = "root";
                    String password = "sae030721";

                    // Save hits and kills data to the database
                    try (Connection connection = DriverManager.getConnection(url, username, password)) {
                        String query = "INSERT INTO tanks (player_name, kill_count, damage_count) VALUES (?, ?, ?)";
                        PreparedStatement statement = connection.prepareStatement(query);

                        statement.setString(1, winner.name);
                        statement.setInt(2, tank.getCountOfKill());
                        statement.setInt(3, tank.getCountOfHit());

                        statement.executeUpdate();
                        System.out.println("Data saved to the database.");
                    } catch (SQLException e) {
                        System.out.println("Error saving data to the database: " + e.getMessage());
                    }

                    frame.setVisible(false);

                    try (Connection connection = DriverManager.getConnection(url, username, password)) {
                        String query = "SELECT player_name, kill_count, damage_count FROM tanks WHERE player_name = ?";
                        PreparedStatement statement = connection.prepareStatement(query);
                        statement.setString(1, winner.name);

                        ResultSet resultSet = statement.executeQuery();

                        if (resultSet.next()) {
                            String playerName = resultSet.getString("player_name");
                            int killCount = resultSet.getInt("kill_count");
                            int damageCount = resultSet.getInt("damage_count");


                            showMessageAndRestart("WINNER: " + playerName + "\n" +
                                    "KILL: " + killCount + "\n" +
                                    "DAMAGES: " + damageCount);
                        } else {
                            System.out.println("Tank not found in the database.");
                        }
                    } catch (SQLException e) {
                        System.out.println("Error retrieving data from the database: " + e.getMessage());
                    }
                }
                break;
            }
        }
    }

    private void showMessageAndRestart(String message) {
        int result = JOptionPane.showOptionDialog(
                null,
                message,
                "Message",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new Object[]{"OK"},
                "OK");

        if (result == JOptionPane.OK_OPTION) {
            restartApplication();
        }
    }

    private void restartApplication() {
        final String javaBin = System.getProperty("java.home") + "/bin/java";
        final String currentJar = new java.io.File(HelloApplication.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getName();

        if (!currentJar.endsWith(".jar")) {
            return; // Not running from JAR
        }

        try {
            final ProcessBuilder builder = new ProcessBuilder(javaBin, "-jar", currentJar);
            builder.start();
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reset() {
        for (Tank tank : tanks) {
            tank.reset();
        }
    }

    private void initMultiTank(String name, String secondName) {
        int tankX = (SCREEN_WIDTH / CELL_SIZE) / 2;
        int tankY = (SCREEN_HEIGHT / CELL_SIZE) / 2;

        if (gameType == GameType.MULTI_PLAY) {
            tank = new Tank(name, tankX * CELL_SIZE, (tankY + 5) * CELL_SIZE, TankType.RED, map, this);
            secondTank = new Tank(secondName, tankX * CELL_SIZE, (tankY - 5) * CELL_SIZE, TankType.BLUE, map, this);
        }
    }

    private void initSingleTank(String name) {
        int tankX = (SCREEN_WIDTH / CELL_SIZE) / 2;
        int tankY = (SCREEN_HEIGHT / CELL_SIZE) / 2;

        if (gameType == GameType.SINGLE_PLAY) {
            tank = new Tank(name, tankX * CELL_SIZE, (tankY + 5) * CELL_SIZE, TankType.RED, map, this);
            secondTank = new Tank("BOT", tankX * CELL_SIZE, (tankY - 5) * CELL_SIZE, TankType.BOT, map, this);
            botTank = new TankBot(secondTank, this);
        }
    }

    private void collectToArray() {
        Tank[] tempTank = {tank, secondTank};
        tanks = new ArrayList<>();
        tanks.addAll(Arrays.asList(tempTank));
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

    public void startGame() {
        frame.add(this);
        frame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
