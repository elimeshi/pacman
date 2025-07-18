package com.example.controller;

import java.awt.geom.Point2D;

import com.example.model.entity.Pacman;
import com.example.model.entity.enemy.Ghost;
import com.example.model.entity.enemy.GhostMode;
import com.example.model.entity.enemy.Pinky;

public class PinkyController extends GhostController {

    Ghost pinky;

    public PinkyController(Pinky pinky, Pacman pacman, AI ai) {
        super(pinky, pacman, ai);
        this.pinky = pinky;
        scatterTile = new Point2D.Double(2, -4);
    }

    public Point2D.Double targetTile() {
        if (pinky.mode == GhostMode.Chase) {
            Point2D.Double pacmanTile = getPacmanTile();
            switch (ai.getPacmanDirection()) {
                case 0:   pacmanTile.x += 4; break;
                case -90: pacmanTile.y += 4; break;
                case 90:  pacmanTile.y -= 4;
                case 180: pacmanTile.x -= 4; break;
            }
            return pacmanTile;
        }
        return super.targetTile();
    }
}
