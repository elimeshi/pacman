package com.example.controller;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import com.example.config.GameConfig;
import com.example.model.entity.Pacman;
import com.example.model.fruit.Fruit;
import com.example.model.fruit.FruitType;
import com.example.model.tile.TileMap;
import com.example.utils.SoundManager;
import com.example.view.GameLoop;

public class FruitController {

    public Pacman pacman;
    public Fruit fruit;
    public int timerTarget;
    public int timer;
    public double totalWeight;
    public List<int[]> fruitPositions = new ArrayList<>();
    public TileMap tileMap;

    public FruitController(Fruit fruit, Pacman pacman, TileMap tileMap) {
        this.fruit = fruit;
        this.pacman = pacman;
        this.tileMap = tileMap;

        totalWeight = 0;
        for (FruitType type : FruitType.values()) totalWeight += type.getWeight();
    }

    public void initialize() {
        fruit.disappear();
        setTimerForNextFruit();
        getPossiblePositions();
    }

    public void getPossiblePositions() {
        fruitPositions.clear();
        int[][] directions = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        Queue<int[]> q = new LinkedList<>();
        Set<Point> visited = new HashSet<>();
        q.add(new int[]{13, 23});
        while (!q.isEmpty()) {
            int[] p = q.poll();
            for (int[] d : directions) {
                int[] np = new int[]{p[0] + d[0], p[1] + d[1]};
                if (np[0] < 0 || np[0] >= tileMap.map[0].length || np[1] < 0 || np[1] >= tileMap.map.length) continue;
                
                char nc = tileMap.map[np[1]][np[0]];
                if (!(nc == '≡' || nc == '·')) continue;
                
                Point newPoint = new Point(np[0], np[1]);
                if (visited.contains(newPoint)) continue;
                
                if (nc == '≡') fruitPositions.add(np);
                visited.add(newPoint);
                q.add(np);
            }
        }
    }

    public void addPossiblePosition(int[] pos) {
        fruitPositions.add(pos);
    }

    public FruitType chooseRandomFruitType() {
        double randomWeight = GameLoop.nextDouble() * totalWeight;
        double currentWeight = 0;

        for (FruitType type : FruitType.values()) {
            currentWeight += type.getWeight();
            if (currentWeight >= randomWeight) return type;
        }

        return FruitType.CHERRY;
    }

    public int getDuration(FruitType type) {
        return (int) (GameConfig.FPS * (7 - type.getPoints() / 300.0));
    }

    public void setTimerForNextFruit() {
        timerTarget = GameLoop.nextInt(10) + 5 * GameConfig.FPS;
        timer = 0;
    }

    public void createFruit() {
        FruitType type = chooseRandomFruitType();
        int[] position = fruitPositions.get(GameLoop.nextInt(fruitPositions.size()));
        fruit.setNewFruit(position[0], position[1], type, getDuration(type));
        timer = 0;
    }

    public void removeFruit() {
        fruit.disappear();
        setTimerForNextFruit();
    }

    public void eatFruit() {
        removeFruit();
        pacman.addPoints(fruit.type.getPoints());
        SoundManager.getInstance().play("fruit eaten");
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
