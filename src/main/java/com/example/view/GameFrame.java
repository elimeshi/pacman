package com.example.view;

import java.io.Serializable;

public class GameFrame implements Serializable {
    int frame, pacmanNextDirection;
    public GameFrame(int frame, int pacmanNextDirection) {
        this.frame = frame;
        this.pacmanNextDirection = pacmanNextDirection;
    }
}
