package com.game.ui;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    public LoginPanel(MainFrame frame){
        setLayout(new GridBagLayout());
        setBackground(new Color(30,30,30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.gridx = 0;

        JLabel title = new JLabel("Login");
        title.setForeground(Color.yellow);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridy = 0;
        add(title, gbc);

        JTextField userField = new JTextField(15);
        userField.setBorder(BorderFactory.createTitledBorder("Username"));
        gbc.gridy = 1;
        add(userField,gbc);

        JPasswordField passField = new JPasswordField(15);
        passField.setBorder(BorderFactory.createTitledBorder("Password"));
        gbc.gridy = 2;
        add(passField,gbc);

        JButton loginbtn = new JButton("Enter the Galaxy");
        gbc.gridy = 3;
        add(loginbtn,gbc);

        loginbtn.addActionListener(e -> {
            String name = userField.getText();
            String pass = new String(passField.getPassword());

            if (!name.isEmpty() && !pass.isEmpty()){
                frame.showPage("MENU");
            }
            else {
                JOptionPane.showMessageDialog(this, "Please fill in all fields!");
            }
        });

    }
}
