package com.example.model.entity;

import java.awt.geom.Point2D;

public class Pacman extends Entity {

    public int nextDirection;
    public int life = 3;
    public int points = 0;
    public boolean dead = false;
    public boolean deadNow = false;
    public boolean restarted = false;
    public boolean gameOver = false;
    public int mouthDegrees = 0;
    private boolean isMouthOpening = true;

    public Pacman(double x, double y, double speed) {
        super(x, y, speed);
        direction = 180;
        nextDirection = 180;
    }

    public void setRegenPos(double x, double y) { this.regenPos = new Point2D.Double(x, y); }

    public void initialize() {
        restart();
        life = 3;
        points = 0;
        gameOver = false;
        restarted = false;
    }

    public void addPoints(int num) {
        points += num;
    }

    public void restart() {
        x = regenPos.x;
        y = regenPos.y;
        direction = 180;
        nextDirection = 180;
        mouthDegrees = 0;
        isMouthOpening = true;
        dead = false;
    }
    
    public void updateMouth() {
        mouthDegrees += isMouthOpening ? 3 : -3;
        if (mouthDegrees >= 45) {
            isMouthOpening = false;
        } else if (mouthDegrees <= 0) {
            isMouthOpening = true;
        }
    }

    public void updateDeath() {
        mouthDegrees += 5;
        if (mouthDegrees >= 180) {
            life--;
            restarted = true;
            if (life < 0) gameOver = true;
            else {
                restart();
            }
        } 
    }

    public void die() {
        direction = 90;
        mouthDegrees = 25;
        dead = true;
        deadNow = true;
    }
}
