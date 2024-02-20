package com.example.task7.controller;

import com.example.task7.entity.User;
import com.example.task7.repository.UserRepository;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        // Perform validation and save user to database
        return userRepository.save(user);
    }

    @PostMapping("/{userId}/follow")
    public void followUser(@PathVariable Long userId, @RequestParam Long followUserId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        User followUser = userRepository.findById(followUserId)
                .orElseThrow(() -> new IllegalArgumentException("Follow user not found with id: " + followUserId));

        user.follow(followUser);
        userRepository.save(user);
    }

    @PostMapping("/{userId}/unfollow")
    public void unfollowUser(@PathVariable Long userId, @RequestParam Long unfollowUserId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        User unfollowUser = userRepository.findById(unfollowUserId)
                .orElseThrow(() -> new IllegalArgumentException("Unfollow user not found with id: " + unfollowUserId));

        user.unfollow(unfollowUser);
        userRepository.save(user);
    }
}


