package com.example.task7.service;

import com.example.task7.entity.Content;
import com.example.task7.entity.User;
import com.example.task7.repository.ContentRepository;
import com.example.task7.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContentRepository contentRepository;

    public User registerUser(User user) {
        return userRepository.save(user);
    }

    public void followUser(Long userId, Long followUserId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        User followUser = userRepository.findById(followUserId)
                .orElseThrow(() -> new IllegalArgumentException("Follow user not found with id: " + followUserId));

        user.getFollowing().add(followUser);
        userRepository.save(user);
    }

    public void likeContent(Long userId, Long contentId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new IllegalArgumentException("Content not found with id: " + contentId));

        content.setLikes(content.getLikes() + 1);
        contentRepository.save(content);
    }

    public User buildUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        // Implement algorithm to build user profile
        User userProfile = new User();
        userProfile.setUsername(user.getUsername());
        userProfile.setInterests(user.getInterests());
        userProfile.setFollowing(userProfile.getFollowing());

        return userProfile;
    }
}

