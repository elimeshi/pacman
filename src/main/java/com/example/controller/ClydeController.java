package com.example.controller;

import java.awt.geom.Point2D;

import com.example.model.entity.Pacman;
import com.example.model.entity.enemy.AI;
import com.example.model.entity.enemy.Clyde;
import com.example.model.entity.enemy.Ghost;
import com.example.model.entity.enemy.GhostMode;
import com.example.model.tile.TileMap;

public class ClydeController extends GhostController {

    Ghost clyde;

    public ClydeController(Clyde clyde, Pacman pacman, AI ai, int FPS, TileMap tileMap) {
        super(clyde, pacman, ai, FPS, tileMap);
        this.clyde = clyde;
    }

    public Point2D.Double targetTile() {
        Point2D.Double pacmanTile = ai.getPacmanTile();
        if (clyde.mode == GhostMode.Chase && pacmanTile.distance(clyde.x, clyde.y) < 8) return pacmanTile;
        return new Point2D.Double(0, 31);
    }

    @Override
    public void updateGhostMode() {
        
    }
}
