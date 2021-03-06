package br.com.battista.arcadia.caller.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.battista.arcadia.caller.model.User;
import br.com.battista.arcadia.caller.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        log.info("Find all users!");
        return userRepository.findAll();
    }

    public User getUserByUsername(String username) {
        log.info("Find the user by username: {}!", username);
        return userRepository.findByUsername(username);
    }

    public User saveUser(User user) {
        log.info("Create new user!");
        return userRepository.saveOrUpdateUser(user);
    }

    public User updateUser(User user) {
        log.info("Update user!");
        return userRepository.saveOrUpdateUser(user);
    }

    public void deleteUser(User user) {
        log.info("Delete user!");
        userRepository.deleteByUsername(user);
    }

}
