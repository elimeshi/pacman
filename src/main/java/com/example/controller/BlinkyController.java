package com.example.controller;

import java.awt.geom.Point2D;

import com.example.model.entity.Pacman;
import com.example.model.entity.enemy.Blinky;
import com.example.model.entity.enemy.Ghost;
import com.example.model.entity.enemy.GhostMode;
import com.example.model.tile.TileMap;
import com.example.utils.SoundManager;

public class BlinkyController extends GhostController {

    Ghost blinky;
    
    public BlinkyController(Blinky blinky, Pacman pacman, AI ai, int FPS, TileMap tileMap, SoundManager soundManager) {
        super(blinky, pacman, ai, FPS, tileMap, soundManager);
        this.blinky = blinky;
        scatterTile = new Point2D.Double(25, -4);
        getNextMode();
    }

    public void initialize() {
        super.initialize();
        getNextMode();
    }

    @Override
    public void setInPen() {
        ghost.setMode(GhostMode.Spawn);
    }

    public Point2D.Double targetTile() {
        if (blinky.mode == GhostMode.Chase) return getPacmanTile();
        return super.targetTile();
    }
}
