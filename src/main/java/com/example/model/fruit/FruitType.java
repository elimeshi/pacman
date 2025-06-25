package com.example.model.fruit;

import java.awt.image.BufferedImage;

import com.example.utils.AssetLoader;

public enum FruitType {
    CHERRY(AssetLoader.loadSprite("fruits/cherry.png"), 100),
    STRAWBERRY(AssetLoader.loadSprite("fruits/strawberry.png"), 100),
    ORANGE(AssetLoader.loadSprite("fruits/orange.png"), 100),
    APPLE(AssetLoader.loadSprite("fruits/apple.png"), 100),
    MELON(AssetLoader.loadSprite("fruits/watermelon.png"), 100);

    private BufferedImage image;
    private int points;

    FruitType(BufferedImage image, int points) {
        this.image = image;
        this.points = points;
    }

    public BufferedImage getImage() {
        return image;
    }

    public int getPoints() {
        return points;
    }
}
