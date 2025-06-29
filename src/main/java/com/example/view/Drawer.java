package com.example.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.example.config.GameConfig;
import com.example.model.Message;
import com.example.model.entity.Pacman;
import com.example.model.entity.enemy.*;
import com.example.model.fruit.Fruit;
import com.example.model.tile.TileMap;
import com.example.utils.AssetLoader;

public class Drawer {

    GameConfig cfg;
    Pacman pacman;
    Ghost[] ghosts;
    TileMap tileMap;
    Fruit fruit;
    Message message;
    int tileSize;
    int scale;

    public Drawer(GameConfig cfg, TileMap tileMap, Pacman pacman, Ghost[] ghosts, Fruit fruit, Message message, int scale) {
        this.cfg = cfg;
        this.tileMap = tileMap;
        this.pacman = pacman; 
        this.ghosts = ghosts;
        this.fruit = fruit;
        this.message = message;
        this.tileSize = cfg.tileSize;
        this.scale = scale;
    }

    public int getXForCenteredText(String text, Graphics2D g2) {
        return (cfg.WINDOW_WIDTH - (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth()) / 2;
    }

    public void drawStartMenu(Graphics2D g2, int commandNum) {
        g2.setColor(Color.WHITE);
        g2.setFont(AssetLoader.loadFont("Emulogic-zrEw", (float) (tileSize * 3)));
        String text = "Pacman";
        g2.drawString(text, getXForCenteredText(text, g2), 100);

        int x, y;
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, (float) (tileSize)));
        String[] options = new String[]{"New game", "Recorded games", "Manage control keys", "Quit"};
        for (int i = 0; i < options.length; i++) {
            x = getXForCenteredText(options[i], g2);
            y = 300 + i * tileSize * 2;
            g2.drawString(options[i], x, y);
            if (commandNum == i) {
                g2.drawString(">", x - tileSize * 2, y);
            }
        }
    }

    public void drawPostMenu(Graphics2D g2, int commandNum) {
        g2.setColor(Color.WHITE);
        g2.setFont(AssetLoader.loadFont("Emulogic-zrEw", (float) (tileSize * 3)));
        String text = message.getMessage();
        g2.drawString(text, getXForCenteredText(text, g2), 100);

        int x, y;
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, (float) (tileSize)));
        String[] options = new String[]{"Save game", "New game", "Leaderboards", "Quit"};
        for (int i = 0; i < options.length; i++) {
            x = getXForCenteredText(options[i], g2);
            y = 300 + i * tileSize * 2;
            g2.drawString(options[i], x, y);
            if (commandNum == i) {
                g2.drawString(">", x - tileSize * 2, y);
            }
        }
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

    public void drawMessage(Graphics2D g2) {
        if (message.getMessage().isEmpty()) return;
        g2.setColor(message.getColor());
        g2.setFont(AssetLoader.loadFont("Emulogic-zrEw", (float) (tileSize * 0.9)));
        g2.drawString(message.getMessage(), getXForCenteredText(message.getMessage(), g2), tileSize * 18);
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

    public void drawGame(Graphics2D g2) {
        drawTileMap(g2);
        drawFruit(g2);
        drawPacman(g2);
        drawGhosts(g2);
        drawMessage(g2);
        drawPoints(g2);
        drawPacmanLife(g2);
    }

    public void draw(Graphics2D g2, GameState state, int commandNum) {
        switch (state) {
            case START_MENU:
                drawStartMenu(g2, commandNum); break;
            
            case READY:
            case PAUSED:
            case RUN:
                drawGame(g2); break;
            
            case POST_MENU:
                drawPostMenu(g2, commandNum); break;
        }
    }
}
