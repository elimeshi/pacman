package com.example.controller;

import com.example.model.entity.Entity;
import com.example.model.tile.Tile;
import com.example.model.tile.TileMap;
import com.example.model.tile.TileType;

public abstract class EntityController {

    public Entity entity;
    public double EPSILON = 1e-5;

    public EntityController(Entity entity) {
        this.entity = entity;
    }

    public boolean isOnTile() {
        return entity.y % 1 == 0 && entity.x % 1 == 0;
    }

    public double snapIfClose(double pos) {
        double threshold = entity.speed / 2.0 + EPSILON;
        double nearest = Math.round(pos);
        return Math.abs(pos - nearest) <= threshold ? nearest : pos;
    }

    public Tile getNextTile(int direction) {
        int nextTileRow = (int) entity.y;
        int nextTileCol = (int) entity.x;

        switch(direction) {
            case 90: nextTileRow -= 1; break; // up
            case -90: nextTileRow += 1; break; // down
            case 0: nextTileCol += 1; break; // right
            case 180: nextTileCol -= 1; break; // left
        }

        nextTileCol = (nextTileCol + TileMap.getInstance().mapWidth()) % TileMap.getInstance().mapWidth();

        return TileMap.getInstance().getTileAt(nextTileRow, nextTileCol);
    }

    public void updatePosition() {
        if (isOnTile() && getNextTile(entity.direction).type == TileType.Wall) return;
        double x = entity.x, y = entity.y, speed = entity.speed;
        switch (entity.direction) {
            case 0:   x += speed; break; // right
            case 90:  y -= speed; break; // up
            case -90: y += speed; break; // down
            case 180: x -= speed; break; // left
        }
        x = (x + TileMap.getInstance().mapWidth()) % TileMap.getInstance().mapWidth();

        x = snapIfClose(x);
        y = snapIfClose(y);

        entity.setPosition(x, y);
    }

    public void update() {
        updatePosition();
    }
}
