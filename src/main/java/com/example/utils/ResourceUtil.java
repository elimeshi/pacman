package com.example.utils;

import java.io.File;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class ResourceUtil {
    public static InputStream getResourceAsStream(String path) {
        InputStream in = ResourceUtil.class.getResourceAsStream(path);
        if (in == null) throw new IllegalArgumentException("Resource not found: " + path);
        return in;
    }

    public static File getResourceAsFile(String path) {
        try {
            File f = new File(ResourceUtil.class.getResource(path).toURI());
            return f;
        } catch (Exception e) {
            throw new IllegalArgumentException("Resource not found: " + path);
        }
    }

    public static Clip getResourceAsClip(String path) {
        try (AudioInputStream ais = AudioSystem.getAudioInputStream(ResourceUtil.class.getResource(path))) {
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            return clip;
        } catch (Exception e) {
            throw new IllegalArgumentException("Resource not found: " + path);
        }
    }
}
