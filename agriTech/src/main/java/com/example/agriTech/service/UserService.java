package com.example.agriTech.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.agriTech.model.User;
import com.example.agriTech.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUser() {
        return this.userRepository.findAllClients();
    }
    public User getUserId (Long id) {
        return this.userRepository.findByAccountId(id)
            .orElseThrow(()->new RuntimeException("khong tim thay nguoi dung"));
    }
}
