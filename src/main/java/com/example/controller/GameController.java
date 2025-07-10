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
    public TileType pacmanTile;
    public boolean victory = false;
    public int level;
    public int quarterDots;
    public int eatenDots = 0;
    public int ghostTimer = 0;
    public int[] blinkyElroyMode;

    public GameController(Pacman pacman, Ghost[] ghosts, AI ai, Fruit fruit) {
        this.pacman = pacman;
        pacmanController = new PacmanController(pacman);
        ghostControllers = new GhostController[]{
            new BlinkyController((Blinky) ghosts[0], pacman, ai),
            new PinkyController((Pinky) ghosts[1], pacman, ai),
            new InkyController((Inky) ghosts[2], pacman, ai),
            new ClydeController((Clyde) ghosts[3], pacman, ai),
        };
        fruitController = new FruitController(fruit, pacman);
    }

    public void initializeNewGame(int level) {
        initializeNextLevel(level);
        ghostControllers[0].ai.setForbiddenUpIntersections(level);
        pacman.initialize();
    }

    public void initializeNextLevel(int level) {
        TileMap.getInstance().loadLevel(level);
        pacman.setRegenPos(TileMap.getInstance().ghostPenCenter().x, level == 0 ? 20 : 23);
        pacman.restart();
        GhostModeSchedule.getInstance().loadModeSchedule(level);
        blinkyElroyMode = GhostModeSchedule.getInstance().loadBlinkyConfig(level);
        for (GhostController controller : ghostControllers) controller.initialize();
        fruitController.initialize();
        quarterDots = TileMap.getInstance().initialDots / 4;
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
        int remainingDots = TileMap.getInstance().initialDots - eatenDots;
        if (remainingDots < blinkyElroyMode[1]) return;
        if (remainingDots <= blinkyElroyMode[1]) ghostControllers[0].upgradeElroyMode();
        else if (remainingDots <= blinkyElroyMode[0]) ghostControllers[0].setElroyMode();
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
        if (eatenDots >= TileMap.getInstance().initialDots) victory = true;
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
            fruitController.addPossiblePosition((int) pacman.x, (int) pacman.y);
        } else if (pacmanTile == TileType.Energizer) {
            SoundManager.getInstance().play("energizer");
            fruitController.addPossiblePosition((int) pacman.x, (int) pacman.y);
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