package com.example.model.entity;

import com.example.model.entity.enemy.Ghost;

public class Entity {
    public double x, y, speed;
    public int mapWidth;
    public int direction;
    public int scale;
    public double EPSILON = 1e-5;

    public Entity(double x, double y, double speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.mapWidth = 27;
    }

    public boolean isOnTileOffset(double pos, double tileOffset) {
        return pos % 1 == tileOffset;
    } 

    public boolean isInPen() {
        return (x >= 11 && x <= 16 && 
                y >= 13 && y <= 15) 
                ||
               (x >= 13 && x <= 14 && 
                y >= 11 && y <= 15);
    }

    public boolean isOnPenTile() {
        return isOnTileOffset(x, 0.5) &&
               isOnTileOffset(y, 0);
    }

    public double snapToHalf(double pos) {
        return Math.round(pos * 2) / 2.0;
    }

    public double snapIfClose(double pos) {
        double threshold = speed / 2.0 + EPSILON;
        double nearest = this instanceof Ghost && isInPen() ? snapToHalf(pos) : Math.round(pos);
        return Math.abs(pos - nearest) <= threshold ? nearest : pos;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
