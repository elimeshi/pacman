package com.example.controller;

import com.example.model.entity.enemy.Inky;
import com.example.model.tile.TileMap;
import com.example.model.entity.Pacman;
import com.example.model.entity.enemy.Ghost;

public class InkyController extends GhostController {

    Ghost inky;

    public InkyController(Inky inky, Pacman pacman, int FPS, TileMap tileMap) {
        super(inky, pacman, FPS, tileMap);
        this.inky = inky;
    }

    @Override
    public void updateGhostMode() {
        
    }

    @Override
    public void setFrightened() {inky.setFrightened();}

    @Override
    public void update() {
        updateGhostMode();
    }
    
}
