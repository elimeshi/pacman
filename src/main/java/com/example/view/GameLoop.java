package com.example.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.example.config.GameConfig;
import com.example.controller.AI;
import com.example.controller.GameController;
import com.example.controller.KeyHandler;
import com.example.model.Speeds;
import com.example.model.entity.Pacman;
import com.example.model.entity.enemy.*;
import com.example.model.fruit.Fruit;
import com.example.model.tile.TileMap;
import com.example.utils.AssetLoader;

public class GameLoop {

    GameConfig cfg;
    KeyHandler keyH;
    TileMap tileMap;
    int tileSize;
    int scale;
    Pacman pacman;
    Ghost[] ghosts;
    Fruit fruit;
    GameController controller;
    AI ai;

    int points = 0;

    public GameLoop(GameConfig cfg, KeyHandler keyH) {
        this.cfg = cfg;
        this.keyH = keyH;
        this.tileSize = GameConfig.tileSize;
        Speeds.setSpeeds(cfg.FPS);
        scale = tileSize / 3;
        tileMap = new TileMap(7);
        pacman = new Pacman(13.5, 23.0, Speeds.pacman);
        keyH.setPacman(pacman);
        ghosts = new Ghost[]{
            new Blinky(13.5, 11, Speeds.ghostNormal),
            new Pinky (13.5, 14, Speeds.ghostNormal),
            new Inky  (11.5, 14, Speeds.ghostNormal),
            new Clyde (15.5, 14, Speeds.ghostNormal)
        };
        ai = new AI(pacman, (Blinky) ghosts[0], tileMap);
        fruit = new Fruit();
        controller = new GameController(pacman, (Blinky) ghosts[0], (Pinky) ghosts[1], (Inky) ghosts[2], (Clyde) ghosts[3], ai, fruit, tileMap, cfg.FPS);
    }

    public void update() {
        if (pacman.gameOver) return;

        if (pacman.dead) {
            pacman.updateDeath();
            return;
        }

        controller.update();
    }

    public void drawTileMap(Graphics2D g2) {
        for (int i = 0; i < tileMap.mapHeight(); i++) {
            for (int j = 0; j < tileMap.mapWidth(); j++) {
                g2.drawImage(tileMap.getTileAt(i, j).image, 
                             j * tileSize, 
                             i * tileSize, 
                             tileSize, 
                             tileSize, 
                             null);
            }
        }
    } 

    public void drawFruit(Graphics2D g2) {
        if (fruit.isVisible)
        g2.drawImage(fruit.type.getImage(), 
                     fruit.x * tileSize - scale, 
                     fruit.y * tileSize - scale, 
                     tileSize + scale * 2, 
                     tileSize + scale * 2, 
                     null);
    }

    public void drawPacman(Graphics2D g2) {
        g2.setColor(Color.YELLOW);
        g2.fillArc((int) (pacman.x * tileSize - scale / 2), 
                   (int) (pacman.y * tileSize - scale / 2), 
                   tileSize + scale, 
                   tileSize + scale, 
                   pacman.mouthDegrees + pacman.direction, 
                   360 - pacman.mouthDegrees * 2);
    }

    public void drawSprite(Graphics2D g2, BufferedImage img, double x, double y, int size, int scale) {
        g2.drawImage(img, (int) (x * size - scale),
                          (int) (y * size - scale),
                          size + scale * 2,
                          size + scale * 2,
                          null);
    }

    public void drawGhosts(Graphics2D g2) {
        for (Ghost ghost : ghosts) {
            double x = ghost.x, y = ghost.y;
            if (ghost.mode != GhostMode.Eaten) drawSprite(g2, ghost.sprite, x, y, tileSize, scale);
            if (!ghost.isFrightened) drawSprite(g2, ghost.eyes, x, y, tileSize, scale);
        }
    }

    public void drawPoints(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.setFont(AssetLoader.loadFont("Emulogic-zrEw", (float) (tileSize * 0.6)));
        g2.drawString("Points", tileSize, (int) (cfg.WINDOW_HEIGHT - tileSize * 1.2));
        g2.drawString(String.valueOf(pacman.points), (int) (tileSize * 2), (int) (cfg.WINDOW_HEIGHT - tileSize * 0.3));
    } 

    public void drawPacmanLife(Graphics2D g2) {
        g2.setColor(Color.yellow);
        for (int i = 0; i < pacman.life; i++) {
            g2.fillArc(tileSize * (6 + i * 2), 
                       (int) (cfg.WINDOW_HEIGHT - tileSize * 1.8), 
                       (int) (tileSize * 1.6), 
                       (int) (tileSize * 1.6), 
                       45, 270);
        }
    }

    public void draw(Graphics2D g2) {
        drawTileMap(g2);
        drawFruit(g2);
        drawPacman(g2);
        drawGhosts(g2);
        drawPoints(g2);
        drawPacmanLife(g2);
    }

}
