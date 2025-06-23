package com.example.controller;

import com.example.model.Speeds;
import com.example.model.entity.Pacman;
import com.example.model.entity.enemy.Blinky;
import com.example.model.entity.enemy.Ghost;
import com.example.model.entity.enemy.GhostMode;
import com.example.model.tile.TileMap;

public class BlinkyController extends GhostController {

    Ghost blinky;
    
    public BlinkyController(Blinky blinky, Pacman pacman, int FPS, TileMap tileMap) {
        super(blinky, pacman, FPS, tileMap);
        this.blinky = blinky;
        getNextMode();
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
                blinky.setMode(GhostMode.Chase);
                blinky.setDirection(blinky.ai.getDirection(blinky));
            } else {
                blinky.setDirection(90);
            } 
        } else {
            if (++modeCounter >= modeDuration) getNextMode();
            blinky.setMode(currenMode);
        }
    }

    @Override
    public void setFrightened() {
        blinky.setFrightened();
        frightenedCounter = 0;
    }

    @Override
    public void update() {
        updateGhostMode();
        super.update();
    }
}
