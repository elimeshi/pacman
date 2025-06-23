package com.example.controller;

import com.example.model.entity.Pacman;
import com.example.model.entity.enemy.Ghost;
import com.example.model.entity.enemy.Pinky;
import com.example.model.tile.TileMap;

public class PinkyController extends GhostController {

    Ghost pinky;

    public PinkyController(Pinky pinky, Pacman pacman, int FPS, TileMap tileMap) {
        super(pinky, pacman, FPS, tileMap);
        this.pinky = pinky;
    }

    @Override
    public void updateGhostMode() {
        
    }

    @Override
    public void setFrightened() {pinky.setFrightened();}

    @Override
    public void update() {
        updateGhostMode();
    }
}
