package com.example.javatank.core;

import java.awt.*;
import java.util.Arrays;

import static com.example.javatank.core.GameCore.CELL_SIZE;

public class Map {
    private final int width;
    private final int height;
    private final int[][] grid;

    public Map(int width, int height) {
        this.width = width;
        this.height = height;
        grid = new int[width][height];

        initializeMap();
    }

    private void initializeMap() {
        for (int i = 0; i < width; i++) {
            Arrays.fill(grid[i], 0);
        }

       for (int i = 0; i < width; i++) {
            grid[i][0] = 1;
            grid[i][height - 1] = 1;
        }
        for (int j = 0; j < height; j++) {
            grid[0][j] = 1;
            grid[width - 1][j] = 1;
        }

        for (int i = 2; i < width - 2; i++) {
            grid[i][2] = 1;
            grid[i][height - 3] = 1;
        }
        for (int j = 4; j < height - 4; j++) {
            grid[2][j] = 1;
            grid[width - 3][j] = 1;
        }

        int wallColumn = width / 2;
        for (int j = 2; j < height - 2; j++) {
            grid[wallColumn][j] = 1;
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isWall(int x, int y) {
        if (x >= grid.length || y >= grid.length) {
            return false;
        }

        return grid[x][y] == 1;
    }

    public void drawMap(Graphics graphics) {
        graphics.setColor(new Color(128, 0, 128)); // Brown color
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (isWall(i, j)) {
                    int x = i * CELL_SIZE;
                    int y = j * CELL_SIZE;
                    graphics.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                }
            }
        }
    }
}