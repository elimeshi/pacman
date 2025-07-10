package com.example.model.entity.enemy;

public class Pinky extends Ghost {

    public Pinky(double x, double y, double speed) {
        super(x, y, speed);
        mode = GhostMode.InPen;
        direction = -90;
    }

    public void initialize() {
        super.initialize();
        direction = -90;
    }
}
