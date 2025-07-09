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
import com.example.model.tile.Tile;
import com.example.model.tile.TileMap;
import com.example.model.tile.TileType;
import com.example.utils.SoundManager;
import com.example.view.GameLoop;

public class FruitController {

    public Pacman pacman;
    public Fruit fruit;
    public int timerTarget;
    public int timer;
    public double totalWeight;
    public List<int[]> fruitPositions = new ArrayList<>();

    public FruitController(Fruit fruit, Pacman pacman) {
        this.fruit = fruit;
        this.pacman = pacman;

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
        q.add(new int[]{(int) pacman.regenPos.x, (int) pacman.regenPos.y});
        while (!q.isEmpty()) {
            int[] p = q.poll();
            for (int[] d : directions) {
                int[] np = new int[]{p[0] + d[0], p[1] + d[1]};
                if (np[0] < 0 || np[0] >= TileMap.getInstance().map[0].length || np[1] < 0 || np[1] >= TileMap.getInstance().map.length) continue;
                
                Tile t = TileMap.getInstance().getTileAt(np[1], np[0]);
                if (t.type == TileType.Wall) continue;
                
                Point newPoint = new Point(np[0], np[1]);
                if (visited.contains(newPoint)) continue;
                
                if (t.type == TileType.Empty) fruitPositions.add(np);
                visited.add(newPoint);
                q.add(np);
            }
        }
    }

    public void addPossiblePosition(int x, int y) {
        fruitPositions.add(new int[]{x, y});
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
