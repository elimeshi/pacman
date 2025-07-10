package com.example.model.entity.enemy;

import java.awt.geom.Point2D;

public class Inky extends Ghost {

    public Inky(double x, double y, double speed) {
        super(x, y, speed);
        mode = GhostMode.InPen;
        direction = 90;
    }

    public void setRegenPos() { regenPos = new Point2D.Double(penCenter.x - 2, penCenter.y); }
}
