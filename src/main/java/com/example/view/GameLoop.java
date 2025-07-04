package com.example.view;

import java.awt.Graphics2D;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;
import java.util.Map.Entry;

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
    public Message message;
    public int commandNum = 0;
    public final String[] menuOptions = new String[]{"New game", "Recorded games", "Manage control keys", "Leaderboards", "Quit"};
    public final String[] savedGameMenuOptions = new String[]{"Play", "Rename", "Delete", "Export"};
    public boolean invalidInput = false;
    public List<String[]> savedGames = new ArrayList<>();
    public int savedGameIndex = 0;

    public int frame = 0;
    public GameLogger gameLogger;
    public SoundManager soundManager;

    public GameLoop(GameConfig cfg, KeyHandler keyH) {
        this.cfg = cfg;
        this.keyH = keyH;
        this.tileSize = cfg.tileSize;
        level = 1;
        Speeds.setSpeeds(cfg.FPS);
        gameState = GameState.MENU;
        message = new Message();
        scale = tileSize / 3;
        tileMap = new TileMap(1);
        pacman = new Pacman(13.5, 23.0, Speeds.pacman);
        soundManager = new SoundManager();
        soundManager.play("pick");
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
        
        controller = new GameController(pacman, ghosts, ai, fruit, tileMap, cfg.FPS, soundManager);
        drawer = new Drawer(cfg, tileMap, pacman, ghosts, fruit, message, scale);
        
        gameLogger = new GameLogger(seed);
        gameLogger.LoadLeaderboards();
        loadSavedGames();
    }

    public void runMenuCommand() {
        if (gameState == GameState.MENU) {
            switch (commandNum) {
                case 0:
                    startGame();
                    break;
                case 1:
                    commandNum = 1;
                    loadSavedGames();
                    gameState = GameState.SAVED_GAMES;
                    break;
                case 2:
                    break;
                case 3:
                    commandNum = 0;
                    gameState = GameState.LEADERBOARDS; break;
                case 4:
                    System.exit(0);
                default:
                    break;
            }
        } else if (gameState == GameState.SAVED_GAMES) {
            if (commandNum == 0) gameState = GameState.MENU;
            else {
                savedGameIndex = commandNum - 1;
                commandNum = 1;
                gameState = GameState.SAVED_GAME_MANAGER;
            }
        }
        

    }

    public void saveInput() {
        if (gameState == GameState.SAVE_GAME) saveGame();
        else if (gameState == GameState.UPDATE_LEADERBOARDS) saveLeaderboards();
    }

    public void saveLeaderboards() {
        if (message.getMessage().isBlank()) {
            invalidInput = true;
            message.setMessage(Message.EMPTY);
            return;
        }

        invalidInput = false;
        TreeMap<Integer, String> leaderboards = gameLogger.getLeaderboards();
        leaderboards.put(pacman.points, message.getMessage());
        if (leaderboards.size() > 10) leaderboards.remove(leaderboards.firstKey());
        gameLogger.saveLeaderboards();
        gameState = GameState.LEADERBOARDS;
        message.setMessage(Message.EMPTY);
    }

    public void closeLeaderboards() {
        if (message.getMessage().equals(Message.READY)) gameState = GameState.MENU;
        else gameState = GameState.SAVE_GAME;
    }

    public void loadSavedGames() {
        savedGames.clear();
        gameLogger.loadSavedGamesList();
        for (Entry<String, FileTime> fileDetails : gameLogger.getSavedGames()) {
            LocalDateTime dateAndTime = fileDetails.getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            DateTimeFormatter date = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            DateTimeFormatter time = DateTimeFormatter.ofPattern("HH:mm:ss");
            savedGames.add(new String[]{fileDetails.getKey(), dateAndTime.format(date), dateAndTime.format(time)});
        }
    }

    public void saveGame() {
        if (!message.getMessage().isBlank()) gameLogger.saveGame(message.getMessage());
        gameState = GameState.MENU;
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
        pacman.addPoints(400 + 200 * level);
        if (++level > 3) {
            pacman.addPoints(5000);
            pacman.addPoints(pacman.life * 1000);
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
                message.setMessage("EMPTY");
                TreeMap<Integer, String> leaderboards = gameLogger.getLeaderboards(); 
                gameState = leaderboards == null || 
                            leaderboards.isEmpty() || 
                            leaderboards.size() < 10 || 
                            pacman.points > leaderboards.lastKey() ?
                                                GameState.UPDATE_LEADERBOARDS : 
                                                GameState.SAVE_GAME;
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
        if (gameState != GameState.RUN) return;
        updateGame(); frame++;
    }

    public void draw(Graphics2D g2) {
        switch (gameState) {
            case MENU:                  drawer.drawMenu(g2, menuOptions, commandNum);               break;
            case LEADERBOARDS:          drawer.drawLeaderboards(g2, gameLogger.getLeaderboards());  break;
            case UPDATE_LEADERBOARDS:   drawer.drawUpdateLeaderboards(g2, invalidInput);            break;
            case SAVE_GAME:             drawer.drawSaveGame(g2);                                    break;
            case SAVED_GAMES:           drawer.drawSavedGames(g2, savedGames, commandNum);          break;
            case SAVED_GAME_MANAGER:    drawer.drawSavedGameManager(g2, 
                                            savedGames.get(savedGameIndex), 
                                            savedGameMenuOptions, 
                                            commandNum);                                            break;
            default:                    drawer.drawGame(g2);                                        break;
        }
    }
}
