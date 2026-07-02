package com.game.ui;

import com.game.database.DatabaseManager;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private JPanel mainContainer;
    private CardLayout cardLayout;
    private SettingsPanel settingsPanel;
    private GamePanel gamePanel;

    public MainFrame(){
        DatabaseManager.initializeDatabase();


        setTitle("chicken invaders");
        setSize(600,800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);

        mainContainer.add(new LoginPanel(this),"LOGIN");
        mainContainer.add(new MainMenuPanel(this),"MENU");
        gamePanel = new GamePanel(this);
        mainContainer.add(gamePanel, "GAME");
        settingsPanel = new SettingsPanel(this);
        mainContainer.add(settingsPanel, "SETTINGS");

        add(mainContainer);
        setVisible(true);
    }

    public void showPage(String pageName){
        if (settingsPanel != null) {
            settingsPanel.loadSettingsFromDB();
        }
        cardLayout.show(mainContainer,pageName);

        if (pageName.equals("GAME")) {
            gamePanel.requestFocusInWindow();
        }
    }
}
