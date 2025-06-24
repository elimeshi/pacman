package com.example.controller;

import java.awt.geom.Point2D;

import com.example.model.Speeds;
import com.example.model.entity.Pacman;
import com.example.model.entity.enemy.AI;
import com.example.model.entity.enemy.Blinky;
import com.example.model.entity.enemy.Ghost;
import com.example.model.entity.enemy.GhostMode;
import com.example.model.tile.TileMap;

public class BlinkyController extends GhostController {

    Ghost blinky;
    
    public BlinkyController(Blinky blinky, Pacman pacman, AI ai, int FPS, TileMap tileMap) {
        super(blinky, pacman, ai, FPS, tileMap);
        this.blinky = blinky;
        scatterTile = new Point2D.Double(25, -4);
        getNextMode();
    }

    public Point2D.Double targetTile() {
        if (blinky.mode == GhostMode.Chase) return getPacmanTile();
        return super.targetTile();
    }

    @Override
    public void updateGhostMode() {
        if (blinky.mode == GhostMode.Frightened) {
            if (collisionWithPacman()) {
                blinky.setMode(GhostMode.Eaten);
                blinky.setSpeed(Speeds.eaten);
            } else if (++frightenedCounter >= frightenedDuration * FPS) {
                blinky.setMode(currenMode);
            }
        } else if (blinky.mode == GhostMode.Eaten) {
            if (blinky.x == 13.5 && blinky.y == 11) {
                blinky.setDirection(-90);
            } else if (blinky.y == blinky.regenPos.y && blinky.x == blinky.regenPos.x) {
                blinky.setMode(GhostMode.Spawn);
                blinky.setSpeed(Speeds.ghostNormal);
            }
        } else if (collisionWithPacman()) {
            pacman.die();
        } else if (blinky.mode == GhostMode.Spawn) {
            if (blinky.y == 11) {
                blinky.setMode(currenMode);
                blinky.setDirection(ai.getDirectionToTarget(blinky, targetTile()));
            } else {
                blinky.setDirection(90);
            } 
        } else {
            if (++modeCounter >= modeDuration) getNextMode();
            blinky.setMode(currenMode);
        }
    }
}
