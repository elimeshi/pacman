package com.example.model.entity.enemy;

public class Blinky extends Ghost {

    public Blinky(double x, double y, double speed) {
        super(x, y, speed);
        mode = GhostMode.Scatter;
    }

    public void initialize() {
        super.initialize();
        mode = GhostMode.Scatter;
        y = 11;
        direction = 0;
    }
}
