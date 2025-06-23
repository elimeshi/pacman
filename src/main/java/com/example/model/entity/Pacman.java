package com.example.model.entity;

public class Pacman extends Entity {

    public boolean dead = false;
    public boolean gameOver = false;

    public Pacman(double x, double y, double speed) {
        super(x, y, speed);
        direction = 180;
        nextDirection = 180;
    }

    public int mouthDegrees = 0;
    private boolean isMouthOpening = true;
    
    public int points = 0;

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
        if (mouthDegrees >= 180) gameOver = true;
    }

    public void die() {
        direction = 90;
        mouthDegrees = 25;
        dead = true;
    }

    public void update() {
        updateMouth();
        super.update();
    }
}
