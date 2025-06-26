package com.example.model;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

public class Message {
    List<String> VALUES = Arrays.asList("EMPTY", "READY", "GAME_OVER", "VICTORY", "PAUSED");
    String EMPTY = "";
    String READY =     "Get ready!";
    String GAME_OVER = "Game Over!";
    String VICTORY =   "You won!!!";
    String PAUSED =    "  Paused  ";

    private String message;
    private Color color;

    public Message() {
        this.message = READY;
        color = Color.YELLOW;
    }

    public void setMessage(String type) {
        if (!VALUES.contains(type)) throw new IllegalArgumentException("Invalid type, types should be " + VALUES.toString());
        switch (type) {
            case "EMPTY": 
                message = EMPTY;
                break;
            case "READY":
                message = READY;
                color = Color.YELLOW;
                break;
            case "GAME_OVER":
                message = GAME_OVER;
                color = Color.RED;
                break;
            case "VICTORY":
                message = VICTORY;
                color = Color.GREEN;
                break;
            case "PAUSED":
                message = PAUSED;
                color = Color.GRAY;
                break;
            default:
                break;
        }
    }

    public String getMessage() {
        return message;
    }

    public Color getColor() {
        return color;
    }
}
