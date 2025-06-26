package com.example.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;

import javax.swing.JPanel;

import com.example.config.GameConfig;
import com.example.controller.KeyHandler;

public class GamePanel extends JPanel implements Runnable {

    GameConfig cfg = new GameConfig();
    KeyHandler keyH = new KeyHandler();
    GameLoop gameLoop = new GameLoop(cfg, keyH);
    Thread gameThread;

    public GamePanel() {
        this.setPreferredSize(new Dimension(cfg.WINDOW_WIDTH, cfg.WINDOW_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        repaint();
        gameLoop.startGame();


        double drawInterval = 1000000000 / cfg.FPS;
        double delta = 0;
        long currentTime, lastTime = System.nanoTime();

        int frameCount = 0;
        long fpsTimer = System.currentTimeMillis();

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            while (delta >= 1) {
                Toolkit.getDefaultToolkit().sync();
                update();
                repaint();
                delta--;

                frameCount++;
                if (System.currentTimeMillis() - fpsTimer >= 1000) {
                    System.out.println("FPS: " + frameCount);
                    frameCount = 0;
                    fpsTimer += 1000;
                }
            }
        }
    }

    public void update() {
        gameLoop.update();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        gameLoop.draw(g2);
        g2.dispose();
    }
}
