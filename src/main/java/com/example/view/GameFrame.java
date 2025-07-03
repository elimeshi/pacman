package com.example.view;

import java.io.Serializable;

public class GameFrame implements Serializable {

    private static final long serialVersionUID = -8408412111876360630L;

    int frame, pacmanNextDirection;
    public GameFrame(int frame, int pacmanNextDirection) {
        this.frame = frame;
        this.pacmanNextDirection = pacmanNextDirection;
    }

    @Override
    public String toString() { return "GameFrame(" + frame + ", " + pacmanNextDirection + ")"; }
}
