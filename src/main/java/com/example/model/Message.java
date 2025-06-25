package com.example.model;

import java.awt.Color;

public enum Message {
    EMPTY("", null),
    READY("Get ready!", Color.yellow),
    GAME_OVER("Game Over!", Color.red);

    private String message;
    private Color color;

    Message(String message, Color color) {
        this.message = message;
        this.color = color;
    }

    public String getMessage() {
        return message;
    }

    public Color getColor() {
        return color;
    }
}
