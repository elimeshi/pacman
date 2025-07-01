package com.example.view;

import java.awt.Graphics2D;
import java.util.Random;

import javax.swing.Timer;

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
import com.example.utils.SoundManager;

public class GameLoop {

    GameConfig cfg;
    public GameState gameState;
    public static long seed = System.currentTimeMillis();
    public static Random random = new Random(seed);
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
    public int commandNum = 0;

    int frame = 0;
    public GameLogger gameLogger;
    SoundManager soundManager;

    public GameLoop(GameConfig cfg, KeyHandler keyH) {
        this.cfg = cfg;
        this.keyH = keyH;
        this.tileSize = cfg.tileSize;
        level = 1;
        Speeds.setSpeeds(cfg.FPS);
        gameState = GameState.START_MENU;
        message = new Message();
        scale = tileSize / 3;
        tileMap = new TileMap(1);
        pacman = new Pacman(13.5, 23.0, Speeds.pacman);
        soundManager = new SoundManager();
        soundManager.play("pick");
        keyH.setPacman(pacman);
        keyH.setGameLoop(this);
        keyH.setSoundManager(soundManager);
        ghosts = new Ghost[]{
            new Blinky(13.5, 11, Speeds.ghostNormal),
            new Pinky (13.5, 14, Speeds.ghostNormal),
            new Inky  (11.5, 14, Speeds.ghostNormal),
            new Clyde (15.5, 14, Speeds.ghostNormal)
        };
        ai = new AI(pacman, (Blinky) ghosts[0], tileMap);
        fruit = new Fruit();
        
        controller = new GameController(pacman, ghosts, ai, fruit, tileMap, cfg.FPS, soundManager);
        drawer = new Drawer(cfg, tileMap, pacman, ghosts, fruit, message, scale);
        
        gameLogger = new GameLogger(seed);
        
    }

    public void runMenuCommand() {
        if (gameState == GameState.START_MENU) {
            switch (commandNum) {
                case 0:
                    startGame();
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    System.exit(0);
                default:
                    break;
            }
        } else if (gameState == GameState.POST_MENU) {
            switch (commandNum) {
                case 0:
                    gameLogger.saveToFile();
                    break;
                case 1:
                    level = 1;
                    tileMap.loadMap(level);
                    tileMap.loadTiles(level);
                    controller.initializeNewGame();
                    startGame();
                    break;
                case 2:
                    break;
                case 3:
                    System.exit(0);
                default:
                    break;
            }
        }
    }

    public void startGame() {
        gameState = GameState.TRANSIENT_PAUSE;
        message.setMessage("READY");
        soundManager.play("pacman intro");
        Timer startGameTimer = new Timer(5000, e -> { 
                frame = 0; 
                gameState = GameState.RUN; 
                message.setMessage("EMPTY"); 
                soundManager.loopStart("background");
        });
        startGameTimer.setRepeats(false);
        startGameTimer.start();
    }

    public void nextLevel() {
        if (++level > 3) {
            message.setMessage("VICTORY");
            return;
        } 
        pacman.life++;
        tileMap.loadMap(level);
        tileMap.loadTiles(level);
        controller.initializeNextLevel();
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
            soundManager.stop("background");
            gameState = GameState.TRANSIENT_PAUSE;
            Timer timer = new Timer(3000, e -> { 
                gameState = GameState.POST_MENU;
            });
            timer.setRepeats(false);
            timer.start();
            soundManager.play(controller.victory ? "victory" : "game over");
            return;
        } 

        if (pacman.deadNow) {
            System.out.println("dead now");
            gameState = GameState.TRANSIENT_PAUSE;
            Timer timer = new Timer(1000, e -> { 
                gameState = GameState.RUN;
                controller.pacmanIsDead();
            });
            timer.setRepeats(false);
            timer.start();
            controller.pacmanIsDead();
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
            case TRANSIENT_PAUSE:
                break;
            case RUN:
                updateGame();
                gameLogger.addFrame(frame++, pacman.nextDirection);
                break;
            case POST_MENU:
                break;
        }
    }

    public void draw(Graphics2D g2) {
        drawer.draw(g2, gameState, commandNum);
    }
}
