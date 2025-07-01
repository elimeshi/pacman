package com.example.utils;

import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.Clip;

public class SoundManager {
    Clip clip;
    Map<String, Clip> clips;

    public SoundManager() {
        clips = new HashMap<String,Clip>();
        for (String file : new String[]{"pick", "coin", "energizer", "ghost eaten", "fruit eaten", "pacman intro", "background", "victory", "game over"})
            clips.put(file, AssetLoader.loadClip(file));

        for (Clip clip : clips.values()) {
            clip.start(); clip.stop(); clip.setFramePosition(0);
        }
    }

    public void play(String file) {
        clip = clips.get(file);
        clip.setFramePosition(0);
        clip.start();
    }

    public void loopStart(String file) {
        clip = clips.get(file);
        if (clip.isRunning()) clip.stop();
        clip.setFramePosition(0);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        clip.start();
    }

    public void stop(String file) {
        clip = clips.get(file);
        clip.stop();
        clip.setFramePosition(0);
    }
}
