package com.game.ui;

import javax.swing.*;
import java.awt.*;

public class HowToPlayPanel extends JPanel {

    public HowToPlayPanel(MainFrame frame){

        setLayout(new BorderLayout());
        setBackground(new Color(20,40,60));
        JLabel title = new JLabel("HOW TO PLAY", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(Color.CYAN);
        title.setBorder(BorderFactory.createEmptyBorder(20,0,20,0));
        add(title, BorderLayout.NORTH);

        // متن راهنما
        JTextArea helpText = new JTextArea();

        helpText.setEditable(false);
        helpText.setFocusable(false);
        helpText.setBackground(new Color(20,40,60));
        helpText.setForeground(Color.WHITE);
        helpText.setFont(new Font("Monospaced", Font.PLAIN, 16));

        helpText.setText("""
               MISSION
               Destroy all enemy chickens and survive all 8 levels.

               CONTROLS
               ← → ↑ ↓ / W A S D   Move spaceship
               SPACE               Shoot
               P                   Pause game
               ESC                 Return to menu

               POWER UPS

               🔥 Add Shot
               Increase the number of bullets.

               ⚡ Rapid Fire
               Shoot much faster for a few seconds.

               ❤️ Extra Life
               Restore one life.
 
               🛡️ Shield
               Protects you from enemy attacks.

               ❄️ Freeze Bomb
               Freezes enemies temporarily.


               ENEMIES

               • Normal Chicken
               • Shooter Chicken
               • Boss (Level 4 & 8)

               TIPS

               • Collect power-ups.
               • Avoid enemy bullets and eggs.
               • Defeat the bosses to finish the game.
        """);

        JScrollPane scrollPane = new JScrollPane(helpText);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        // دکمه برگشت
        JButton backBtn = new JButton("Back To Menu");
        backBtn.setPreferredSize(new Dimension(200,40));
        backBtn.setFont(new Font("Monospaced", Font.BOLD, 20));
        backBtn.setBackground(new Color(40, 70, 100));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setBorder(BorderFactory.createLineBorder(new Color(100, 150, 200), 2));

        backBtn.addActionListener(e->frame.showPage("MENU"));

        JPanel bottom = new JPanel();
        bottom.setBackground(new Color(20,40,60));
        bottom.add(backBtn);

        add(bottom, BorderLayout.SOUTH);
    }
}
