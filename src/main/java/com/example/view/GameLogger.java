package com.example.view;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

public class GameLogger {
    
    private List<GameFrame> gameFrames = new LinkedList<>();
    private TreeMap<Integer, String> leaderboards = new TreeMap<>();
    private long randomSeed;

    public GameLogger(long randomSeed) {
        this.randomSeed = randomSeed;
    }

    public void addFrame(int frame, int pacmanInput) {
        gameFrames.add(new GameFrame(frame, pacmanInput));
    }

    public void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("game.ser"))) {
            oos.writeLong(randomSeed);
            oos.writeObject(gameFrames);
            System.out.println("Game serialized successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void loadFromFile(String fileName) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            randomSeed = ois.readLong();
            gameFrames = (List<GameFrame>) ois.readObject(); 
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public void setLeaderboards() {
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
}
