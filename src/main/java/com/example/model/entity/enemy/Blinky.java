package com.example.model.entity.enemy;

import java.awt.geom.Point2D;

public class Blinky extends Ghost {

    public Blinky(double x, double y, double speed) {
        super(x, y, speed);
        mode = GhostMode.Scatter;
        regenPos = new Point2D.Double(13.5, 14);
    }

    public void initialize() {
        super.initialize();
        mode = GhostMode.Scatter;
        y = 11;
        direction = 0;
    }
}
