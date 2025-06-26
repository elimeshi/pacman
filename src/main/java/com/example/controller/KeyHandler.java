package com.example.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.example.model.entity.Pacman;
import com.example.view.GameLoop;

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
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    
}
