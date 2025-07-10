package com.example.controller;

import java.awt.geom.Point2D;

import com.example.model.Speeds;
import com.example.model.entity.Pacman;
import com.example.model.entity.enemy.Blinky;
import com.example.model.entity.enemy.Ghost;
import com.example.model.entity.enemy.GhostMode;
import com.example.model.tile.TileMap;

public class BlinkyController extends GhostController {

    Ghost blinky;
    
    public BlinkyController(Blinky blinky, Pacman pacman, AI ai) {
        super(blinky, pacman, ai);
        this.blinky = blinky;
    }

    public void initialize() {
        super.initialize();
        getNextMode();
        scatterTile = new Point2D.Double(TileMap.getInstance().mapWidth() - 2, -4);
    }

    @Override
    public void setElroyMode() {
        if (currentMode == GhostMode.Elroy) return;

        modes.clear();
        durations.clear();
        currentMode = GhostMode.Elroy;
        if (ghost.mode == GhostMode.Chase || ghost.mode == GhostMode.Scatter) {
            ghost.setMode(GhostMode.Elroy);
            ghost.setSpeed(Speeds.elroy);
        }
    }

    @Override
    public void upgradeElroyMode() {
        Speeds.upgradeElroySpeed();
        if (ghost.mode == GhostMode.Elroy) {
            ghost.setSpeed(Speeds.elroy);
        }
    }

    @Override
    public void setNormalSpeed() { ghost.setSpeed(ghost.mode == GhostMode.Elroy ? Speeds.elroy : Speeds.ghostNormal); }

    @Override
    public void setInPen() {
        ghost.setMode(GhostMode.Spawn);
    }

    @Override
    public Point2D.Double targetTile() {
        if (blinky.mode == GhostMode.Chase || blinky.mode == GhostMode.Elroy) return getPacmanTile();
        return super.targetTile();
    }
}
