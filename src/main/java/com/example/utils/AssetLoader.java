package com.example.utils;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AssetLoader {
    public static BufferedImage loadSprite(String filename) {
        try (InputStream in = ResourceUtil.getResourceAsStream("/images/" + filename)) {
            return ImageIO.read(in);
        } catch (Exception e) {
            throw new RuntimeException("Couldn't load sprite: " + filename, e);
        }
    }

    public static String loadLevel(int level) {
        try (InputStream in = ResourceUtil.getResourceAsStream("/levels/level" + level + ".txt")) {
            return new String(in.readAllBytes());
        } catch (Exception e) {
            throw new RuntimeException("Couldn't load level: " + level + ".txt", e);
        }
    }

    public static JsonNode loadJSON(String fileName) {
        try (InputStream in = ResourceUtil.getResourceAsStream("/config/ghostConfig/" + fileName + ".json")) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readTree(in.readAllBytes());
        } catch (Exception e) {
            throw new RuntimeException("Couldn't load level: " + fileName + ".json", e);
        }
    }

    public static Font loadFont(String fontFileName, float size) {
        try (InputStream in = ResourceUtil.getResourceAsStream("/fonts/" + fontFileName + ".ttf")) {
            Font font = Font.createFont(Font.TRUETYPE_FONT, in).deriveFont(size);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
            return font;
        } catch (Exception e) {
            throw new RuntimeException("Couldn't load sprite: " + fontFileName + ".ttf", e);
        }
    }
    

    public static Clip loadClip(String fileName) {
        try {
            return ResourceUtil.getResourceAsClip("/music/" + fileName + ".wav");
        } catch (Exception e) {
            throw new RuntimeException("Couldn't load sprite: " + fileName + ".wav", e);
        }
    }
}
