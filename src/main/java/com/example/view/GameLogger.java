package com.example.view;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.stream.Stream;

public class GameLogger {
    
    private TreeMap<Integer, String> leaderboards = new TreeMap<>();
    private List<Entry<String, FileTime>> savedGames = new ArrayList<>();
    private List<GameFrame> gameFrames = new ArrayList<>();
    private long randomSeed;

    public GameLogger(long randomSeed) {
        this.randomSeed = randomSeed;
    }

    public void addFrame(int frame, int pacmanInput) {
        gameFrames.add(new GameFrame(frame, pacmanInput));
    }

    public void saveGame(String fileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("saved games/" + fileName + ".ser"))) {
            oos.writeLong(randomSeed);
            oos.writeObject(gameFrames);
            gameFrames.clear();
            System.out.println("Game serialized successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void loadGame(String fileName) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("saved games/" + fileName + ".ser"))) {
            randomSeed = ois.readLong();
            gameFrames = (List<GameFrame>) ois.readObject(); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadSavedGamesList() {
        savedGames.clear();
        try (Stream<Path> files = Files.list(Paths.get("saved games"))) {
            files.filter(Files::isRegularFile).forEach(file -> {
                try {
                    BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class);
                    savedGames.add(Map.entry(removeExtension(file.getFileName().toString()), attr.creationTime()));
                } catch (Exception e) {
                    System.out.println("Error loading attributes for " + file);
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            System.out.println("Error finding directory \"saved games\"");
            e.printStackTrace();
        }
        savedGames.sort((a, b) -> b.getValue().compareTo(a.getValue()));
    }

    public String removeExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return dotIndex > 0 ? fileName.substring(0, dotIndex) : fileName;
    }

    public List<GameFrame> getLogs() { return gameFrames; }    
    
    public long getSeed() { return randomSeed; }

    @SuppressWarnings("unchecked")
    public void LoadLeaderboards() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("leaderboards.ser"))) {
            leaderboards = (TreeMap<Integer, String>) ois.readObject(); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveLeaderboards() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("leaderboards.ser"))) {
            oos.writeObject(leaderboards);
            System.out.println("Leaderboards serialized successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TreeMap<Integer, String> getLeaderboards() {
        return leaderboards;
    }

    public List<Entry<String, FileTime>> getSavedGames() {
        return savedGames;
    }
}
