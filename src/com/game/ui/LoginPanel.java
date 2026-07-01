package com.game.ui;

import com.game.database.DatabaseManager;
import com.game.entities.UserSession;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    public LoginPanel(MainFrame frame){
        setLayout(new GridBagLayout());
        setBackground(new Color(20,40,60));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.gridx = 0;

        JLabel title = new JLabel("WELCOME!");
        title.setForeground(Color.CYAN);
        title.setFont(new Font("Arial", Font.BOLD, 35));
        gbc.gridy = 0;
        add(title, gbc);

        JTextField userField = new JTextField(30);
        userField.setBorder(BorderFactory.createTitledBorder("Username"));
        userField.setFont(new Font("Arial",Font.PLAIN,18));
        gbc.gridy = 1;
        add(userField,gbc);

        JPasswordField passField = new JPasswordField(30);
        passField.setBorder(BorderFactory.createTitledBorder("Password"));
        passField.setFont(new Font("Arial",Font.PLAIN,18));
        gbc.gridy = 2;
        add(passField,gbc);

        JButton loginBtn = new JButton("Enter the Galaxy");
        loginBtn.setFont(new Font("Monospaced", Font.BOLD, 18));
        loginBtn.setPreferredSize(new Dimension(200,50));
        loginBtn.setBackground(new Color(40, 70, 100));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setBorder(BorderFactory.createLineBorder(new Color(100, 150, 200), 2));
        gbc.gridy = 3;
        add(loginBtn,gbc);

        JButton registerBtn = new JButton("Create Account");
        registerBtn.setFont(new Font("Monospaced", Font.BOLD, 18));
        registerBtn.setPreferredSize(new Dimension(200, 50));
        registerBtn.setBackground(new Color(40, 70, 100));
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setFocusPainted(false);
        registerBtn.setBorder(BorderFactory.createLineBorder(new Color(100, 150, 200), 2));
        gbc.gridy = 4;
        add(registerBtn, gbc);

        loginBtn.addActionListener(e -> {
            String name = userField.getText();
            String pass = new String(passField.getPassword());

            if (!name.isEmpty() && !pass.isEmpty()){
                if (DatabaseManager.login(name, pass)) {
                    UserSession.setUser(name);
                    frame.showPage("MENU");
                    JOptionPane.showMessageDialog(this, "Welcome," + name + "!");
                } else {

                    JOptionPane.showMessageDialog(this, "Invalid username or password!");
                }

            }
            else {
                JOptionPane.showMessageDialog(this, "Please fill in all fields!");
            }
        });

        registerBtn.addActionListener(e -> {
            String name = userField.getText();
            String pass = new String(passField.getPassword());

            if (name.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields!");
                return;
            }
            if (DatabaseManager.register(name, pass)) {
                JOptionPane.showMessageDialog(this, "Account created successfully! You can now login.");
            } else {
                JOptionPane.showMessageDialog(this, "Registration failed! Username might already exist.");
            }
        });

    }
}
