package com.example.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sound.sampled.Clip;

public class SoundManager {
    private static SoundManager instance = null;
    private Clip clip;
    private Map<String, Clip> clips;
    private List<Clip> runningClips = new ArrayList<>();

    private SoundManager() {
        clips = new HashMap<String,Clip>();
        for (String file : new String[]{"pick", "coin", "energizer", "ghost eaten", "fruit eaten", "pacman intro", "background", "pacman death", "levelup", "victory", "game over"})
            clips.put(file, AssetLoader.loadClip(file));

        for (Clip clip : clips.values()) {
            clip.start(); clip.stop(); clip.setFramePosition(0);
        }
    }

    public static SoundManager getInstance() {
        if (instance == null) instance = new SoundManager();
        return instance;
    }

    public void play(String file) {
        clip = clips.get(file);
        clip.setFramePosition(0);
        clip.start();
    }

    public void startBackground() {
        clip = clips.get("background");
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

    public void pauseAll() {
        runningClips.clear();
        for (Clip c : clips.values()) if (c.isRunning()) {
            c.stop();
            runningClips.add(c);
        } 
    }

    public void continueAll() {
        for (Clip c : runningClips) c.start();
    }
}
