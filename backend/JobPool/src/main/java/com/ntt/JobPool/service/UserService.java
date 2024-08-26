package com.ntt.JobPool.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.ntt.JobPool.domain.User;
import com.ntt.JobPool.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User saveUser(User user) {
        return this.userRepository.save(user);
    }

    public User getUserById(Long userId) {
        return this.userRepository.findById(userId).orElse(null);
    }

    public User getUserUserName(String userName) {
        return this.userRepository.findUserByEmail(userName);
    }

    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    public User updateUser(@RequestBody User user) {
        return this.userRepository.save(user);
    }

    public void deleteUser(long userId) {
        this.userRepository.deleteById(userId);
    }
}
