package com.example.config;

public class GameConfig {
    public int tileSize = 30;
    public int tilesInCol = 31;                       // edit me
    public int tilesInRow = 28;                       // edit me
    public int WINDOW_WIDTH = tilesInRow * tileSize;
    public int WINDOW_HEIGHT = (tilesInCol + 2) * tileSize;

    public final int FPS = 30;                        // not recommended, but you can edit me

    public void setTilesAmount(int tilesInCol, int tilesInRow) {
        this.tilesInCol = tilesInCol;
        this.tilesInRow = tilesInRow;
        this.WINDOW_WIDTH = tilesInRow * tileSize;
        this.WINDOW_HEIGHT = tilesInCol * tileSize;
    }
}
