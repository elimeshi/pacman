package com.example.controller;

import com.example.model.entity.Pacman;
import com.example.model.tile.Tile;
import com.example.model.tile.TileMap;
import com.example.model.tile.TileType;

public class PacmanController extends EntityController {

    public Pacman pacman;
    public int points;

    public PacmanController(Pacman pacman, TileMap tileMap) {
        super(pacman, tileMap);
        this.pacman = pacman;
    }

    public TileType collectPellet() {
        int ix = (int) pacman.x, iy = (int) pacman.y;
        Tile currentTile = tileMap.getTileAt(iy, ix);

        switch (currentTile.type) {
            case Dot: points += 10; break;
            case Energizer: points += 50; break;
            default: break;
        }

        tileMap.setTileAt(iy, ix, '≡');
        return currentTile.type;
    }

    public void update() {
        super.update();
    }
}
