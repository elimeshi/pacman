package com.example.model.entity.enemy;

import java.awt.geom.Point2D;

public class Clyde extends Ghost {

    public Clyde(double x, double y, double speed) {
        super(x, y, speed);
        mode = GhostMode.InPen;
        direction = 90;
        regenPos = new Point2D.Double(15.5, 14);
    }
}
