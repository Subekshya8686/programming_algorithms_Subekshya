package com.example.task7.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import com.example.task7.entity.Content;
import com.fasterxml.jackson.databind.ObjectMapper; // Import Jackson ObjectMapper
import org.springframework.web.client.RestTemplate; // Import Spring RestTemplate
import org.springframework.web.util.UriComponentsBuilder;

// Custom component to display content (title and description)
class ContentCard extends JPanel {
    private JLabel titleLabel;
    private JLabel descriptionLabel;

    public ContentCard(String title, String description) {
        setLayout(new GridLayout(2, 1));
        titleLabel = new JLabel(title);
        descriptionLabel = new JLabel(description);
        add(titleLabel);
        add(descriptionLabel);
        setBorder(BorderFactory.createEtchedBorder()); // Add border for visual separation
    }
}

public class ContentGUI extends JFrame {
    private JPanel contentPanel;

    public ContentGUI() {
        setTitle("Content Viewer");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        getContentPane().add(scrollPane);

        JButton fetchButton = new JButton("Fetch Content");
        fetchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Call method to fetch content from the backend
                List<String> tags = new ArrayList<>();
                List<Content> fetchedContent = fetchContentFromBackend(tags);
                // Update GUI with fetched content
                updateContentPanel(fetchedContent);
            }
        });
        getContentPane().add(fetchButton, BorderLayout.NORTH);
    }

    private List<Content> fetchContentFromBackend(List<String> tags) {
        // Create a RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();

        // Build the URL with query parameters for tags
        String url = "http://localhost:8080/api/content";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        if (tags != null && !tags.isEmpty()) {
            builder.queryParam("tags", String.join(",", tags));
        }

        // Make an HTTP GET request to fetch content from the backend
        Content[] fetchedContent = restTemplate.getForObject(builder.toUriString(), Content[].class);

        // Convert the array to a List and return it
        return List.of(fetchedContent);
    }
    private void updateContentPanel(List<Content> fetchedContent) {
        contentPanel.removeAll(); // Clear existing content
        // Populate contentPanel with ContentCard components
        for (Content content : fetchedContent) {
            ContentCard contentCard = new ContentCard(content.getTitle(), content.getDescription());
            contentPanel.add(contentCard);
        }
        contentPanel.revalidate(); // Refresh layout
        contentPanel.repaint(); // Repaint components
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ContentGUI::new);
    }
}
