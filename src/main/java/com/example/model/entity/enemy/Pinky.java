package com.example.model.entity.enemy;

import java.awt.geom.Point2D;

public class Pinky extends Ghost {

    public Pinky(double x, double y, double speed) {
        super(x, y, speed);
        mode = GhostMode.InPen;
        direction = 90;
        regenPos = new Point2D.Double(13.5, 14);
    }
}
