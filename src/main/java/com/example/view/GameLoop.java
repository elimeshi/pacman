package com.example.view;

import java.awt.Graphics2D;

import com.example.config.GameConfig;
import com.example.controller.AI;
import com.example.controller.GameController;
import com.example.controller.KeyHandler;
import com.example.model.Message;
import com.example.model.Speeds;
import com.example.model.entity.Pacman;
import com.example.model.entity.enemy.*;
import com.example.model.fruit.Fruit;
import com.example.model.tile.TileMap;

public class GameLoop {

    GameConfig cfg;
    GameState gameState;
    int level;
    KeyHandler keyH;
    TileMap tileMap;
    int tileSize;
    int scale;
    Pacman pacman;
    Ghost[] ghosts;
    Fruit fruit;
    GameController controller;
    Drawer drawer;
    AI ai;

    int points = 0;
    Message message;

    public GameLoop(GameConfig cfg, KeyHandler keyH) {
        this.cfg = cfg;
        this.keyH = keyH;
        this.tileSize = GameConfig.tileSize;
        level = 1;
        Speeds.setSpeeds(cfg.FPS);
        gameState = GameState.RUN;
        message = new Message();
        scale = tileSize / 3;
        tileMap = new TileMap(1);
        pacman = new Pacman(13.5, 11.0, Speeds.pacman);
        keyH.setPacman(pacman);
        keyH.setGameLoop(this);
        ghosts = new Ghost[]{
            new Blinky(13.5, 11, Speeds.ghostNormal),
            new Pinky (13.5, 14, Speeds.ghostNormal),
            new Inky  (11.5, 14, Speeds.ghostNormal),
            new Clyde (15.5, 14, Speeds.ghostNormal)
        };
        ai = new AI(pacman, (Blinky) ghosts[0], tileMap);
        fruit = new Fruit();
        controller = new GameController(pacman, (Blinky) ghosts[0], (Pinky) ghosts[1], (Inky) ghosts[2], (Clyde) ghosts[3], ai, fruit, tileMap, cfg.FPS);
        drawer = new Drawer(cfg, tileMap, pacman, ghosts, fruit, message, tileSize, scale);
    }

    public void startGame() {
        try {
            Thread.sleep(3000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        message.setMessage("EMPTY");
    }

    public void nextLevel() {
        if (++level > 3) {
            message.setMessage("VICTORY");
            return;
        } 
        pacman.life++;
        tileMap.loadMap(level);
        tileMap.loadTiles(level);
        controller.restart();
    }

    public void pauseGame() {
        if (gameState == GameState.RUN) {
            gameState = GameState.PAUSED;
            message.setMessage("PAUSED");
        } else if (gameState == GameState.PAUSED) {
            gameState = GameState.RUN;
            message.setMessage("EMPTY");
        }
    }

    public void updateGame() {
        if (pacman.gameOver || controller.victory) {
            try {
                gameState = GameState.POST_MENU;
                Thread.sleep(3000);
                System.exit(0); // Change me when game development proceeds.
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } 

        if (pacman.dead) {
            pacman.updateDeath();
            if (pacman.gameOver) message.setMessage("GAME_OVER"); 
            return;
        }

        controller.update();
        if (controller.victory) nextLevel();
    }

    public void update() {
        switch (gameState) {
            case START_MENU:
            
                break;
            case PAUSED:
                break;
            case RUN:
                updateGame();
                break;
            case POST_MENU:
                break;
        }
    }

    public void draw(Graphics2D g2) {
        drawer.draw(g2, gameState);
    }
}
