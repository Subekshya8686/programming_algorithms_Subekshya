package com.example.task7.view;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginGUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginGUI() {
        setTitle("Login");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.setPreferredSize(new Dimension(500, 600));

        JPanel innerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 2, 5, 5);

        JLabel usernameLabel = new JLabel("Username:");
        innerPanel.add(usernameLabel, gbc);

        gbc.gridy++;
        usernameField = new JTextField(20);
        innerPanel.add(usernameField, gbc);

        gbc.gridy++;
        JLabel passwordLabel = new JLabel("Password:");
        innerPanel.add(passwordLabel, gbc);

        gbc.gridy++;
        passwordField = new JPasswordField(20);
        innerPanel.add(passwordField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton registerButton = new JButton("Register");
        JButton loginButton = new JButton("Login");
        buttonPanel.add(registerButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(20, 0))); // Add space between buttons
        buttonPanel.add(loginButton);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        innerPanel.add(buttonPanel, gbc);

        Border border = BorderFactory.createLineBorder(Color.BLACK);
        innerPanel.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        outerPanel.add(innerPanel, BorderLayout.CENTER);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(LoginGUI.this, "Credentials empty!");
                    return;
                }

                try {
                    // Create HTTP GET request to check if the username exists
                    URL url = new URL("http://localhost:8080/api/users/check?username=" + username);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        // Username exists, proceed with login
                        openTagSelectionPage();
                    } else {
                        // Username not found
                        JOptionPane.showMessageDialog(LoginGUI.this, "User not found!");
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(LoginGUI.this, "Error: " + ex.getMessage());
                }
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open registration window
                dispose(); // Close the login window
                new UserRegistration(); // Open the registration window
            }
        });

        add(outerPanel, BorderLayout.CENTER);
    }

    private void openTagSelectionPage() {
        // Close login window
        setVisible(false);

        // Open tag selection window
        TagSelectionUI tagSelectionUI = new TagSelectionUI();
        tagSelectionUI.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LoginGUI();
            }
        });
    }
}
