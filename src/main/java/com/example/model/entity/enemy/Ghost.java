package com.example.model.entity.enemy;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import com.example.model.Speeds;
import com.example.model.entity.Entity;
import com.example.model.tile.TileMap;
import com.example.utils.*;

public class Ghost extends Entity {

    public GhostMode mode;
    public boolean isFrightened;
    public boolean frightenedIsOver = false;
    public int spriteIdx = 0;
    public int spriteCounter = 0;
    public int frightenedSpriteIdx = 0;
    public int frightenedSpriteCounter = 0;
    public BufferedImage sprite, eyes;

    public Point penTopLeft, penBottomRight;
    public Point2D.Double penCenter;

    public Ghost(double x, double y, double speed) {
        super(x, y, speed);
        setSprite();
    }

    public void initialize() {
        penTopLeft = TileMap.getInstance().ghostPenTopLeftCorner();
        penBottomRight = TileMap.getInstance().ghostPenBottomRightCorner();
        penCenter = TileMap.getInstance().ghostPenCenter();
        
        setRegenPos();
        restart();
        mode = GhostMode.InPen;
        speed = Speeds.ghostNormal;
        isFrightened = false;
        frightenedIsOver = false;
        spriteIdx = 0;
        spriteCounter = 0;
        frightenedSpriteIdx = 0;
        frightenedSpriteCounter = 0;
        direction = 90;
        setSprite();
    }

    public void restart() {
        x = regenPos.x;
        y = regenPos.y;
        direction = 90;
    }

    public void setRegenPos() { regenPos = new Point2D.Double(penCenter.x, penCenter.y); }

    public boolean isInPen() {
        return (x >= penTopLeft.x && 
                x <= penBottomRight.x && 
                y >= penTopLeft.y && 
                y <= penBottomRight.y)
                ||
               (Math.abs(x - penCenter.x) <= 0.5 && 
                y >= penTopLeft.y - 1 && y <= penBottomRight.y);
    }

    public boolean isOnPenTile() {
        return isOnTileOffset(x, 0.5) &&
               isOnTileOffset(y, 0);
    }

    public double snapToHalf(double pos) {
        return Math.round(pos * 2) / 2.0;
    }

    @Override
    public double snapIfClose(double pos) {
        double threshold = speed / 2.0 + EPSILON;
        double nearest = isInPen() ? snapToHalf(pos) : Math.round(pos);
        return Math.abs(pos - nearest) <= threshold ? nearest : pos;
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
