package com.example.task7.service;

import com.example.task7.entity.Content;
import com.example.task7.entity.User;
import com.example.task7.repository.ContentRepository;
import com.example.task7.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RecommendationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContentRepository contentRepository;

    public List<Content> recommendContent(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + username);
        }

        // Get user's interests
        Set<String> userInterests = Collections.singleton(user.getInterests());

        // Fetch all content from the database
        List<Content> allContent = contentRepository.findAll();

        // Calculate relevance score for each content item
        Map<Content, Integer> contentScores = new HashMap<>();
        for (Content content : allContent) {
            int score = calculateContentScore(content, userInterests, user);
            contentScores.put(content, score);
        }

        // Sort content items based on relevance score (descending order)
        List<Content> recommendedContent = new ArrayList<>(contentScores.keySet());
        recommendedContent.sort((c1, c2) -> contentScores.get(c2).compareTo(contentScores.get(c1)));

        return recommendedContent;
    }

    // Calculate relevance score for a content item based on user interests, content popularity, and network influence
    private int calculateContentScore(Content content, Set<String> userInterests, User user) {
        int score = 0;

        // Check if content matches user interests
        Set<String> contentTags = content.getTags();
        for (String tag : contentTags) {
            if (userInterests.contains(tag)) {
                score++;
            }
        }

        // Consider content popularity (e.g., number of likes)
        score += content.getLikes();

        // Consider network influence (e.g., number of followers)
        // Adjust weight based on the importance of network influence
        score += user.getFollowing().size(); // Considering followers as network influence

        return score;
    }
}
