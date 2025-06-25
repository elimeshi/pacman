package com.example.controller;

import java.awt.geom.Point2D;
import java.util.Queue;

import com.example.model.Speeds;
import com.example.model.entity.Pacman;
import com.example.model.entity.enemy.AI;
import com.example.model.entity.enemy.Ghost;
import com.example.model.entity.enemy.GhostMode;
import com.example.model.tile.TileMap;

public abstract class GhostController extends EntityController {

    Ghost ghost;
    Pacman pacman;
    AI ai;
    Queue<GhostMode> modes;
    Queue<Integer> durations;
    int frightenedDuration;
    GhostMode currenMode;
    int FPS;
    int modeDuration;
    int modeCounter;
    int frightenedCounter;
    Point2D.Double scatterTile;
    Point2D.Double ghostPenGate;

    public GhostController(Ghost ghost, Pacman pacman, AI ai, int FPS, TileMap tileMap) {
        super(ghost, tileMap);
        this.ghost = ghost;
        this.pacman = pacman;
        this.ai = ai;
        this.FPS = FPS;
        GhostModeSchedule profile = new GhostModeSchedule();
        modes = profile.modes;
        durations = profile.durations;
        frightenedDuration = profile.frightenedDuration;
        frightenedCounter = -1;
        currenMode = null;
        this.ghostPenGate = new Point2D.Double(13, 11);
    }

    public Point2D.Double getPacmanTile() {
        return new Point2D.Double(pacman.x, pacman.y);
    }

    public Point2D.Double targetTile() {
        if (ghost.mode == GhostMode.Scatter) return scatterTile;
        if (ghost.mode == GhostMode.Eaten) return ghostPenGate;
        return null;
    }

    public boolean isInPen() {
        return (ghost.x >= 11 && ghost.x <= 16 && 
                ghost.y >= 13 && ghost.y <= 15) 
                ||
               (ghost.x >= 13 && ghost.x <= 14 && 
                ghost.y >= 11 && ghost.y <= 15);
    }

    public void setFrightened() {
        if (ghost.mode == GhostMode.Eaten) return;
        if (ghost.mode == GhostMode.Chase || ghost.mode == GhostMode.Scatter) ghost.setMode(GhostMode.Frightened);
        ghost.setFrightened();
        frightenedCounter = 0;
        ghost.setSpeed(Speeds.frightened);
    }

    public void getOutOfPen() {
        if (currenMode != null) return;
        getNextMode();
        ghost.setMode(GhostMode.Spawn);
    }

    public void updateGhostMode() {
        if (ghost.isFrightened) {
            
            if (++frightenedCounter >= frightenedDuration * FPS) {
                ghost.setFrightenedOff();
                ghost.setSpeed(Speeds.ghostNormal);
            } else if (frightenedDuration * FPS - frightenedCounter <= 2 * FPS) {
                ghost.frightenedIsOver = true;
            } 
        }
        if (ghost.mode == GhostMode.Frightened) {
            if (!ghost.isFrightened) {
                ghost.setMode(currenMode); return;
            }
            if (collisionWithPacman()) {
                ghost.setFrightenedOff();
                pacman.addPoints(200);
                ghost.setMode(GhostMode.Eaten);
                ghost.setSpeed(Speeds.eaten);
            }
        } else if (ghost.mode == GhostMode.Eaten) {
            if (ghost.x == 13.5 && ghost.y == 11) {
                ghost.setDirection(-90);
            } else if (ghost.y == ghost.regenPos.y && ghost.x == ghost.regenPos.x) {
                ghost.setMode(GhostMode.Spawn);
                ghost.setSpeed(Speeds.ghostNormal);
            }
        } else if (collisionWithPacman()) {
            pacman.die();
        } else if (ghost.mode == GhostMode.Spawn) {
            if (ghost.y == 11) {
                if (ghost.isFrightened) { ghost.setMode(GhostMode.Frightened); return; }
                ghost.setMode(currenMode);
                ghost.setDirection(ai.getDirectionToTarget(ghost, targetTile()));
            } else {
                ghost.setDirection(ai.getDirectionIfSpawn(ghost));
            } 
        } else if (ghost.mode != GhostMode.InPen) {
            if (++modeCounter >= modeDuration) getNextMode();
            ghost.setMode(currenMode);
        }
    }

    public void updateGhostDirection() {
        switch (ghost.mode) {
            case Chase:
            case Scatter:
                if (!isOnTile()) return;
                ghost.setDirection(ai.getDirectionToTarget(ghost, targetTile()));
                break;
            case Frightened:
                if (!isOnTile() && !isInPen()) return;
                ghost.setDirection(ai.getDirectionIfFrightened(ghost));
                break;
            case Eaten:
                if (!ghost.isInPen()) {
                    if (!isOnTile()) return;
                    ghost.setDirection(ai.getDirectionToTarget(ghost, ghostPenGate)); return;
                }

                if (ghost.y == 11 && ghost.x != 13.5) {
                    ghost.setDirection(ghost.x < 13.5 ? 0 : 180); return;
                } else if (ghost.y < ghost.regenPos.y) {
                    ghost.setDirection(-90); return;
                } else if (ghost.x != ghost.regenPos.x) {
                    ghost.setDirection(ghost.x < ghost.regenPos.x ? 0 : 180);
                }
                break;
            case Spawn:
                ghost.setDirection(ai.getDirectionIfSpawn(ghost));
                break;
            case InPen:
                ghost.setDirection(ai.getDirectionInPen(ghost));
            default:
                break;
        }
    }

    public boolean collisionWithPacman() {
        return (Math.abs(pacman.x - ghost.x) < 0.8 && 
                Math.abs(pacman.y - ghost.y) < 0.8);
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
        double threshold = ghost.speed / 2.0 + EPSILON;
        double nearest = isInPen() ? snapToHalf(pos) : Math.round(pos);
        return Math.abs(pos - nearest) <= threshold ? nearest : pos;
    }

    public void update() {
        updateGhostMode();
        updateGhostDirection();
        ghost.setSprite();
        super.update();
    };
}
