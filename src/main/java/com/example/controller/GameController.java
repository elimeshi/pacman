package com.example.controller;

import com.example.model.entity.Pacman;
import com.example.model.entity.enemy.*;
import com.example.model.tile.TileMap;
import com.example.model.tile.TileType;

public class GameController {

    public PacmanController pacmanController;
    public GhostController[] ghostControllers;
    public TileType pacmanTile;

    public GameController(Pacman pacman, Blinky blinky, Pinky pinky, Inky inky, Clyde clyde, AI ai, TileMap tileMap, int FPS) {
        pacmanController = new PacmanController(pacman, tileMap);
        ghostControllers = new GhostController[]{
            new BlinkyController(blinky, pacman, ai, FPS, tileMap),
            new PinkyController(pinky, pacman, ai, FPS, tileMap),
            new InkyController(inky, pacman, ai, FPS, tileMap),
            new ClydeController(clyde, pacman, ai, FPS, tileMap),
        };
    }

    public void update() {
        pacmanController.update();
        pacmanTile = TileType.Empty;
        if (pacmanController.isOnTile()) pacmanTile = pacmanController.collectPellet();
        for (GhostController controller : ghostControllers) {
            if (pacmanTile == TileType.Energizer) controller.setFrightened();
            controller.update();
        }
    }
}