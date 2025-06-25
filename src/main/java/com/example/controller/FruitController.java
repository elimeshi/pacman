package com.example.controller;

import java.util.Random;

import com.example.model.entity.Pacman;
import com.example.model.fruit.Fruit;
import com.example.model.fruit.FruitType;

public class FruitController {
    public Pacman pacman;
    public Fruit fruit;
    public int fps;
    public int timerTarget;
    public int timer;

    public FruitController(Fruit fruit, Pacman pacman, int fps) {
        this.fruit = fruit;
        this.pacman = pacman;
        this.fps = fps;
        setTimerForNextFruit();
    }

    public void setTimerForNextFruit() {
        Random random = new Random();
        timerTarget = random.nextInt(10) * fps;
        timer = 0;
    }

    public void createFruit() {
        fruit.setNewFruit(13, 17, FruitType.CHERRY, 6 * fps);
        timer = 0;
    }

    public void removeFruit() {
        fruit.disappear();
        setTimerForNextFruit();
    }

    public void eatFruit() {
        removeFruit();
        pacman.addPoints(fruit.type.getPoints());
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
