package com.example.controller;

import java.awt.geom.Point2D;

import com.example.model.entity.Pacman;
import com.example.model.entity.enemy.Clyde;
import com.example.model.entity.enemy.Ghost;
import com.example.model.entity.enemy.GhostMode;
import com.example.model.tile.TileMap;

public class ClydeController extends GhostController {

    Ghost clyde;

    public ClydeController(Clyde clyde, Pacman pacman, AI ai, TileMap tileMap) {
        super(clyde, pacman, ai, tileMap);
        this.clyde = clyde;
        scatterTile = new Point2D.Double(0, 31);
    }

    public Point2D.Double targetTile() {
        if (clyde.mode == GhostMode.Chase) {
            Point2D.Double pacmanTile = getPacmanTile();
            return pacmanTile.distance(clyde.x, clyde.y) > 8 ? pacmanTile : scatterTile;
        }
        return super.targetTile();
    }
}
