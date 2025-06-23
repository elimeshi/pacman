package com.example.controller;

import com.example.model.tile.Tile;
import com.example.model.tile.TileMap;
import com.example.model.tile.TileType;

public abstract class EntityController {
    public double x, y, speed;
    public double EPSILON = 1e-5;
    public int direction, nextDirection;
    public TileMap tileMap;

    public EntityController(TileMap tileMap) {
        this.tileMap = tileMap;
    }

    public boolean isOnTile() {
        return y % 1 == 0 && x % 1 == 0;
    }

    public double snapIfClose(double pos) {
        double threshold = speed / 2.0 + EPSILON;
        double nearest = Math.round(pos);
        return Math.abs(pos - nearest) <= threshold ? nearest : pos;
    }

    public void updateDirection() {
        if ((direction - nextDirection) % 180 == 0) { // if next direction is back from the current direction
            direction = nextDirection;
        } else if (nextDirection != direction && isOnTile() && getNextTile(nextDirection).type != TileType.Wall) 
            direction = nextDirection;
    }
    
    public Tile getNextTile(int direction) {
        int nextTileRow = (int) y;
        int nextTileCol = (int) x;

        switch(direction) {
            case 90: nextTileRow -= 1; break; // up
            case -90: nextTileRow += 1; break; // down
            case 0: nextTileCol += 1; break; // right
            case 180: nextTileCol -= 1; break; // left
        }

        nextTileCol = (nextTileCol + 28) % 28;

        return tileMap.getTileAt(nextTileRow, nextTileCol);
    }

    public void updatePosition() {
        if (isOnTile() && getNextTile(direction).type == TileType.Wall) return;
        
        switch (direction) {
            case 0:   x += speed; break; // right
            case 90:  y -= speed; break; // up
            case -90: y += speed; break; // down
            case 180: x -= speed; break; // left
        }
        x = (x + 27) % 27;

        x = snapIfClose(x);
        y = snapIfClose(y);
    }

    public void update() {
        updateDirection();
        updatePosition();
    }
}
