package com.example.controller;

import com.example.config.GameConfig;
import com.example.model.entity.Pacman;
import com.example.model.entity.enemy.*;
import com.example.model.fruit.Fruit;
import com.example.model.tile.TileMap;
import com.example.model.tile.TileType;
import com.example.utils.SoundManager;

public class GameController {

    public Pacman pacman;
    public PacmanController pacmanController;
    public GhostController[] ghostControllers;
    public FruitController fruitController;
    public TileMap tileMap;
    public TileType pacmanTile;
    public boolean victory = false;
    public int initialDots = 30;
    public int quarterDots = initialDots / 4;
    public int eatenDots = 0;
    public int ghostTimer = 0;
    public int[] blinkyElroyMode;

    public GameController(Pacman pacman, Ghost[] ghosts, AI ai, Fruit fruit, TileMap tileMap) {
        this.pacman = pacman;
        this.tileMap = tileMap;
        pacmanController = new PacmanController(pacman, tileMap);
        ghostControllers = new GhostController[]{
            new BlinkyController((Blinky) ghosts[0], pacman, ai, tileMap),
            new PinkyController((Pinky) ghosts[1], pacman, ai, tileMap),
            new InkyController((Inky) ghosts[2], pacman, ai, tileMap),
            new ClydeController((Clyde) ghosts[3], pacman, ai, tileMap),
        };
        fruitController = new FruitController(fruit, pacman, tileMap);
    }

    public void initializeNewGame() {
        pacman.initialize();
        GhostModeSchedule.getInstance().loadModeSchedule(1);
        blinkyElroyMode = GhostModeSchedule.getInstance().loadBlinkyConfig(1);
        System.out.println(blinkyElroyMode[0] + " " + blinkyElroyMode[1]);
        for (GhostController controller : ghostControllers) controller.initialize();
        tileMap.loadLevel(1);
        eatenDots = 0;
        ghostTimer = 0;
        fruitController.initialize();
        victory = false;
    }

    public void initializeNextLevel(int level) {
        pacman.restart();
        GhostModeSchedule.getInstance().loadModeSchedule(level);
        blinkyElroyMode = GhostModeSchedule.getInstance().loadBlinkyConfig(level);
        for (GhostController controller : ghostControllers) controller.initialize();
        fruitController.initialize();
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

    public void updateBlinkyElroyMode() {
        if (initialDots - eatenDots < blinkyElroyMode[1]) return;
        if (initialDots - eatenDots <= blinkyElroyMode[1]) ghostControllers[0].upgradeElroyMode();
        else if (initialDots - eatenDots <= blinkyElroyMode[0]) ghostControllers[0].setElroyMode();
    }

    public void pacmanIsDead() {
        ghostTimer = 0;
        for (GhostController controller : ghostControllers) controller.restart();
    }

    public void releaseGhosts() {
        if (++ghostTimer > 9 * GameConfig.FPS && eatenDots >= quarterDots * 3) {
            ghostControllers[3].getOutOfPen();
        } else if (ghostTimer > 6 * GameConfig.FPS && eatenDots >= quarterDots * 2) {
            ghostControllers[2].getOutOfPen();
        } else if (ghostTimer > 3 * GameConfig.FPS && eatenDots >= quarterDots) {
            ghostControllers[1].getOutOfPen();
        }
    }

    public void checkForLevelComplete() {
        if (eatenDots >= initialDots) victory = true;
    }

    public void update() {
        if (pacman.dead) {
            pacman.updateDeath();
            return;
        }

        pacmanController.update();
        pacmanTile = TileType.Empty;
        if (pacmanController.isOnTile()) pacmanTile = pacmanController.collectPellet();
        if (pacmanTile == TileType.Dot) {
            eatenDots++;
            SoundManager.getInstance().play("coin");
            fruitController.addPossiblePosition(new int[]{(int) pacman.x, (int) pacman.y});
        } else if (pacmanTile == TileType.Energizer) {
            SoundManager.getInstance().play("energizer");
            fruitController.addPossiblePosition(new int[]{(int) pacman.x, (int) pacman.y});
        }
        checkForLevelComplete();
        if (victory) return;

        releaseGhosts();
        updateBlinkyElroyMode();
        for (GhostController controller : ghostControllers) {
            if (pacmanTile == TileType.Energizer) controller.setFrightened();
            controller.update();
        }
        fruitController.update();
    }
}