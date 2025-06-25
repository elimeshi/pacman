package com.example.model.fruit;

import java.awt.image.BufferedImage;

import com.example.utils.AssetLoader;

public enum FruitType {
    CHERRY    (AssetLoader.loadSprite("fruits/cherry.png"),     100, 0.45),
    STRAWBERRY(AssetLoader.loadSprite("fruits/strawberry.png"), 300, 0.25),
    ORANGE    (AssetLoader.loadSprite("fruits/orange.png"),     500, 0.15),
    APPLE     (AssetLoader.loadSprite("fruits/apple.png"),      700, 0.1),
    MELON     (AssetLoader.loadSprite("fruits/watermelon.png"),1000, 0.05);

    private BufferedImage image;
    private int points;
    private double weight;

    FruitType(BufferedImage image, int points, double weight) {
        this.image = image;
        this.points = points;
        this.weight = weight;
    }

    public BufferedImage getImage() {
        return image;
    }

    public int getPoints() {
        return points;
    }

    public double getWeight() {
        return weight;
    }
}
