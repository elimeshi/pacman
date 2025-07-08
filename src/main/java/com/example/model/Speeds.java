package com.example.model;

import com.example.config.GameConfig;

public class Speeds {
    public static double pacman, ghostNormal, elroy, frightened, eaten;

    public static void setSpeeds() {;
        pacman =      6.0 / GameConfig.FPS;
        ghostNormal = 4.5 / GameConfig.FPS;
        elroy =       5.5 / GameConfig.FPS;
        frightened =  3.0 / GameConfig.FPS;
        eaten =       9.0 / GameConfig.FPS;
    }

    public static void upgradeElroySpeed() { 
        elroy =       6.5 / GameConfig.FPS;
    }
}
