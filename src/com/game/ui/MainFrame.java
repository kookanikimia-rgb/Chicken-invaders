package com.game.ui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JPanel mainContainer;
    private CardLayout cardLayout;

    public MainFrame(){
        setTitle("chicken invaders");
        setSize(600,800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainContainer = new JPanel();

        mainContainer.add(new LoginPanel(this),"LOGIN");
        mainContainer.add(new MainMenuPanel(this),"MENU");
        mainContainer.add(new GamePanel(this),"GAME");
        mainContainer.add(new SettingsPanel(this),"SETTINGS");

        add(mainContainer);
        setVisible(true);
    }

    public void showPage(String pageName){
        cardLayout.show(mainContainer,pageName);
    }
}
