package com.example.controller;

import com.example.model.entity.Pacman;
import com.example.model.entity.enemy.*;
import com.example.model.fruit.Fruit;
import com.example.model.tile.TileMap;
import com.example.model.tile.TileType;
import com.example.utils.SoundManager;

public class GameController {

    public SoundManager soundManager;
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

    public GameController(Pacman pacman, Ghost[] ghosts, AI ai, Fruit fruit, TileMap tileMap, int FPS, SoundManager soundManager) {
        this.pacman = pacman;
        this.fps = FPS;
        pacmanController = new PacmanController(pacman, tileMap);
        ghostControllers = new GhostController[]{
            new BlinkyController((Blinky) ghosts[0], pacman, ai, FPS, tileMap, soundManager),
            new PinkyController((Pinky) ghosts[1], pacman, ai, FPS, tileMap, soundManager),
            new InkyController((Inky) ghosts[2], pacman, ai, FPS, tileMap, soundManager),
            new ClydeController((Clyde) ghosts[3], pacman, ai, FPS, tileMap, soundManager),
        };
        fruitController = new FruitController(fruit, pacman, FPS, soundManager);
        this.soundManager = soundManager;
    }

    public void initializeNewGame() {
        pacman.initialize();
        for (GhostController controller : ghostControllers) controller.initialize();
        eatenDots = 0;
        ghostTimer = 0;
        victory = false;
    }

    public void initializeNextLevel() {
        pacman.restart();
        for (GhostController controller : ghostControllers) controller.initialize();
        eatenDots = 0;
        ghostTimer = 0;
        victory = false;
    }

    public void restart() {
        pacman.restart();
        for (GhostController controller : ghostControllers) controller.restart();
        eatenDots = 0;
        ghostTimer = 0;
        victory = false;
    }

    public void pacmanIsDead() {
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
        if (pacmanTile == TileType.Dot) {
            eatenDots++;
            soundManager.play("coin");
        } else if (pacmanTile == TileType.Energizer) {
            soundManager.play("energizer");
        }
        checkForLevelComplete();
        if (victory) return;

        releaseGhosts();
        for (GhostController controller : ghostControllers) {
            if (pacmanTile == TileType.Energizer) controller.setFrightened();
            controller.update();
        }
        fruitController.update();
    }
}