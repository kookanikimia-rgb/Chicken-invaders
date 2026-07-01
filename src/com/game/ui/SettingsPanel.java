package com.game.ui;

import com.game.database.DatabaseManager;
import com.game.entities.UserSession;

import javax.swing.*;
import java.awt.*;

public class SettingsPanel extends JPanel {
    private JCheckBox bgMusic;
    private JCheckBox shotSound;
    private JCheckBox crashSound;
    private JCheckBox gameOverSound;
    public SettingsPanel(MainFrame frame){

        setLayout(new GridBagLayout());
        setBackground(new Color(20,40,60));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.gridx = 0;

        JLabel title = new JLabel("GAME SETTINGS");
        title.setForeground(Color.CYAN);
        title.setFont(new Font("Arial",Font.BOLD,24));
        gbc.gridy = 0;
        add(title,gbc);

        gbc.gridy = 1;
        add(Box.createRigidArea(new Dimension(0, 20)), gbc);

        bgMusic = new JCheckBox("Music Background");
        shotSound = new JCheckBox("Gun Shot");
        crashSound = new JCheckBox("Afar Explosion");
        gameOverSound = new JCheckBox("game over");

        JCheckBox[] checkBoxes = {bgMusic,shotSound,crashSound,gameOverSound};
        for (JCheckBox cb : checkBoxes) {
            cb.setForeground(Color.CYAN);
            cb.setBackground(new Color(20, 40, 60));
            cb.setOpaque(true);
            cb.setFocusPainted(false);
            cb.setFont(new Font("Arial", Font.BOLD, 16));
            cb.setAlignmentX(Component.LEFT_ALIGNMENT);

        }

        gbc.gridy = 2; add(bgMusic,gbc);
        gbc.gridy = 3; add(shotSound,gbc);
        gbc.gridy = 4; add(crashSound,gbc);
        gbc.gridy = 5; add(gameOverSound,gbc);

        gbc.gridy = 6;
        add(Box.createRigidArea(new Dimension(0, 20)), gbc);

        JButton saveBtn = new JButton("Save Settings");
        saveBtn.setPreferredSize(new Dimension(200,40));
        saveBtn.setFont(new Font("Monospaced", Font.BOLD, 20));
        saveBtn.setBackground(new Color(40, 70, 100));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);
        saveBtn.setBorder(BorderFactory.createLineBorder(new Color(100, 150, 200), 2));
        gbc.gridy = 7;
        add(saveBtn,gbc);

        JButton backBtn = new JButton("Back To Menu");
        backBtn.setPreferredSize(new Dimension(200,40));
        backBtn.setFont(new Font("Monospaced", Font.BOLD, 20));
        backBtn.setBackground(new Color(40, 70, 100));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setBorder(BorderFactory.createLineBorder(new Color(100, 150, 200), 2));
        gbc.gridy = 8;
        add(backBtn,gbc);

        saveBtn.addActionListener(e->{
            String currentUserName = UserSession.getUserName();
            if(currentUserName != null){
                DatabaseManager.updateSoundSetting(currentUserName,"bg_music",bgMusic.isSelected());
                DatabaseManager.updateSoundSetting(currentUserName,"shot_sound",shotSound.isSelected());
                DatabaseManager.updateSoundSetting(currentUserName,"crash_sound",crashSound.isSelected());
                DatabaseManager.updateSoundSetting(currentUserName,"game_over_sound",gameOverSound.isSelected());

                JOptionPane.showMessageDialog(this, "Settings saved successfully for " + currentUserName + "!");
            }else {
                JOptionPane.showMessageDialog(this, "Error: No active user session found!");
            }
        });

        backBtn.addActionListener(e -> frame.showPage("MENU"));
    }

    public  void loadSettingsFromDB() {
        String currentUserName = UserSession.getUserName();
        if (currentUserName != null) {
            bgMusic.setSelected(DatabaseManager.getSoundSetting(currentUserName, "bg_music"));
            shotSound.setSelected(DatabaseManager.getSoundSetting(currentUserName, "shot_sound"));
            crashSound.setSelected(DatabaseManager.getSoundSetting(currentUserName, "crash_sound"));
            gameOverSound.setSelected(DatabaseManager.getSoundSetting(currentUserName, "game_over_sound"));
        } else {

            bgMusic.setSelected(true);
            shotSound.setSelected(true);
            crashSound.setSelected(true);
            gameOverSound.setSelected(true);
        }
    }
}
