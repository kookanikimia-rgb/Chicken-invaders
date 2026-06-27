package com.game.ui;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    public GamePanel(MainFrame frame){
        setBackground(Color.BLACK);
        setLayout(new BorderLayout());

        JLabel loadingLabel = new JLabel("Game Screen - Press ESC to Quit (Soon)", SwingConstants.CENTER);
        loadingLabel.setForeground(Color.green);
        loadingLabel.setFont(new Font("Consolas",Font.ITALIC,20));

        add(loadingLabel,BorderLayout.CENTER);

    }
}
