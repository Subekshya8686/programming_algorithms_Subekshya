package com.example.task7.view;

import com.example.task7.view.LoginGUI;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UserRegistration extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public UserRegistration() {
        setTitle("Registration");
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
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(UserRegistration.this, "Credentials empty");
                    return; // Stop further execution
                }

                try {
                    if (usernameExists(username)) {
                        JOptionPane.showMessageDialog(UserRegistration.this, "Username already taken. Please login.");

                        // Close the registration frame
                        dispose();
                        return; // Stop further execution
                    }

                    // Create HTTP POST request
                    URL url = new URL("http://localhost:8080/api/users/register");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setDoOutput(true);

                    // Construct JSON data manually
                    String jsonInputString = "{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}";

                    // Write JSON data to the connection output stream
                    try (OutputStream os = connection.getOutputStream()) {
                        byte[] input = jsonInputString.getBytes("utf-8");
                        os.write(input, 0, input.length);
                    }

                    // Read response from the server
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                        StringBuilder response = new StringBuilder();
                        String responseLine;
                        while ((responseLine = br.readLine()) != null) {
                            response.append(responseLine.trim());
                        }
                        JOptionPane.showMessageDialog(UserRegistration.this, response.toString());
                        // Close the registration frame
                        dispose();
                        // Open the login page
                        new LoginGUI();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(UserRegistration.this, "Error: " + ex.getMessage());
                }
            }
        });

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the registration frame
                new LoginGUI(); // Open the login page
            }
        });

        add(outerPanel, BorderLayout.CENTER);
    }
    private boolean usernameExists(String username) throws IOException {
        // Create HTTP GET request to check if the username exists
        URL url = new URL("http://localhost:8080/api/users/check?username=" + username);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        // Check if the response code indicates success
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return true; // Username exists
        } else {
            return false; // Username does not exist
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new UserRegistration();
            }
        });
    }
}
