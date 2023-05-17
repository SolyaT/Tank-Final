package com.example.javatank.core.tank;

import com.example.javatank.core.GameCore;
import com.example.javatank.core.Map;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Tank {
    private int x;
    private int y;

    private int speed;
    private int direction;

    public boolean isAlive = true;

    private int currentHealth;
    private int maxHealth;

    private BufferedImage tankImage;

    private GameCore gameCore;

    private final int tankSize = 50;
    private Map currentMap;
    private final TankType tankType;

    private int bulletSpeed = 100;
    private boolean canShoot = true;
    private int shootCooldown = 1;
    private int damage = 20;
    private int countOfKill;
    private int countOfHit;

    private Clip hitSound;
    private Clip shootSound;
    private Clip explosionSound;

    public Tank(int x, int y, TankType tankType, Map map, GameCore gameCore) {
        maxHealth = 100;

        this.x = x;
        this.y = y;
        this.tankType = tankType;
        this.gameCore = gameCore;
        currentHealth = maxHealth;
        currentMap = map;


        speed = 5;
        loadTankImage();
        loadTankSounds();
    }

    private void loadTankImage() {
        String imageName = switch (tankType) {
            case RED -> "/tank_red.png";
            case BLUE, BOT -> "/tank_blue.png";
        };

        try {
            tankImage = ImageIO.read(Objects.requireNonNull(getClass().getResource(imageName)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadTankSounds() {
        try {
            File shootFile = new File(Objects.requireNonNull(getClass().getClassLoader().getResource("shoot.wav")).toURI());
            File hitFile = new File(Objects.requireNonNull(getClass().getClassLoader().getResource("hit.wav")).toURI());
            File explosionFile = new File(Objects.requireNonNull(getClass().getClassLoader().getResource("explosion.wav")).toURI());

            AudioInputStream shootInputStream = AudioSystem.getAudioInputStream(shootFile);
            AudioInputStream hitInputStream = AudioSystem.getAudioInputStream(hitFile);
            AudioInputStream explosionInputStream = AudioSystem.getAudioInputStream(explosionFile);

            shootSound = AudioSystem.getClip();
            shootSound.open(shootInputStream);

            hitSound = AudioSystem.getClip();
            hitSound.open(hitInputStream);

            explosionSound = AudioSystem.getClip();
            explosionSound.open(explosionInputStream);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void startShootCooldownTimer() {
        Timer cooldownTimer = new Timer();
        cooldownTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                canShoot = true;
            }
        }, shootCooldown);
    }

    public void shoot() {
        if (canShoot) {
            if (shootSound != null) {
                shootSound.setFramePosition(0);
                shootSound.start();
            }

            performRaycasting();
            canShoot = false;
            startShootCooldownTimer();
        }
    }

    private void performRaycasting() {
        int bulletX = x;
        int bulletY = y;
        int bulletDirection = direction;

        while (bulletX >= 0 && bulletY >= 0) {
            switch (bulletDirection) {
                case 1 -> bulletY -= bulletSpeed;
                case 0 -> bulletY += bulletSpeed;
                case 2 -> bulletX -= bulletSpeed;
                case 3 -> bulletX += bulletSpeed;
            }

            if (bulletX >= 0 && bulletX < currentMap.getWidth() * GameCore.CELL_SIZE &&
                    bulletY >= 0 && bulletY < currentMap.getHeight() * GameCore.CELL_SIZE) {
                if (currentMap.isWall(bulletX / GameCore.CELL_SIZE, bulletY / GameCore.CELL_SIZE)) {
                    break;
                }
            }

            for (Tank tank : gameCore.getTanks()) {
                if (tank != this && tank.intersects(bulletX, bulletY)) {
                    tank.applyDamage(this.damage);
                    this.countOfHit++;
                    break;
                }
            }
        }
    }

    private boolean intersects(int targetX, int targetY) {
        Rectangle targetBounds = new Rectangle(targetX, targetY, tankSize, tankSize);
        Rectangle tankBounds = new Rectangle(x, y, tankSize, tankSize);
        return targetBounds.intersects(tankBounds);
    }

    private void applyDamage(int damage) {
        Random random = new Random();
        var randomDamage = random.nextInt(damage - 11) + damage;
        currentHealth -= randomDamage;

        if (hitSound != null) {
            hitSound.setFramePosition(0);
            hitSound.start();
        }

        if (currentHealth > 0) {
            countOfHit += randomDamage;
        } else {
            currentHealth = 0;
            countOfKill++;
            isAlive = false;

            if (explosionSound != null) {
                explosionSound.setFramePosition(0);
                explosionSound.start();
            }
        }

        gameCore.updateGameState();
    }

    public void draw(Graphics graphics) {
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

    private boolean checkTankCollision(int targetX, int targetY) {
        Rectangle targetBounds = new Rectangle(targetX, targetY, tankSize, tankSize);

        for (Tank tank : gameCore.getTanks()) {
            if (tank != this) {
                Rectangle otherTankBounds = new Rectangle(tank.getX(), tank.getY(), tankSize, tankSize);
                if (targetBounds.intersects(otherTankBounds)) {
                    return false;
                }
            }
        }

        return true;
    }

    public void moveUp() {
        if (checkWallCollision(x, y - speed) && checkTankCollision(x, y - speed)) {
            y -= speed;
            direction = 1;
        }
    }

    public void moveDown() {
        if (checkWallCollision(x, y + speed) && checkTankCollision(x, y + speed)) {
            y += speed;
            direction = 0;
        }
    }

    public void moveRight() {
        if (checkWallCollision(x + speed, y) && checkTankCollision(x + speed, y)) {
            x += speed;
            direction = 3;
        }
    }

    public void moveLeft() {
        if (checkWallCollision(x - speed, y) && checkTankCollision(x - speed, y)) {
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

    public int getCountOfKill() {
        return countOfKill;
    }

    public int getCountOfHit() {
        return countOfHit;
    }

    public TankType getTankType() {
        return tankType;
    }

    public void reset() {
        isAlive = true;
        currentHealth = 100;
        int tankX = (GameCore.SCREEN_WIDTH / GameCore.CELL_SIZE) / 2;
        int tankY = (GameCore.SCREEN_HEIGHT / GameCore.CELL_SIZE) / 2;

        switch (tankType) {
            case RED -> {
                x = tankX * GameCore.CELL_SIZE;
                y = (tankY + 5) * GameCore.CELL_SIZE;
            }
            case BLUE -> {
                x = tankX * GameCore.CELL_SIZE;
                y = (tankY - 5) * GameCore.CELL_SIZE;
            }
        }
    }
}
