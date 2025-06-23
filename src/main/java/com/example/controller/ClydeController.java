package com.example.controller;

import com.example.model.entity.Pacman;
import com.example.model.entity.enemy.Clyde;
import com.example.model.entity.enemy.Ghost;
import com.example.model.tile.TileMap;

public class ClydeController extends GhostController {

    Ghost clyde;

    public ClydeController(Clyde clyde, Pacman pacman, int FPS, TileMap tileMap) {
        super(clyde, pacman, FPS, tileMap);
        this.clyde = clyde;
    }

    @Override
    public void updateGhostMode() {
        
    }

    @Override
    public void setFrightened() {clyde.setFrightened();}

    @Override
    public void update() {
        updateGhostMode();
    }
    
}
