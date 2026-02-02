package com.chatapp.user.service;

import com.chatapp.user.User;
import com.chatapp.user.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User updateUsername(Long id, String username) {
        User user = getUserById(id);
        user.setUsername(username);
        return userRepository.save(user);
    }

    public User updateStatus(Long id, String status) {
        User user = getUserById(id);
        user.setStatus(status);
        return userRepository.save(user);
    }
}
