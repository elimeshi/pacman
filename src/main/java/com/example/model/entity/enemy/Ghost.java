package com.example.model.entity.enemy;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import com.example.model.entity.Entity;
import com.example.utils.*;

public class Ghost extends Entity {

    public GhostMode mode;
    public boolean isFrightened;
    public boolean frightenedIsOver = false;
    public int spriteIdx = 0;
    public int spriteCounter = 0;
    public int frightenedCounter = 0;
    public int frightenedSpriteIdx = 0;
    public int frightenedSpriteCounter = 0;
    public Point2D.Double regenPos; // regeneration position in pixels
    public BufferedImage sprite, eyes;

    public Ghost(double x, double y, double speed) {
        super(x, y, speed);
        setSprite();
    }

    public void initialize() {
        restart();
        mode = GhostMode.InPen;
        frightenedIsOver = false;
        spriteIdx = 0;
        spriteCounter = 0;
        frightenedCounter = 0;
        frightenedSpriteIdx = 0;
        frightenedSpriteCounter = 0;
        direction = 90;
    }

    public void restart() {
        x = regenPos.x;
        y = regenPos.y;
        direction = 90;
    }

    public void updateFrightenedSpriteIdx() {
        if (!frightenedIsOver) return; 
        if (++frightenedSpriteCounter >= 10) {
            frightenedSpriteCounter = 0;
            frightenedSpriteIdx = frightenedSpriteIdx == 0 ? 1 : 0;
        }
    }

    public void updateSpriteIdx() {
        if (++spriteCounter >= 10) {
            spriteCounter = 0;
            spriteIdx = spriteIdx == 0 ? 1 : 0;
        }
    }

    public void setSprite() {
        updateSpriteIdx();

        String spritePath;
        if (isFrightened) {
            frightenedCounter++;
            updateFrightenedSpriteIdx();
            spritePath = "ghosts/frightened" + frightenedSpriteIdx + spriteIdx + ".png";
        } else {
            spritePath = "ghosts/" + getClass().getSimpleName() + spriteIdx + ".png";
        }

        sprite = AssetLoader.loadSprite(spritePath);
        eyes = AssetLoader.loadSprite("ghost eyes/" + direction + ".png");
    }

    public void setFrightened() {
        isFrightened = true;
        frightenedSpriteCounter = 30;
        frightenedSpriteIdx = 0;
        frightenedCounter = 0;
        frightenedIsOver = false;
    }

    public void setFrightenedOff() {
        isFrightened = false;
    }

    public Point2D.Double targetTile() { return new Point2D.Double(0, 0); }

    public void setMode(GhostMode newMode) {
        mode = newMode;
    }

    public void setDirection(int newDirection) {
        direction = newDirection;
    }

    public void setSpeed(double newSpeed) {
        speed = newSpeed;
    }
}
