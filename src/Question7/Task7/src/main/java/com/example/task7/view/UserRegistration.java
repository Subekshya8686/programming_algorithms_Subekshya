package com.example.task7.view;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class UserRegistration extends JFrame {

    private JTextField usernameField;

    public UserRegistration() {
        setTitle("User Registration");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 150);

        initComponents();
        addComponentsToFrame();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponents() {
        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(15);

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                if (!username.isEmpty()) {
                    registerUser(username);
                } else {
                    JOptionPane.showMessageDialog(UserRegistration.this, "Username cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        setLayout(new GridLayout(2, 2));
        add(usernameLabel);
        add(usernameField);
        add(registerButton);
    }

    private void addComponentsToFrame() {
        setLayout(new GridLayout(3, 1));
        add(usernameField);
    }

    private void registerUser(String username) {
        try {
            URL url = new URL("http://localhost:8080/api/users");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonInputString = "{\"username\": \"" + username + "\"}";

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            if (conn.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
                JOptionPane.showMessageDialog(UserRegistration.this, "User registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(UserRegistration.this, "Failed to register user!", "Error", JOptionPane.ERROR_MESSAGE);
            }

            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(UserRegistration.this, "Failed to connect to the server!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UserRegistration());
    }
}
