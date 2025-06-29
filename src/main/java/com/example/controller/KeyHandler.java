package com.example.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.example.model.entity.Pacman;
import com.example.view.GameLoop;
import com.example.view.GameState;

public class KeyHandler implements KeyListener {

    public Pacman pacman;
    public GameLoop gameLoop;

    public void setPacman(Pacman pacman) { 
        this.pacman = pacman;
    }

    public void setGameLoop(GameLoop gameLoop) {
        this.gameLoop = gameLoop;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (gameLoop.gameState == GameState.START_MENU || gameLoop.gameState == GameState.POST_MENU) {
            switch (code) {
                case KeyEvent.VK_UP:
                    gameLoop.commandNum = (gameLoop.commandNum + 3) % 4;
                    break;
                case KeyEvent.VK_DOWN:
                    gameLoop.commandNum = (gameLoop.commandNum + 5) % 4;
                    break;
                case KeyEvent.VK_ENTER:
                    gameLoop.runMenuCommand();
                    break;
            }
        } else if (gameLoop.gameState == GameState.RUN) {
            switch (code) {
                case KeyEvent.VK_UP:
                    pacman.nextDirection = 90;
                    break;
                case KeyEvent.VK_DOWN:
                    pacman.nextDirection = -90;
                    break;
                case KeyEvent.VK_RIGHT:
                    pacman.nextDirection = 0;
                    break;
                case KeyEvent.VK_LEFT:
                    pacman.nextDirection = 180;
                    break;
                case KeyEvent.VK_SPACE:
                    gameLoop.pauseGame();
                    break;
            }
        } else if (gameLoop.gameState == GameState.PAUSED) {
            if (code == KeyEvent.VK_SPACE) gameLoop.pauseGame();
        }
        
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    
}
