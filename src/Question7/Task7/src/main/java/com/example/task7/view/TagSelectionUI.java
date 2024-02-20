package com.example.task7.view;

import com.example.task7.repository.UserRepository;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class TagSelectionUI extends JFrame {
    private UserRepository userRepository;
    private String backendUrl = "http://localhost:8080/api/tags/post";

    public TagSelectionUI() {
        initializeUI();
    }

    private JCheckBox tag1CheckBox;
    private JCheckBox tag2CheckBox;
    private JCheckBox tag3CheckBox;
    private JButton submitButton;

    private void initializeUI() {
        setTitle("Tag Selection");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null); // Center the window
        setLayout(new GridLayout(4, 1));

        tag1CheckBox = new JCheckBox("Tag 1");
        tag2CheckBox = new JCheckBox("Tag 2");
        tag3CheckBox = new JCheckBox("Tag 3");
        submitButton = new JButton("Submit");

        add(tag1CheckBox);
        add(tag2CheckBox);
        add(tag3CheckBox);
        add(submitButton);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder selectedTags = new StringBuilder();
                if (tag1CheckBox.isSelected()) {
                    selectedTags.append(tag1CheckBox.getText()).append(", ");
                }
                if (tag2CheckBox.isSelected()) {
                    selectedTags.append(tag2CheckBox.getText()).append(", ");
                }
                if (tag3CheckBox.isSelected()) {
                    selectedTags.append(tag3CheckBox.getText()).append(", ");
                }

                String selectedTagsString = selectedTags.toString().replaceAll(", $", "");

                sendTagsToBackend(selectedTagsString);
                setVisible(false);
            }
        });
    }

    private void sendTagsToBackend(String selectedTags) {
        try {
            URL url = new URL(backendUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            String jsonInputString = "{\"selectedTags\": \"" + selectedTags + "\"}";

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.out.println("Response from backend: " + response.toString());
                // Update GUI based on backend response if needed
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(TagSelectionUI.this, "Error: " + ex.getMessage());
        }
    }
}
