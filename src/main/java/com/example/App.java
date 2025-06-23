package com.example;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import com.example.view.GamePanel;

public class App {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);

        GamePanel gp = new GamePanel();        
        window.add(gp);
        window.pack();
        window.setIconImage(new ImageIcon(gp.getClass().getResource("/images/pacman.png")).getImage());
        window.setLocationRelativeTo(null);
        window.setTitle("Pacman");
        window.setVisible(true);
        
        gp.startGameThread();
    }
}
