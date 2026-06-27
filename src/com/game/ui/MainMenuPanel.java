package com.game.ui;

import javax.swing.*;
import java.awt.*;

public class MainMenuPanel extends JPanel {
    public MainMenuPanel(MainFrame frame){
        setLayout(new GridLayout(6,1,10,20));
        setBackground(Color.BLACK);
        setBorder(BorderFactory.createEmptyBorder(50,150,50,150));

        String[] buttons = {"New Game","High Scores","Settings","How To Play","Exit"};

        for (String text : buttons){
            JButton btn = new JButton(text);
            setFocusable(false);
            btn.setFont(new Font("Monospaced",Font.BOLD,18));

            btn.addActionListener(e -> {
                switch (text){
                    case "New Game" -> frame.showPage("GAME");
                    case "Settings" ->frame.showPage("SETTINGS");
                    case "Exit"->System.exit(0);
                    default -> JOptionPane.showMessageDialog(this, "بزودی پیاده‌سازی می‌شود: " + text);
                }
            });
            add(btn);
        }
    }
}
