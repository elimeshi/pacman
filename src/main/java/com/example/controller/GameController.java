package com.example.controller;

import com.example.model.entity.Pacman;
import com.example.model.entity.enemy.*;
import com.example.model.fruit.Fruit;
import com.example.model.tile.TileMap;
import com.example.model.tile.TileType;

public class GameController {

    public Pacman pacman;
    public PacmanController pacmanController;
    public GhostController[] ghostControllers;
    public FruitController fruitController;
    public TileType pacmanTile;
    public boolean victory = false;
    public int fps;
    public int initialDots = 24;
    public int quarterDots = initialDots / 4;
    public int eatenDots = 0;
    public int ghostTimer = 0;

    public GameController(Pacman pacman, Blinky blinky, Pinky pinky, Inky inky, Clyde clyde, AI ai, Fruit fruit, TileMap tileMap, int FPS) {
        this.pacman = pacman;
        this.fps = FPS;
        pacmanController = new PacmanController(pacman, tileMap);
        ghostControllers = new GhostController[]{
            new BlinkyController(blinky, pacman, ai, FPS, tileMap),
            new PinkyController(pinky, pacman, ai, FPS, tileMap),
            new InkyController(inky, pacman, ai, FPS, tileMap),
            new ClydeController(clyde, pacman, ai, FPS, tileMap),
        };
        fruitController = new FruitController(fruit, pacman, FPS);
    }

    public void restart() {
        pacman.restart();
        for (GhostController controller : ghostControllers) controller.restart();
        eatenDots = 0;
        ghostTimer = 0;
        victory = false;
    }

    public void checkIfPacmanIsDead() {
        if (!pacman.deadNow) return; 
        ghostTimer = 0;
        for (GhostController controller : ghostControllers) controller.restart();
        pacman.deadNow = false;
    }

    public void releaseGhosts() {
        if (++ghostTimer > 9 * fps && eatenDots >= quarterDots * 3) {
            ghostControllers[3].getOutOfPen();
        } else if (ghostTimer > 6 * fps && eatenDots >= quarterDots * 2) {
            ghostControllers[2].getOutOfPen();
        } else if (ghostTimer > 3 * fps && eatenDots >= quarterDots) {
            ghostControllers[1].getOutOfPen();
        }
    }

    public void checkForLevelComplete() {
        if (eatenDots >= initialDots) victory = true;
    }

    public void update() {
        pacmanController.update();
        pacmanTile = TileType.Empty;
        if (pacmanController.isOnTile()) pacmanTile = pacmanController.collectPellet();
        if (pacmanTile == TileType.Dot) eatenDots++;
        checkForLevelComplete();
        if (victory) return;

        releaseGhosts();
        for (GhostController controller : ghostControllers) {
            if (pacmanTile == TileType.Energizer) controller.setFrightened();
            controller.update();
        }
        checkIfPacmanIsDead();
        fruitController.update();
    }
}