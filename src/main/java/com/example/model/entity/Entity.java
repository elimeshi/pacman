package com.example.model.entity;

import java.awt.geom.Point2D;

public class Entity {
    public double x, y, speed;
    public int direction;
    public int scale;
    public double EPSILON = 1e-5;
    public Point2D.Double regenPos; // regeneration position in pixels

    public Entity(double x, double y, double speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
    }

    public boolean isOnTileOffset(double pos, double tileOffset) {
        return pos % 1 == tileOffset;
    } 

    public double snapIfClose(double pos) {
        double threshold = speed / 2.0 + EPSILON;
        double nearest = Math.round(pos);
        return Math.abs(pos - nearest) <= threshold ? nearest : pos;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
