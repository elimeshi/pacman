package com.example.view;

import java.awt.Graphics2D;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;
import java.util.Map.Entry;

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

    public boolean debugMode = false;
    public PrintWriter debugLog;
    public GameState gameState;
    public static long seed;
    public static Random random;
    public volatile Integer pendingDirection;
    public int deathPauseFrames;
    public int finishLevelPauseFrames;
    public int startGamePauseFrames;
    public int gameOverPauseFrames;
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

    public Message message;
    public int commandNum = 0;
    public final String[] menuOptions = new String[]{"New game", "Recorded games", "Manage control keys", "Leaderboards", "Quit"};
    public final String[] savedGameMenuOptions = new String[]{"Play", "Rename", "Delete", "Export"};
    public boolean invalidInput = false;
    public boolean replayMode = false;
    public boolean exportMode = false;
    public List<GameFrame> frames;
    public int framePointer;
    public List<String[]> savedGames = new ArrayList<>();
    public int savedGameIndex = 0;

    public static int frame;
    public GameLogger gameLogger;

    public GameLoop(KeyHandler keyH) {
        this.keyH = keyH;
        this.tileSize = GameConfig.tileSize;
        level = 1;
        Speeds.setSpeeds();
        gameState = GameState.MENU;
        message = new Message();
        scale = tileSize / 3;
        tileMap = new TileMap();
        pacman = new Pacman(13.5, 23.0, Speeds.pacman);
        SoundManager.getInstance().play("pick");
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
        
        controller = new GameController(pacman, ghosts, ai, fruit, tileMap);
        drawer = new Drawer(tileMap, pacman, ghosts, fruit, message, scale);
        
        gameLogger = new GameLogger();
        gameLogger.LoadLeaderboards();
        loadSavedGames();

        // TreeMap<Integer, String> l = gameLogger.getLeaderboards();
        // System.out.println(l);
        // l.remove(12460);
        // gameLogger.saveLeaderboards();
        // gameLogger.LoadLeaderboards();
    }

    public static int nextInt(int bound) {
        int result = random.nextInt(bound);
        System.out.println("Frame " + frame + ", bound: " + bound + ", next int: " + result);
        return result;
    }

    public static double nextDouble() {
        double result = random.nextDouble();
        System.out.println("Frame " + frame + ", next double: " + result);
        return result;
    }

    public void runMenuCommand() {
        if (gameState == GameState.MENU) {
            switch (commandNum) {
                case 0:
                    startGame();
                    break;
                case 1:
                    loadSavedGames();
                    commandNum = savedGames.isEmpty() ? 0 : 1;
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
        } else if (gameState == GameState.SAVED_GAME_MANAGER) {
            switch (commandNum) {
                case 0: gameState = GameState.SAVED_GAMES; commandNum = 1; break;
                case 1: playGame(); break;
                case 2: renameSavedGame(); break;
                case 3: deleteSavedGame(); break;
                case 4: exportSavedGame(); break;
                default: break;
            }
        }
    }

    public void saveInput() {
        if (gameState == GameState.SAVE_GAME) saveGame();
        else if (gameState == GameState.UPDATE_LEADERBOARDS) saveLeaderboards();
        else if (gameState == GameState.RENAME_SAVED_GAME) saveRenameSavedGame();
    }

    public boolean validateInput() {
        if (message.getMessage().isBlank()) {
            invalidInput = true;
            message.setMessage(Message.EMPTY);
            return false;
        }
        return true;
    }

    public void saveLeaderboards() {
        if (!validateInput()) return;

        invalidInput = false;
        TreeMap<Integer, String> leaderboards = gameLogger.getLeaderboards();
        leaderboards.put(pacman.points, message.getMessage().trim());
        if (leaderboards.size() > 10) leaderboards.remove(leaderboards.firstKey());
        gameLogger.saveLeaderboards();
        gameState = GameState.LEADERBOARDS;
        message.setMessage(Message.EMPTY);
    }

    public void closeLeaderboards() {
        if (gameLogger.getSeed() == 0) gameState = GameState.MENU;
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
        gameLogger.saveGame(message.getMessage());
        gameState = GameState.MENU;
    }

    public void playGame() {
        replayMode = true;
        gameLogger.loadGame(savedGames.get(savedGameIndex)[0]);
        frames = gameLogger.getLogs();
        seed = gameLogger.getSeed();
        random = new Random(seed);
        startGame();
        System.out.println(frames);
        framePointer = 0;
    }

    public void updateReplayFrame() {
        if (framePointer >= frames.size()) return;
        GameFrame currentFrame = frames.get(framePointer);
        if (currentFrame.frame == frame) { pacman.nextDirection = currentFrame.pacmanNextDirection; System.out.println(framePointer); framePointer++; }
    }

    public void renameSavedGame() {
        gameState = GameState.RENAME_SAVED_GAME;
        message.setMessage(Message.EMPTY);
    }

    public void saveRenameSavedGame() {
        if (!validateInput()) return;
        gameLogger.renameSavedGame(savedGames.get(savedGameIndex)[0], message.getMessage());
        loadSavedGames();
        commandNum = 1;
        gameState = GameState.SAVED_GAME_MANAGER;
    }

    public void deleteSavedGame() {
        gameLogger.deleteSavedGame(savedGames.get(savedGameIndex)[0]);
        loadSavedGames();
        commandNum = 1;
        gameState = GameState.SAVED_GAMES;
    }

    public void exportSavedGame() {
        GameExporter exporter = new GameExporter(this);
        playGame();
        exportMode = true;
        exporter.execute();
    }

    public void onExportFinished() {
        gameState = GameState.SAVED_GAME_MANAGER;
    }

    public void startGame() {
        if (debugMode) try {debugLog = new PrintWriter(new FileWriter(replayMode ? "debug_replay_mode.txt" : "debug_play_mode.txt", true)); } catch (Exception e) {e.printStackTrace();}
        level = 1;
        frame = 0;
        startGamePauseFrames = 5 * GameConfig.FPS;
        deathPauseFrames = 0;
        finishLevelPauseFrames = 0;
        gameOverPauseFrames = 0;
        if (!replayMode) {
            seed = System.currentTimeMillis();
            random = new Random(seed);
            gameLogger.startRecord(seed);
        }
        controller.initializeNewGame();
        gameState = GameState.RUN;
        message.setMessage("READY");
        SoundManager.getInstance().play("pacman intro");
    }

    public synchronized void getInput() {
        if (pendingDirection == null) return;
        pacman.nextDirection = pendingDirection;
        gameLogger.addFrame(frame, pendingDirection);
        pendingDirection = null;
    }

    public void nextLevel() {
        pacman.addPoints(400 + 200 * level);
        if (++level > 3) {
            controller.victory = true;
            pacman.addPoints(5000);
            pacman.addPoints(pacman.life * 1000);
            message.setMessage("VICTORY");
            return;
        }
        startGamePauseFrames = 2 * GameConfig.FPS;
        message.setMessage(Message.READY);
        pacman.life++;
        tileMap.loadMap(level);
        tileMap.loadTiles(level);
        controller.initializeNextLevel(level);
    }

    public void pauseGame() {
        if (gameState == GameState.RUN) {
            gameState = GameState.PAUSED;
            message.setMessage("PAUSED");
            SoundManager.getInstance().pauseAll();
        } else if (gameState == GameState.PAUSED) {
            gameState = GameState.RUN;
            message.setMessage("EMPTY");
            SoundManager.getInstance().continueAll();
        }
    }

    public void updateGame() {
        if (replayMode) updateReplayFrame(); else getInput();

        if (startGamePauseFrames > 0) {
            if (--startGamePauseFrames <= 0) {
                message.setMessage(Message.EMPTY);
                SoundManager.getInstance().startBackground();
            }
            return;
        }

        if (finishLevelPauseFrames > 0) {
            if (--finishLevelPauseFrames <= 0) nextLevel(); 
            return;
        } 

        if ((pacman.gameOver || controller.victory) && gameOverPauseFrames <= 0) {
            SoundManager.getInstance().stop("background");
            gameOverPauseFrames = 3 * GameConfig.FPS;
            SoundManager.getInstance().play(controller.victory ? "victory" : "game over");
            return;
        }

        if (gameOverPauseFrames > 0) {
            if (--gameOverPauseFrames <= 0) {
                message.setMessage("EMPTY");
                TreeMap<Integer, String> leaderboards = gameLogger.getLeaderboards();
                if (replayMode) { 
                    gameState = exportMode ? GameState.WAIT_FOR_EXPORT : GameState.SAVED_GAME_MANAGER; 
                    replayMode = false;
                    exportMode = false;
                    return;
                } 
                gameState = leaderboards == null || 
                            leaderboards.isEmpty() || 
                            leaderboards.size() < 10 || 
                            pacman.points > leaderboards.firstKey() ?
                                                GameState.UPDATE_LEADERBOARDS : 
                                                GameState.SAVE_GAME;
            }
            return;
        }

        if (pacman.deadNow) {
            deathPauseFrames = GameConfig.FPS;
            SoundManager.getInstance().stop("background");
            SoundManager.getInstance().play("pacman death");
            pacman.deadNow = false;
        }

        if (deathPauseFrames > 0) {
            if (--deathPauseFrames <= 0) {
                controller.pacmanIsDead();
            }
            return;
        }

        if (pacman.restarted) {
            message.setMessage(Message.READY);
            startGamePauseFrames = GameConfig.FPS * 2;
            pacman.restarted = false;
            return;
        }

        controller.update();
        if (pacman.gameOver) message.setMessage("GAME_OVER");
        if (controller.victory) {
            if (level == 3) {
                nextLevel(); return;
            }
            finishLevelPauseFrames = 2 * GameConfig.FPS;
            SoundManager.getInstance().stop("background");
            SoundManager.getInstance().play("levelup");
            controller.victory = false;
        }
    }

    public void debug() {
        if (debugMode) {
            debugLog.println("Frame: " + frame +", PacMan: (" + pacman.x + ", " + pacman.y + ")");
            debugLog.println("Fruit: " + fruit.type);
            debugLog.println("\n");
            debugLog.flush();
        } 
    }

    public void update() {
        if (gameState != GameState.RUN || exportMode) return;
        updateGame(); 
        debug();
        frame++;
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
            case RENAME_SAVED_GAME:     drawer.drawRenameSavedGame(g2);                             break;
            case WAIT_FOR_EXPORT:       drawer.drawWaitForExport(g2, frame);                        break;
            default:                    if (exportMode) drawer.drawWaitForExport(g2, frame);
                                        else drawer.drawGame(g2);                                   break;
        }
    }
}
