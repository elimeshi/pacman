package com.example.controller;

import java.awt.geom.Point2D;

import com.example.model.entity.enemy.Inky;
import com.example.model.tile.TileMap;
import com.example.model.entity.Pacman;
import com.example.model.entity.enemy.AI;
import com.example.model.entity.enemy.Ghost;
import com.example.model.entity.enemy.GhostMode;

public class InkyController extends GhostController {

    Ghost inky;

    public InkyController(Inky inky, Pacman pacman, AI ai, int FPS, TileMap tileMap) {
        super(inky, pacman, ai, FPS, tileMap);
        this.inky = inky;
    }

    public Point2D.Double targetTile() {
        if (inky.mode == GhostMode.Chase) {
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

    @Override
    public void updateGhostMode() {
        
    }
}
