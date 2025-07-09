package com.example.config;

public class GameConfig {
    public static final int tileSize = 30;
    public static final int tilesInCol = 31;                       // edit me
    public static final int tilesInRow = 34;                       // edit me
    public static final int WINDOW_WIDTH = tilesInRow * tileSize;
    public static final int WINDOW_HEIGHT = (tilesInCol + 2) * tileSize;

    public static final int FPS = 30;                        // not recommended, but you can edit me
}
