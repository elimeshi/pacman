package com.example.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.TreeMap;

import com.example.config.GameConfig;
import com.example.model.Message;
import com.example.model.entity.Pacman;
import com.example.model.entity.enemy.*;
import com.example.model.fruit.Fruit;
import com.example.model.tile.TileMap;
import com.example.utils.AssetLoader;

public class Drawer {

    Pacman pacman;
    Ghost[] ghosts;
    Fruit fruit;
    Message message;
    Font pacmanFont1;
    Font pacmanFont2;
    Font pacmanFont3;
    Font pacmanFont09;
    Font pacmanFont06;
    Font pacmanFont04;
    int tileSize;
    int scale;
    int gameMapX = 0;

    public Drawer(Pacman pacman, Ghost[] ghosts, Fruit fruit, Message message, int scale) {
        this.pacman = pacman; 
        this.ghosts = ghosts;
        this.fruit = fruit;
        this.message = message;
        this.tileSize = GameConfig.tileSize;
        this.scale = scale;
        pacmanFont1 = AssetLoader.loadFont("Emulogic-zrEw", (float) (tileSize));
        pacmanFont2 = pacmanFont1.deriveFont((float) tileSize * 2);
        pacmanFont3 = pacmanFont1.deriveFont((float) tileSize * 3);
        pacmanFont04 = pacmanFont1.deriveFont((float) (tileSize * 0.4));
        pacmanFont06 = pacmanFont1.deriveFont((float) (tileSize * 0.6));
        pacmanFont09 = pacmanFont1.deriveFont((float) (tileSize * 0.9));
    }

    public void updateGameMapX(int x) {
        this.gameMapX = x;
    }

    public int getXForCenteredText(String text, Graphics2D g2) {
        return (GameConfig.WINDOW_WIDTH - getTextLength(text, g2)) / 2;
    }

