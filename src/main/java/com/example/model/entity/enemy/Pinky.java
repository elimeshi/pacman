package com.example.model.entity.enemy;

import java.awt.geom.Point2D;

public class Pinky extends Ghost {

    public Pinky(double x, double y, double speed) {
        super(x, y, speed);
        mode = GhostMode.InPen;
        direction = 90;
        nextDirection = 90;
        regenPos = new Point2D.Double(13.5, 14);
    }

    public Point2D.Double targetTile() {
        if (mode == GhostMode.Chase) {
            Point2D.Double pacmanTile = ai.getPacmanTile();
            switch (ai.getPacmanDirection()) {
                case 0:   pacmanTile.x += 4; break;
                case -90: pacmanTile.y += 4; break;
                case 90:  pacmanTile.y -= 4;
                case 180: pacmanTile.x -= 4; break;
            }
            return pacmanTile;
        }
        return new Point2D.Double(2, -4);
    }
}
