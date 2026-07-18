package com.game.ui;

import com.game.database.DatabaseManager;
import com.game.entities.PlaneInfo;
import com.game.entities.UserSession;

import javax.swing.*;
import java.awt.*;

public class StorePanel extends JPanel {

    private MainFrame frame;

    public StorePanel(MainFrame frame){

        this.frame = frame;

        setLayout(new BorderLayout());
        setBackground(new Color(20,40,60));

        JLabel title = new JLabel("STORE",SwingConstants.CENTER);
        title.setFont(new Font("Arial",Font.BOLD,34));
        title.setForeground(Color.WHITE);

        add(title,BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setLayout(new GridLayout(4,1,10,10));
        center.setBackground(new Color(20,40,60));

        center.add(createPlanePanel("Default"));
        center.add(createPlanePanel("Fast"));
        center.add(createPlanePanel("Heavy"));
        center.add(createPlanePanel("Sniper"));

        add(center,BorderLayout.CENTER);

        JButton backBtn = new JButton("Back To Menu");
        backBtn.setPreferredSize(new Dimension(200,40));
        backBtn.setFont(new Font("Monospaced", Font.BOLD, 20));
        backBtn.setBackground(new Color(40, 70, 100));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setBorder(BorderFactory.createLineBorder(new Color(100, 150, 200), 2));

        backBtn.addActionListener(e->frame.showPage("MENU"));

        JPanel south = new JPanel();
        south.setBackground(new Color(20,40,60));
        south.add(backBtn);

        add(south,BorderLayout.SOUTH);
    }

    private JPanel createPlanePanel(String name){

        String imagePath = "";

        switch (name.toUpperCase()){

            case "DEFAULT":
                imagePath = "/Assets/images/airplan/2.png";
                break;

            case "FAST":
                imagePath = "/Assets/images/airplan/5.png";
                break;

            case "HEAVY":
                imagePath = "/Assets/images/airplan/3.png";
                break;

            case "SNIPER":
                imagePath = "/Assets/images/airplan/6.png";
                break;
        }

        ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));

        Image img = icon.getImage().getScaledInstance(
                90,     // عرض
                90,     // ارتفاع
                Image.SCALE_SMOOTH
        );

        JLabel imageLabel = new JLabel(new ImageIcon(img));
        imageLabel.setBorder(
                BorderFactory.createEmptyBorder(0,10,0,15)
        );

        JPanel panel = new JPanel(new BorderLayout(15,10));

        panel.setPreferredSize(new Dimension(700,140));

        panel.setBackground(new Color(32,42,60));

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(90,170,255),2),
                        BorderFactory.createEmptyBorder(20,20,20,20)
                )
        );

        PlaneInfo planeInfo = DatabaseManager.getPlaneInfo(name.toUpperCase());

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info,BoxLayout.Y_AXIS));

        JLabel title = new JLabel(name);

        title.setFont(new Font("Arial",Font.BOLD,22));

        title.setForeground(Color.WHITE);

        info.add(title);
        info.add(Box.createVerticalStrut(5));


        JLabel ability = new JLabel(
                planeInfo.doubleBossDamage ?
                        "Ability : Double Boss Damage"
                        :
                        "Ability : -"
        );

        ability.setForeground(Color.LIGHT_GRAY);

        JLabel speed =
                new JLabel("Speed : " + planeInfo.speed);

        JLabel fire =
                new JLabel("Fire Rate : " + planeInfo.shootDelay + " ms");

        JLabel hp =
                new JLabel("Lives : " + planeInfo.hp);

        speed.setForeground(Color.WHITE);
        fire.setForeground(Color.WHITE);
        hp.setForeground(Color.WHITE);

        JLabel priceLabel = new JLabel();

        if (planeInfo.price == 0) {
            priceLabel.setText("FREE");
            priceLabel.setForeground(new Color(100,255,120));
        } else {
            priceLabel.setText("$ " + planeInfo.price);
            priceLabel.setForeground(new Color(255,220,70));
        }

        priceLabel.setFont(new Font("Arial",Font.BOLD,18));
        info.add(Box.createVerticalStrut(8));

        info.add(priceLabel);
        info.add(Box.createVerticalStrut(10));

        info.add(ability);
        info.add(Box.createVerticalStrut(10));

        info.add(speed);
        info.add(fire);
        info.add(hp);

        JButton buy = new JButton("BUY");
        buy.setPreferredSize(new Dimension(90,35));

        buy.setFont(new Font("Arial", Font.BOLD, 12));

        buy.setBackground(new Color(10,90,170));
        buy.setForeground(Color.WHITE);
        buy.setFocusPainted(false);

        buy.addActionListener(e -> {

            String currentPlane =
                    DatabaseManager.getSelectedPlane(UserSession.getUserName());

            if (currentPlane.equals(name.toUpperCase())) {

                JOptionPane.showMessageDialog(
                        this,
                        "This plane is already selected."
                );

                return;
            }


            int score = DatabaseManager.getHighScore(UserSession.getUserName());
            int planePrice = planeInfo.price;

            if(score < planePrice){

                JOptionPane.showMessageDialog(
                        this,
                        "Not enough score!",
                        "Store",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            DatabaseManager.updateSelectedPlane(
                    UserSession.getUserName(),
                    name.toUpperCase()
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Plane purchased successfully!"
            );

            frame.showPage("MENU");
            frame.showPage("STORE");

        });

        panel.add(imageLabel, BorderLayout.WEST);
        panel.add(info, BorderLayout.CENTER);
        JPanel right = new JPanel();
        right.setOpaque(false);
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));

        right.add(Box.createVerticalGlue());
        right.add(buy);
        right.add(Box.createVerticalGlue());

        right.setBorder(BorderFactory.createEmptyBorder(0,0,0,20));

        panel.add(right, BorderLayout.EAST);

        String currentPlane = DatabaseManager.getSelectedPlane(UserSession.getUserName());

        if (currentPlane.equals(name.toUpperCase())) {
            buy.setEnabled(false);
            buy.setText("Selected");
            buy.setBackground(new Color(40,160,80));
            buy.setForeground(Color.WHITE);
        }

        switch(name.toUpperCase()){

            case "FAST":
                panel.setBorder(
                        BorderFactory.createLineBorder(
                                new Color(180,150,10),5));
                break;

            case "HEAVY":
                panel.setBorder(
                        BorderFactory.createLineBorder(
                                new Color(255,60,70),5));
                break;

            case "SNIPER":
                panel.setBorder(
                        BorderFactory.createLineBorder(
                                new Color(180,70,255),5));
                break;

            default:
                panel.setBorder(
                        BorderFactory.createLineBorder(
                                Color.GRAY,5));
        }
        return panel;
    }
}
