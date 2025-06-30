package com.example.controller;

import java.awt.geom.Point2D;
import java.util.Queue;

import com.example.model.Speeds;
import com.example.model.entity.Pacman;
import com.example.model.entity.enemy.Ghost;
import com.example.model.entity.enemy.GhostMode;
import com.example.model.entity.enemy.Pinky;
import com.example.model.tile.TileMap;
import com.example.utils.SoundManager;

public abstract class GhostController extends EntityController {

    Ghost ghost;
    Pacman pacman;
    AI ai;
    GhostModeSchedule profile;
    Queue<GhostMode> modes;
    Queue<Integer> durations;
    GhostMode currentMode;
    boolean restarted;
    SoundManager soundManager;
    int FPS;
    int modeDuration;
    int modeCounter;
    int frightenedDuration;
    int frightenedCounter;
    int inPenDuration;
    int inPenCounter;
    Point2D.Double scatterTile;
    Point2D.Double ghostPenGate;

    public GhostController(Ghost ghost, Pacman pacman, AI ai, int FPS, TileMap tileMap, SoundManager soundManager) {
        super(ghost, tileMap);
        this.ghost = ghost;
        this.pacman = pacman;
        this.ai = ai;
        this.FPS = FPS;
        profile = new GhostModeSchedule();
        modes = profile.modes;
        durations = profile.durations;
        frightenedDuration = profile.frightenedDuration * FPS;
        inPenDuration = profile.inPenAfterEatenDuration * FPS;
        frightenedCounter = -1;
        inPenCounter = -1;
        currentMode = null;
        restarted = false;
        this.ghostPenGate = new Point2D.Double(13, 11);
        this.soundManager = soundManager;
    }

    public void initialize() {
        ghost.initialize();
        profile = new GhostModeSchedule();
        modes = profile.modes;
        durations = profile.durations;
        frightenedDuration = profile.frightenedDuration * FPS;
        inPenDuration = profile.inPenAfterEatenDuration * FPS;
        frightenedCounter = -1;
        inPenCounter = -1;
        currentMode = null;
        restarted = false;
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

    public void restart() {
        ghost.restart();
        restarted = true;
        setInPen();
        inPenCounter = -1;
    }

    public void setFrightened() {
        if (ghost.mode == GhostMode.Eaten) return;
        if (ghost.mode == GhostMode.Chase || ghost.mode == GhostMode.Scatter) ghost.setMode(GhostMode.Frightened);
        ghost.setFrightened();
        frightenedCounter = 0;
        ghost.setSpeed(Speeds.frightened);
    }

    public void getOutOfPen() {
        if (currentMode != null && !restarted) return;
        restarted = false;
        getNextMode();
        ghost.setMode(GhostMode.Spawn);
    }

    public void setInPen() {
        inPenCounter = 0;
        ghost.setMode(GhostMode.InPen);
        ghost.setDirection(ghost instanceof Pinky ? -90 : 90);
    }

    public void updateFrightened() {
        if (!ghost.isFrightened) return;

        if (++frightenedCounter >= frightenedDuration) {
            ghost.setFrightenedOff();
            ghost.setSpeed(Speeds.ghostNormal);
        } else if (frightenedDuration - frightenedCounter <= 2 * FPS) {
            ghost.frightenedIsOver = true;
        } 
    }

    public void updateFrightenedMode() {
        if (!ghost.isFrightened) {
            ghost.setMode(currentMode); return;
        }
        if (collisionWithPacman()) {
            soundManager.play("ghost eaten");
            ghost.setFrightenedOff();
            pacman.addPoints(200);
            ghost.setMode(GhostMode.Eaten);
            ghost.setSpeed(Speeds.eaten);
        }
    }

    public void updateEatenMode() {
        if (ghost.x == 13.5 && ghost.y == 11) {
            ghost.setDirection(-90);
        } else if (ghost.y == ghost.regenPos.y && ghost.x == ghost.regenPos.x) {
            setInPen();
            ghost.setSpeed(Speeds.ghostNormal);
        }
    }

    public void updateSpawnMode() {
        if (ghost.y == 11) {
            if (ghost.isFrightened) { ghost.setMode(GhostMode.Frightened); return; }
            ghost.setMode(currentMode);
            ghost.setDirection(ai.getDirectionToTarget(ghost, targetTile()));
        } else {
            ghost.setDirection(ai.getDirectionIfSpawn(ghost));
        } 
    }

    public void updateInPenMode() {
        if (inPenCounter < 0) return;
        if (++inPenCounter >= inPenDuration) ghost.setMode(GhostMode.Spawn);
    }

    public void updateChaseAndScatterMode() {
        if (++modeCounter >= modeDuration) getNextMode();
        ghost.setMode(currentMode);
    }

    public void updateGhostMode() {
        updateFrightened();
        if (ghost.mode == GhostMode.Frightened) {
            updateFrightenedMode();
        } else if (ghost.mode == GhostMode.Eaten) {
            updateEatenMode();
        } else if (ghost.mode == GhostMode.Spawn) {
            updateSpawnMode();
        } else if (ghost.mode == GhostMode.InPen) {
            updateInPenMode();
        } else {
            updateChaseAndScatterMode();
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

    public void killPacman() {
        if (ghost.mode != GhostMode.Frightened && ghost.mode != GhostMode.Eaten && collisionWithPacman()) {
            pacman.die();
        } 
    }

    public void getNextMode() {
        if (modes.isEmpty()) return;
        currentMode = modes.poll();
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
        killPacman();
        updateGhostMode();
        updateGhostDirection();
        ghost.setSprite();
        super.update();
    };
}
