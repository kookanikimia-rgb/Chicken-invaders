package com.game.ui;

import com.game.audio.SettingsPanel;
import com.game.audio.SoundManager;
import com.game.database.DatabaseManager;
import com.game.entities.UserSession;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private JPanel mainContainer;
    private CardLayout cardLayout;
    private SettingsPanel settingsPanel;
    private GamePanel gamePanel;
    private StorePanel storePanel;
    private HighScoresPanel highScoresPanel;

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
        mainContainer.add(new HowToPlayPanel(this),"HOW_TO_PLAY");
        highScoresPanel = new HighScoresPanel(this);
        mainContainer.add(highScoresPanel, "HIGH_SCORES");
        gamePanel = new GamePanel(this);
        mainContainer.add(gamePanel, "GAME");
        settingsPanel = new SettingsPanel(this);
        mainContainer.add(settingsPanel, "SETTINGS");
        storePanel = new StorePanel(this);
        mainContainer.add(storePanel,"STORE");

        add(mainContainer);
        setVisible(true);
    }

    public void showPage(String pageName){
        if (settingsPanel != null) {
            settingsPanel.loadSettingsFromDB();
        }
        switch (pageName) {

            case "MENU":
                SoundManager.stopEffectSound();
                SoundManager.startBackgroundMusic();
                break;

            case "GAME":
                SoundManager.startBackgroundMusic();
                break;

            case "HIGH_SCORES":
                highScoresPanel.refreshTable();
                break;

            case "STORE":
                storePanel.refresh();
                break;
        }
        cardLayout.show(mainContainer,pageName);

        if (pageName.equals("GAME")) {
            gamePanel.requestFocusInWindow();
            gamePanel.startGame();
        }
    }
}
