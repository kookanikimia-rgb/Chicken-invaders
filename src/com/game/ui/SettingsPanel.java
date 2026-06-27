package com.game.ui;

import javax.swing.*;
import java.awt.*;

public class SettingsPanel extends JPanel {
    public SettingsPanel(MainFrame frame){
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(50,50,50));
        setBorder(BorderFactory.createEmptyBorder(50,50,50,50));

        JLabel title = new JLabel();
        title.setForeground(Color.WHITE);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JCheckBox bgMusic = new JCheckBox("Music Background", true);
        JCheckBox shotSound = new JCheckBox("Gun Shot", true);
        JCheckBox crashSound = new JCheckBox("Afar Explosion", true);
        JCheckBox gameOverSound = new JCheckBox("game over", true);

        JButton backBtn = new JButton("Back To Menu");
        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        backBtn.addActionListener(e -> frame.showPage("MENU"));

        add(title);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(bgMusic); add(shotSound); add(crashSound); add(gameOverSound);
        add(Box.createRigidArea(new Dimension(0, 30)));
        add(backBtn);
    }
}
