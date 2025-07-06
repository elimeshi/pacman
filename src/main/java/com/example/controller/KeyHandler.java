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
        if (gameLoop.gameState == GameState.MENU || gameLoop.gameState == GameState.SAVED_GAMES || gameLoop.gameState == GameState.SAVED_GAME_MANAGER) {
            int num = gameLoop.gameState == GameState.MENU ?
                        gameLoop.menuOptions.length :
                        gameLoop.gameState == GameState.SAVED_GAMES ?
                        gameLoop.savedGames.size() + 1 :
                        gameLoop.savedGameMenuOptions.length + 1;
            switch (code) {
                case KeyEvent.VK_UP:
                    gameLoop.soundManager.play("pick");
                    gameLoop.commandNum = (gameLoop.commandNum + num - 1) % num;
                    break;
                case KeyEvent.VK_DOWN:
                    gameLoop.soundManager.play("pick");
                    gameLoop.commandNum = (gameLoop.commandNum + num + 1) % num;
                    break;
                case KeyEvent.VK_ENTER:
                    gameLoop.soundManager.play("pick");
                    gameLoop.runMenuCommand();
                    break;
            }
        } else if (gameLoop.gameState == GameState.RUN && !gameLoop.replayMode) {
            switch (code) {
                case KeyEvent.VK_UP: gameLoop.pendingDirection = 90; break;
                case KeyEvent.VK_DOWN: gameLoop.pendingDirection = -90; break;
                case KeyEvent.VK_RIGHT:gameLoop.pendingDirection = 0; break;
                case KeyEvent.VK_LEFT:gameLoop.pendingDirection = 180; break;
                case KeyEvent.VK_SPACE: gameLoop.pauseGame(); break;
            }
        } else if (gameLoop.gameState == GameState.PAUSED) {
            if (code == KeyEvent.VK_SPACE) gameLoop.pauseGame();
        } else if (gameLoop.gameState == GameState.UPDATE_LEADERBOARDS || gameLoop.gameState == GameState.SAVE_GAME || gameLoop.gameState == GameState.RENAME_SAVED_GAME) {
            if (code == KeyEvent.VK_BACK_SPACE) { gameLoop.message.deleteInputMessage(); return; }
            char c = e.getKeyChar();
            if(Character.isLetterOrDigit(c) || c == ' ') gameLoop.message.writeInputMessage(String.valueOf(e.getKeyChar()));
            else if (code == KeyEvent.VK_ENTER) gameLoop.saveInput();
        } else if (gameLoop.gameState == GameState.LEADERBOARDS) {
            if (code == KeyEvent.VK_ENTER) {
                gameLoop.soundManager.play("pick");
                gameLoop.closeLeaderboards();
            } 
        }
        
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    
}
