package com.example.controller;

import java.awt.geom.Point2D;

import com.example.model.entity.enemy.Inky;
import com.example.model.tile.TileMap;
import com.example.model.entity.Pacman;
import com.example.model.entity.enemy.Ghost;
import com.example.model.entity.enemy.GhostMode;

public class InkyController extends GhostController {

    Ghost inky;

    public InkyController(Inky inky, Pacman pacman, AI ai) {
        super(inky, pacman, ai);
        this.inky = inky;
    }

    public void initialize() {
        super.initialize();
        scatterTile = new Point2D.Double(TileMap.getInstance().mapWidth(), 31);
    }

    public Point2D.Double targetTile() {
        if (inky.mode == GhostMode.Chase) {
            Point2D.Double pacmanTile = getPacmanTile();
            Point2D.Double blinkyTile = ai.getBlinkyTile();
            int pacmanDirection = ai.getPacmanDirection();
            switch (pacmanDirection) {
                case 0:   pacmanTile.x += 2; break;
                case -90: pacmanTile.y += 2; break;
                case 90:  pacmanTile.y -= 2;
                case 180: pacmanTile.x -= 2; break;
            }
            return new Point2D.Double(2 * pacmanTile.x - blinkyTile.x, 2 * pacmanTile.y - blinkyTile.y);
        }
        return super.targetTile();
    }
}
