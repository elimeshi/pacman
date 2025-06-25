package com.example.model.fruit;

public class Fruit {
    public int x, y, duration;
    public FruitType type;
    public boolean isVisible;
    
    public Fruit() {
        isVisible = false;
    }

    public void setNewFruit(int x, int y, FruitType type, int duration) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.duration = duration;
        isVisible = true;
    }

    public void disappear() {
        isVisible = false;
    }
}
