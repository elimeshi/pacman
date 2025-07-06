package com.example.view;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.SwingWorker;

public class GameExporter extends SwingWorker<Void, Void> {

    GameLoop gameLoop;

    public GameExporter(GameLoop gameLoop) {
        this.gameLoop = gameLoop;
    }

    @Override
    protected Void doInBackground() throws Exception {
        while (gameLoop.exportMode) {
            if (gameLoop.gameState == GameState.RUN) {
                gameLoop.updateGame();
                gameLoop.debug();
                GameLoop.frame++;

                if (GameLoop.frame % 3 == 0) {
                    BufferedImage image = new BufferedImage(gameLoop.cfg.WINDOW_WIDTH, gameLoop.cfg.WINDOW_HEIGHT, BufferedImage.TYPE_INT_RGB);
                    Graphics2D g2 = (Graphics2D) image.getGraphics();
                    gameLoop.drawer.drawGame(g2);
                    g2.dispose();
                    try {
                        ImageIO.write(image, "png", new File(String.format("output/frame_%05d.png", GameLoop.frame / 3)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }        
        return null;
    }

    public void exportToMP4() {
        try {
            String videoName = gameLoop.savedGames.get(gameLoop.savedGameIndex)[0] + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".mp4";
            ProcessBuilder builder = new ProcessBuilder(
                "ffmpeg", "-y", 
                "-framerate", "10",
                "-i", "output/frame_%05d.png",
                "-c:v", "libx264",
                "-pix_fmt", "yuv420p",
                "output/" + videoName);
            builder.redirectErrorStream(true);
            Process process = builder.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }
            int exitCode = process.waitFor();
            System.out.println(exitCode != 0 ? "ffmpeg failed to convert to mp4, exit code " + exitCode : "video converted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteImages() {
        Path path = Paths.get("output");

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path, "frame_*.png")) {
            stream.forEach(img -> {
                try {
                    Files.delete(img);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void done() {
        exportToMP4();
        deleteImages();
        gameLoop.onExportFinished();
    }
    
}
