package com.example.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.example.model.entity.Pacman;

public class KeyHandler implements KeyListener {

    public Pacman pacman;

    public void setPacman(Pacman pacman) { this.pacman = pacman; }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        int nextDirection = 0;
        switch (code) {
            case KeyEvent.VK_UP:
                nextDirection = 90;
                break;
            case KeyEvent.VK_DOWN:
                nextDirection = -90;
                break;
            case KeyEvent.VK_RIGHT:
                nextDirection = 0;
                break;
            case KeyEvent.VK_LEFT:
                nextDirection = 180;
                break;
        }

        pacman.nextDirection = nextDirection;
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    
}
