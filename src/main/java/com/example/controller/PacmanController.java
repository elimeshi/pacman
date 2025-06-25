package com.example.controller;

import com.example.model.entity.Pacman;
import com.example.model.tile.Tile;
import com.example.model.tile.TileMap;
import com.example.model.tile.TileType;

public class PacmanController extends EntityController {

    public Pacman pacman;

    public PacmanController(Pacman pacman, TileMap tileMap) {
        super(pacman, tileMap);
        this.pacman = pacman;
    }

    public void updateDirection() {
        if ((pacman.direction - pacman.nextDirection) % 180 == 0) { // if next direction is back from the current direction
            pacman.direction = pacman.nextDirection;
        } else if (pacman.nextDirection != pacman.direction && isOnTile() && getNextTile(pacman.nextDirection).type != TileType.Wall) 
            pacman.direction = pacman.nextDirection;
    }

    public TileType collectPellet() {
        int ix = (int) pacman.x, iy = (int) pacman.y;
        Tile currentTile = tileMap.getTileAt(iy, ix);

        switch (currentTile.type) {
            case Dot: pacman.addPoints(10);; break;
            case Energizer: pacman.addPoints(50);; break;
            default: break;
        }

        tileMap.setTileAt(iy, ix, '≡');
        return currentTile.type;
    }

    public void update() {
        pacman.updateMouth();
        updateDirection();
        super.update();
    }
}
