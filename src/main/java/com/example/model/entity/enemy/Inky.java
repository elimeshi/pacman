package com.example.model.entity.enemy;

import java.awt.geom.Point2D;

public class Inky extends Ghost {

    public Inky(double x, double y, double speed) {
        super(x, y, speed);
        mode = GhostMode.InPen;
        direction = -90;
        nextDirection = -90;
        regenPos = new Point2D.Double(11.5, 14);
    }

    public Point2D.Double targetTile() {
        if (mode == GhostMode.Chase) {
            Point2D.Double pacmanTile = ai.getPacmanTile();
            Point2D.Double blinkyTile = ai.getBlinkyTile();
            int pacmanDirection = ai.getPacmanDirection();
            switch (pacmanDirection) {
                case 0:   pacmanTile.x += 4; break;
                case -90: pacmanTile.y += 4; break;
                case 90:  pacmanTile.y -= 4;
                case 180: pacmanTile.x -= 4; break;
            }
            return new Point2D.Double(2 * pacmanTile.x - blinkyTile.x, 2 * pacmanTile.y - blinkyTile.y);
        }
        return new Point2D.Double(27, 31);
    }
}
