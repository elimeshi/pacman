package com.example.model;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

public class Message {
    public static final String EMPTY = "";
    public static final String READY =     "Get ready!";
    public static final String GAME_OVER = "Game Over!";
    public static final String VICTORY =   "You won!!!";
    public static final String PAUSED =    "  Paused  ";
    List<String> VALUES = Arrays.asList("EMPTY", EMPTY, "READY", READY, "GAME_OVER", GAME_OVER, "VICTORY", VICTORY, "PAUSED", PAUSED);

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
            case EMPTY:
                message = EMPTY;
                break;
            case "READY":
            case READY:
                message = READY;
                color = Color.YELLOW;
                break;
            case "GAME_OVER":
            case GAME_OVER:
                message = GAME_OVER;
                color = Color.RED;
                break;
            case "VICTORY":
            case VICTORY:
                message = VICTORY;
                color = Color.GREEN;
                break;
            case "PAUSED":
            case PAUSED:
                message = PAUSED;
                color = Color.GRAY;
                break;
            default:
                break;
        }
    }

    public void writeInputMessage(String input) {
        message += input;
    }

    public void deleteInputMessage() {
        if (message.isEmpty()) return;
        message = message.substring(0, message.length() - 1);
    }

    public String getMessage() {
        return message;
    }

    public Color getColor() {
        return color;
    }
}
