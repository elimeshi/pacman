package com.example.controller;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import com.example.model.entity.Pacman;
import com.example.model.entity.enemy.Blinky;
import com.example.model.entity.enemy.Ghost;
import com.example.model.entity.enemy.GhostMode;
import com.example.model.tile.TileMap;
import com.example.model.tile.TileType;
import com.example.view.GameLoop;

public class AI {

    Pacman pacman;
    Blinky blinky;
    double tileX, tileY;
    int direction;
    GhostMode mode;
    Point2D.Double ghostPenGate;

    final int[] DIRECTIONS = {90, 180, -90, 0};
    final int[][] DIR_OFFSETS = {
        {-1, 0}, // Up
        {0, -1}, // Left
        {1, 0},  // Down
        {0, 1}   // Right
    };

    public AI(Pacman pacman, Blinky blinky) {
        this.pacman = pacman;
        this.blinky = blinky;
        this.ghostPenGate = new Point2D.Double(13, 11);
    }

    public void updateGlobalVariables(Ghost ghost) {
        tileX = ghost.x;
        tileY = ghost.y;
        direction = ghost.direction;
        mode = ghost.mode;
    }

    public Point2D.Double getBlinkyTile() {
        return new Point2D.Double(blinky.x, blinky.y);
    }

    public int getPacmanDirection() {
        return pacman.direction;
    }

    public int getReverseDirection(int dir) {
        if (dir == 0) return 180;
        if (dir == 90) return -90;
        if (dir == -90) return 90;
        if (dir == 180) return 0;
        return 0; // fallback
    }

    public int[] getPossibleDirections() {
        int reverse = getReverseDirection(direction);
        List<Integer> result = new ArrayList<>(3); // max 3 directions since reverse is excluded

        for (int i = 0; i < 4; i++) {
            int dir = DIRECTIONS[i];
            if (dir == reverse) continue; // Skip reverse

            if (dir == 90 && 
                tileX <= 16 && 
                tileX >= 11 && 
               (tileY == 11 || 
                (tileY == 23 && pacman.regenPos.y == 23))) 
                    continue; // skip going up in the forbidden intersections 

            int new_x = (int) tileX + DIR_OFFSETS[i][1];
            int new_y = (int) tileY + DIR_OFFSETS[i][0];
            new_x = (new_x + 28) % 28;

            if (TileMap.getInstance().getTileAt(new_y, new_x).type != TileType.Wall) 
                result.add(dir);
        }

        // Convert list to array
        return result.stream().mapToInt(i -> i).toArray();
    }

    public int[] filterByTarget(int[] directions, Point2D.Double target) {
        List<Integer> filtered = new ArrayList<>();
        double minDistance = Double.MAX_VALUE;

        for (int dir : directions) {
            int nx = (int) tileX, ny = (int) tileY;

            switch (dir) {
                case 90:   ny -= 1; break; // Up
                case 180:  nx -= 1; break; // Left
                case -90:  ny += 1; break; // Down
                case 0:    nx += 1; break; // Right
            }

            double dist = target.distance(nx, ny); // Euclidean distance

            if (dist < minDistance) {
                filtered.clear();
                filtered.add(dir);
                minDistance = dist;
            } else if (dist == minDistance) {
                filtered.add(dir);
            }
        }

        return filtered.stream().mapToInt(i -> i).toArray();
    }

    public int getDirectionInPen(Ghost ghost) {
        updateGlobalVariables(ghost);
        if (tileY <= TileMap.getInstance().ghostPenCenter().y - 0.5) return -90; // move down
        if (tileY >= TileMap.getInstance().ghostPenCenter().y + 0.5) return  90; // move up
        return direction;
    }

    public int getDirectionIfSpawn(Ghost ghost) {
        if (ghost.x != 13.5) return ghost.x < 13.5 ? 0 : 180;
        return 90;
    }

    public int getDirectionToTarget(Ghost ghost, Point2D.Double target) {
        updateGlobalVariables(ghost);
        int[] directions = getPossibleDirections();
        return filterByTarget(directions, target)[0];
    }

    public int getDirectionIfFrightened(Ghost ghost) {
        if (ghost.isInPen() && ghost.mode != GhostMode.Frightened) return getDirectionInPen(ghost);
        updateGlobalVariables(ghost);
        int[] directions = getPossibleDirections();
        return directions[GameLoop.nextInt(directions.length)];
    }
}
