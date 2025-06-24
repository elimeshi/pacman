package com.example.model;

public class Speeds {
    public static double pacman, ghostNormal, frightened, eaten;

    public static void setSpeeds(int fps) {;
        pacman =      6.0 / fps;
        ghostNormal=  4.5 / fps;
        frightened =  3.0 / fps;
        eaten =       9.0 / fps;
    }
}
