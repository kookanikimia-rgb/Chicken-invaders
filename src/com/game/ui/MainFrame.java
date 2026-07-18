package com.game.ui;

import com.game.audio.SettingsPanel;
import com.game.database.DatabaseManager;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private JPanel mainContainer;
    private CardLayout cardLayout;
    private SettingsPanel settingsPanel;
    private GamePanel gamePanel;
    private StorePanel storePanel;

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
        mainContainer.add(new HighScoresPanel(this),"HIGH_SCORES");
        mainContainer.add(new HowToPlayPanel(this),"HOW_TO_PLAY");
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

        if(pageName.equals("HIGH_SCORES")){

            mainContainer.remove(2);

            mainContainer.add(new HighScoresPanel(this),"HIGH_SCORES");

            revalidate();
            repaint();
        }
        if(pageName.equals("STORE")){

            mainContainer.remove(storePanel);

            storePanel = new StorePanel(this);

            mainContainer.add(storePanel,"STORE");

            revalidate();
            repaint();
        }
        cardLayout.show(mainContainer,pageName);

        if (pageName.equals("GAME")) {
            gamePanel.requestFocusInWindow();
            gamePanel.startGame();
        }
    }
}