    public int getTextLength(String text, Graphics2D g2) {
        return (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
    }

    public void drawMenu(Graphics2D g2, String[] options, int commandNum) {
        g2.setColor(Color.WHITE);
        g2.setFont(pacmanFont3);
        String text = "Pacman";
        g2.drawString(text, getXForCenteredText(text, g2), 200);

        int x, y;
        g2.setFont(pacmanFont1);
        for (int i = 0; i < options.length; i++) {
            x = getXForCenteredText(options[i], g2);
            y = 400 + i * tileSize * 2;
            g2.drawString(options[i], x, y);
            if (commandNum == i) {
                g2.drawString(">", x - tileSize * 2, y);
            }
        }
    }

    public void drawUpdateLeaderboards(Graphics2D g2, boolean invalidInput) {
        g2.setColor(Color.green);
        g2.setFont(pacmanFont1);
        String text = "Nice work!";
        g2.drawString(text, getXForCenteredText(text, g2), 200);
        g2.setFont(pacmanFont06);
        text = "You just broke into the Top 10!";
        g2.drawString(text, getXForCenteredText(text, g2), 250);
        g2.setColor(Color.white);
        text = "Please enter your name:";
        g2.drawString(text, getXForCenteredText(text, g2), 300);

        int textFieldLength = 300;
        int textFieldX = GameConfig.WINDOW_WIDTH / 2 - textFieldLength / 2;
        g2.drawRect(textFieldX, 310, textFieldLength, tileSize);
        g2.drawString(message.getMessage(), textFieldX + 5, 310 + tileSize - 7);
        int cursorX = getTextLength(message.getMessage(), g2) + textFieldX + 5;
        g2.drawLine(cursorX, 312, cursorX, 312 + tileSize - 4);
        if (invalidInput) {
            g2.setColor(Color.RED);
            g2.setFont(pacmanFont04);
            g2.drawString("Invalid input, try again.", textFieldX, 360);
        }
    }

    public void drawLeaderboards(Graphics2D g2, TreeMap<Integer, String> leaderboards) {
        g2.setColor(Color.WHITE);
        g2.setFont(pacmanFont2);
        String text = "Leaderboards";
        g2.drawString(text, getXForCenteredText(text, g2), 200);

        int y = 400;
        g2.setFont(pacmanFont1);
        for (Integer points : leaderboards.descendingKeySet()) {
            g2.drawString(String.valueOf(points), GameConfig.WINDOW_WIDTH / 2 + 10, y);
            String name = leaderboards.get(points);
            g2.drawString(name, GameConfig.WINDOW_WIDTH / 2 - getTextLength(name, g2) - 10, y);
            y += 50;
        }
        g2.drawLine(GameConfig.WINDOW_WIDTH / 2, 360, GameConfig.WINDOW_WIDTH / 2, y - 40);
    }

    public void drawSaveGame(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.setFont(pacmanFont1);
        String text = "Do you want";
        g2.drawString(text, getXForCenteredText(text, g2), 200);
        text = "to save the game?";
        g2.drawString(text, getXForCenteredText(text, g2), 245);
        g2.setFont(pacmanFont06);
        text = "If not, press enter.";
        g2.drawString(text, getXForCenteredText(text, g2), 300);
        g2.setColor(Color.white);
        text = "If yes, name your game:";
        g2.drawString(text, getXForCenteredText(text, g2), 300 + tileSize);

        int textFieldLength = 300;
        int textFieldX = GameConfig.WINDOW_WIDTH / 2 - textFieldLength / 2;
        g2.drawRect(textFieldX, 360, textFieldLength, tileSize);
        g2.drawString(message.getMessage(), textFieldX + 5, 360 + tileSize - 7);
        int cursorX = getTextLength(message.getMessage(), g2) + textFieldX + 5;
        g2.drawLine(cursorX, 362, cursorX, 362 + tileSize - 4);
    }

    public void drawSavedGames(Graphics2D g2, List<String[]> savedGames, int commandNum) {
        g2.setColor(Color.WHITE);
        g2.setFont(pacmanFont2);
        String text = "Saved games";
        g2.drawString(text, getXForCenteredText(text, g2), 200);
        g2.setFont(pacmanFont06);
        g2.drawString("back", tileSize, tileSize);
        
        if (commandNum == 0) g2.drawString(">", 0, tileSize);
        
        int y = 400;
        int index = 0;
        for (String[] fileDetails : savedGames) {
            String name = fileDetails[0];
            g2.setFont(pacmanFont09);
            int nameX = GameConfig.WINDOW_WIDTH / 2 - getTextLength(name, g2) - 10;
            g2.drawString(name, nameX, y);
            if (commandNum == ++index) g2.drawString(">", nameX - tileSize * 2, y);
            g2.setFont(pacmanFont04);
            g2.drawString(fileDetails[1], GameConfig.WINDOW_WIDTH / 2 + 10, y - 20);
            g2.drawString(fileDetails[2], GameConfig.WINDOW_WIDTH / 2 + 10, y);
            y += 50;
        }
        g2.drawLine(GameConfig.WINDOW_WIDTH / 2, 360, GameConfig.WINDOW_WIDTH / 2, y - 40);
    }

    public void drawSavedGameManager(Graphics2D g2, String[] fileDetails, String[] options, int commandNum) {
        g2.setColor(Color.WHITE);
        g2.setFont(pacmanFont1);
        String text = fileDetails[0];
        g2.drawString(text, getXForCenteredText(text, g2), 200);
        g2.setFont(pacmanFont04);
        for (int i = 1; i <= 2; i++) {
            text = fileDetails[i];
            g2.drawString(text, getXForCenteredText(text, g2), 200 + 20 * i);
        }
        g2.setFont(pacmanFont06);
        g2.drawString("back", tileSize, tileSize);
        if (commandNum == 0) g2.drawString(">", 0, tileSize);
        
        g2.setFont(pacmanFont1);
        int x, y;
        for (int i = 0; i < options.length; i++) {
            x = getXForCenteredText(options[i], g2);
            y = 400 + i * tileSize * 2;
            g2.drawString(options[i], x, y);
            if (commandNum == i + 1) {
                g2.drawString(">", x - tileSize * 2, y);
            }
        }
    }

    public void drawRenameSavedGame(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.setFont(pacmanFont1);
        String text = "Rename your saved game:";
        g2.drawString(text, getXForCenteredText(text, g2), 300);

        g2.setFont(pacmanFont06);
        int textFieldLength = 300;
        int textFieldX = GameConfig.WINDOW_WIDTH / 2 - textFieldLength / 2;
        g2.drawRect(textFieldX, 360, textFieldLength, tileSize);
        g2.drawString(message.getMessage(), textFieldX + 5, 360 + tileSize - 7);
    }

    public void drawWaitForExport(Graphics2D g2, int frame) {
        g2.setColor(Color.WHITE);
        g2.setFont(pacmanFont1);
        String text = "Exporting" + ".".repeat((frame / 10) % 3 + 1);
        g2.drawString(text, getXForCenteredText("exporting", g2), 300);
    }

    private void drawTileMap(Graphics2D g2) {
        for (int i = 0; i < TileMap.getInstance().mapHeight(); i++) {
            for (int j = 0; j < TileMap.getInstance().mapWidth(); j++) {
                g2.drawImage(TileMap.getInstance().getTileAt(i, j).image, 
                             gameMapX + j * tileSize, 
                             i * tileSize, 
                             tileSize, 
                             tileSize, 
                             null);
            }
        }
    }

    private void drawFruit(Graphics2D g2) {
        if (fruit.isVisible)
        g2.drawImage(fruit.type.getImage(), 
                     gameMapX + (fruit.x * tileSize - scale), 
                     fruit.y * tileSize - scale, 
                     tileSize + scale * 2, 
                     tileSize + scale * 2, 
                     null);
    }

    private void drawPacman(Graphics2D g2) {
        g2.setColor(Color.YELLOW);
        g2.fillArc((int) (pacman.x * tileSize - scale / 2) + gameMapX, 
                   (int) (pacman.y * tileSize - scale / 2), 
                   tileSize + scale, 
                   tileSize + scale, 
                   pacman.mouthDegrees + pacman.direction, 
                   360 - pacman.mouthDegrees * 2);
    }

    private void drawSprite(Graphics2D g2, BufferedImage img, double x, double y, int size, int scale) {
        g2.drawImage(img, (int) (x * size - scale) + gameMapX,
                          (int) (y * size - scale),
                          size + scale * 2,
                          size + scale * 2,
                          null);
    }

    private void drawGhosts(Graphics2D g2) {
        for (Ghost ghost : ghosts) {
            double x = ghost.x, y = ghost.y;
            if (ghost.mode != GhostMode.Eaten) drawSprite(g2, ghost.sprite, x, y, tileSize, scale);
            if (!ghost.isFrightened) drawSprite(g2, ghost.eyes, x, y, tileSize, scale);
        }
    }

    private void drawMessage(Graphics2D g2) {
        if (message.getMessage().isEmpty()) return;
        g2.setColor(message.getColor());
        g2.setFont(pacmanFont09);
        g2.drawString(message.getMessage(), getXForCenteredText(message.getMessage(), g2), tileSize * 18);
    }

    private void drawPoints(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.setFont(pacmanFont06);
        g2.drawString("Points", tileSize + gameMapX, (int) (TileMap.getInstance().mapPixelHeight() + tileSize * 0.8));
        g2.drawString(String.valueOf(pacman.points), (int) (tileSize * 2) + gameMapX, (int) (TileMap.getInstance().mapPixelHeight() + tileSize * 1.7));
    } 

    private void drawPacmanLife(Graphics2D g2) {
        g2.setColor(Color.yellow);
        for (int i = 0; i < pacman.life; i++) {
            g2.fillArc(tileSize * (6 + i * 2) + gameMapX, 
                       (int) (TileMap.getInstance().mapPixelHeight() + tileSize * 0.2), 
                       (int) (tileSize * 1.6), 
                       (int) (tileSize * 1.6), 
                       45, 270);
        }
    }
    
    public void drawLevel(Graphics2D g2, int level) {
        g2.setColor(Color.WHITE);
        g2.setFont(pacmanFont1);
        g2.drawString("Level " + level, 
                      gameMapX + (TileMap.getInstance().mapWidth() - 8) * tileSize, 
                      (int) (TileMap.getInstance().mapPixelHeight() + tileSize * 1.5));
    }

    public void drawGame(Graphics2D g2, int level) {
        drawTileMap(g2);
        drawFruit(g2);
        drawPacman(g2);
        drawGhosts(g2);
        drawMessage(g2);
        drawPoints(g2);
        drawPacmanLife(g2);
        drawLevel(g2, level);
    }
}
