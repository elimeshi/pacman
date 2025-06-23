package com.example.controller;

import java.util.Queue;

import com.example.model.entity.Pacman;
import com.example.model.entity.enemy.Ghost;
import com.example.model.entity.enemy.GhostMode;
import com.example.model.tile.TileMap;

public abstract class GhostController extends EntityController {

    Pacman pacman;
    Queue<GhostMode> modes;
    Queue<Integer> durations;
    int frightenedDuration;
    GhostMode currenMode;
    int FPS;
    int modeDuration;
    int modeCounter;
    int frightenedCounter;

    public GhostController(Pacman pacman, int FPS, TileMap tileMap) {
        super(tileMap);
        this.FPS = FPS;
        this.pacman = pacman;
        GhostModeSchedule profile = new GhostModeSchedule();
        modes = profile.modes;
        durations = profile.durations;
        frightenedDuration = profile.frightenedDuration;
        frightenedCounter = -1;
    }

    public boolean isInPen() {
        return (x >= 11 && x <= 16 && 
                y >= 13 && y <= 15) 
                ||
               (x >= 13 && x <= 14 && 
                y >= 11 && y <= 15);
    }

    public boolean isFrightened() {
        return false;
    }

    public void setFrightened() {}

    public void updateGhostMode() {}

    public boolean collisionWithPacman(Ghost ghost) {
        return (Math.abs(pacman.x - ghost.x) < 0.5 && 
                Math.abs(pacman.y - ghost.y) < 0.5);
    }

    public void getNextMode() {
        if (modes.isEmpty()) return;
        currenMode = modes.poll();
        modeDuration = durations.poll() * FPS;
        modeCounter = 0;
    }

    public double snapToHalf(double pos) {
        return Math.round(pos * 2) / 2.0;
    }

    public double snapIfClose(double pos) {
        double threshold = speed / 2.0 + EPSILON;
        double nearest = isInPen() ? snapToHalf(pos) : Math.round(pos);
        return Math.abs(pos - nearest) <= threshold ? nearest : pos;
    }

    public void update() {super.update();};
}
