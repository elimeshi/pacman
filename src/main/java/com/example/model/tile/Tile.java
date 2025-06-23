package com.example.model.tile;

import java.awt.image.BufferedImage;

public class Tile {
    public TileType type;
    public BufferedImage image;

    public Tile(BufferedImage image, TileType type) {
        this.image = image;
        this.type = type;
    }

}
