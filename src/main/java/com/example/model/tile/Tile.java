package com.example.model.tile;

import java.awt.image.BufferedImage;
import java.io.Serializable;

public class Tile implements Serializable {
    public TileType type;
    public BufferedImage image;

    public Tile(BufferedImage image, TileType type) {
        this.image = image;
        this.type = type;
    }

}
