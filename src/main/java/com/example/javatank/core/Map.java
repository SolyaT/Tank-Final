package com.example.javatank.core;

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
            for (int j = 0; j < height; j++) {
                grid[i][j] = 0;
            }
        }

        for (int i = 0; i < width; i++) {
            grid[i][0] = 1;
            grid[i][height - 1] = 1;
        }
        for (int j = 0; j < height; j++) {
            grid[0][j] = 1;
            grid[width - 1][j] = 1;
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isWall(int x, int y) {
        return grid[x][y] == 1;
    }
}