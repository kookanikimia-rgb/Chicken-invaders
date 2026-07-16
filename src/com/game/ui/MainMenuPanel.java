package com.game.ui;

import com.game.audio.SoundManager;

import javax.swing.*;
import java.awt.*;

public class MainMenuPanel extends JPanel {

    private Image backgroundImage;

    public MainMenuPanel(MainFrame frame) {

        try {
            backgroundImage = new ImageIcon(getClass().getResource("/Assets/images/main-menu-background.jpg")).getImage();
        } catch (Exception e) {
            System.out.println("MainMenu background image not found, using default color.");
        }

        SoundManager.startBackgroundMusic();

        setLayout(new GridBagLayout());
        setBackground(new Color(20, 40, 60));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 10, 10, 10);



        JLabel titleLabel = new JLabel("MAIN MENU");
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);


        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 40, 0);
        add(titleLabel, gbc);


        String[] buttons = {"New Game", "High Scores", "Settings", "How To Play", "Exit"};

        for (int i = 0; i < buttons.length; i++) {
            String text = buttons[i];
            JButton btn = new JButton(text);

            btn.setFont(new Font("Monospaced", Font.BOLD, 20));
            btn.setPreferredSize(new Dimension(250, 50));
            btn.setBackground(new Color(40, 70, 100));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createLineBorder(new Color(100, 150, 200), 2));

            btn.addActionListener(e -> {
                switch (text) {
                    case "New Game" -> frame.showPage("GAME");
                    case "Settings" -> frame.showPage("SETTINGS");
                    case "Exit" -> System.exit(0);
                    case "High Scores" -> frame.showPage("HIGH_SCORES");
                }
            });

            gbc.gridy = i + 1;
            gbc.insets = new Insets(10, 0, 10, 0);
            add(btn, gbc);
        }


    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {
            // عکس را به اندازه کل پنل می‌کشد
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
