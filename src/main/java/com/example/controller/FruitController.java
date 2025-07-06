package com.example.controller;

import com.example.model.entity.Pacman;
import com.example.model.fruit.Fruit;
import com.example.model.fruit.FruitType;
import com.example.utils.SoundManager;
import com.example.view.GameLoop;

public class FruitController {
    public Pacman pacman;
    public Fruit fruit;
    public int fps;
    public int timerTarget;
    public int timer;
    public double totalWeight;
    public SoundManager soundManager;

    public FruitController(Fruit fruit, Pacman pacman, int fps, SoundManager soundManager) {
        this.fruit = fruit;
        this.pacman = pacman;
        this.fps = fps;
        this.soundManager = soundManager;

        totalWeight = 0;
        for (FruitType type : FruitType.values()) totalWeight += type.getWeight();
    }

    public FruitType chooseRandomFruitType() {
        double randomWeight = GameLoop.random.nextDouble() * totalWeight;
        double currentWeight = 0;

        for (FruitType type : FruitType.values()) {
            currentWeight += type.getWeight();
            if (currentWeight >= randomWeight) return type;
        }

        return FruitType.CHERRY;
    }

    public int getDuration(FruitType type) {
        return (int) (fps * (7 - type.getPoints() / 300.0));
    }

    public void setTimerForNextFruit() {
        timerTarget = GameLoop.random.nextInt(10) + 5 * fps;
        timer = 0;
    }

    public void createFruit() {
        FruitType type = chooseRandomFruitType();
        fruit.setNewFruit(13, 17, type, getDuration(type));
        timer = 0;
    }

    public void removeFruit() {
        fruit.disappear();
        setTimerForNextFruit();
    }

    public void eatFruit() {
        removeFruit();
        pacman.addPoints(fruit.type.getPoints());
        soundManager.play("fruit eaten");
    }

    public boolean collisionWithPacman() {
        return pacman.x == fruit.x && pacman.y == fruit.y;
    }

    public void update() {
        if (!fruit.isVisible) {
            if (++timer >= timerTarget) createFruit();
        } else {
            if (++timer >= fruit.duration) removeFruit();
            if (collisionWithPacman()) eatFruit();
        }
    }
}
