package com.example.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.example.model.entity.Pacman;
import com.example.utils.SoundManager;
import com.example.view.GameLoop;
import com.example.view.GameState;

public class KeyHandler implements KeyListener {

    public Pacman pacman;
    public GameLoop gameLoop;
    private int mv_down, mv_up, mv_right, mv_left, pause;

    public int getMvDown() { return mv_down; }
    public void setMvDown(int mv_down) { this.mv_down = mv_down; }
    public int getMvUp() { return mv_up; }
    public void setMvUp(int mv_up) { this.mv_up = mv_up; }
    public int getMvRight() { return mv_right; }
    public void setMvRight(int mv_right) { this.mv_right = mv_right; }
    public int getMvLeft() { return mv_left; }
    public void setMvLeft(int mv_left) { this.mv_left = mv_left; }
    public int getPause() { return pause; }
    public void setPause(int pause) { this.pause = pause; }
    public void setPacman(Pacman pacman) { this.pacman = pacman; }
    public void setGameLoop(GameLoop gameLoop) { this.gameLoop = gameLoop; }

    public void setDefaultControlKeys() {
        mv_down = KeyEvent.VK_DOWN;
        mv_up = KeyEvent.VK_UP;
        mv_right = KeyEvent.VK_RIGHT;
        mv_left = KeyEvent.VK_LEFT;
        pause = KeyEvent.VK_SPACE;
    }

    public boolean isValidInput(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || c == ' ';
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
                    SoundManager.getInstance().play("pick");
                    gameLoop.commandNum = (gameLoop.commandNum + num - 1) % num;
                    break;
                case KeyEvent.VK_DOWN:
                    SoundManager.getInstance().play("pick");
                    gameLoop.commandNum = (gameLoop.commandNum + num + 1) % num;
                    break;
                case KeyEvent.VK_ENTER:
                    SoundManager.getInstance().play("pick");
                    gameLoop.runMenuCommand();
                    break;
            }
        } else if (gameLoop.gameState == GameState.RUN && !gameLoop.replayMode) {
            if (code == mv_up) gameLoop.pendingDirection = 90;
            else if (code == mv_down) gameLoop.pendingDirection = -90;
            else if (code == mv_right) gameLoop.pendingDirection = 0;
            else if (code == mv_left) gameLoop.pendingDirection = 180;
            else if (code == pause) gameLoop.pauseGame();
        } else if (gameLoop.gameState == GameState.PAUSED) {
            if (code == KeyEvent.VK_SPACE) gameLoop.pauseGame();
        } else if (gameLoop.gameState == GameState.UPDATE_LEADERBOARDS || gameLoop.gameState == GameState.SAVE_GAME || gameLoop.gameState == GameState.RENAME_SAVED_GAME) {
            if (code == KeyEvent.VK_BACK_SPACE) { gameLoop.message.deleteInputMessage(); return; }
            char c = e.getKeyChar();
            if(isValidInput(c)) gameLoop.message.writeInputMessage(String.valueOf(c));
            else if (code == KeyEvent.VK_ENTER) gameLoop.saveInput();
        } else if (gameLoop.gameState == GameState.LEADERBOARDS) {
            if (code == KeyEvent.VK_ENTER) {
                SoundManager.getInstance().play("pick");
                gameLoop.closeLeaderboards();
            } 
        } else if (gameLoop.gameState == GameState.MANAGE_CONTROL_KEYS) {
            if (code == KeyEvent.VK_ENTER) {
                SoundManager.getInstance().play("pick");
                gameLoop.chooseControlKey();
            } else if (code == KeyEvent.VK_UP) {

            } else if (code == KeyEvent.VK_DOWN) {
                
            } else gameLoop.setControlKey(code);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    
}
