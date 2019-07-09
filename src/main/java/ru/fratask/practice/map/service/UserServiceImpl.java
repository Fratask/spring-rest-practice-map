package ru.fratask.practice.map.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.fratask.practice.map.entity.User;
import ru.fratask.practice.map.repository.UserRepository;

import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public User register(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()){
            return null;
        } else {
            user.setId(userRepository.count());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User registeredUser = userRepository.save(user);
            log.info("IN - register - user: {} successfully registred", registeredUser);
            return registeredUser;
        }
    }

    public User findByUsername(String username) {
        User result = userRepository.findByUsername(username).get();
        log.info("IN - findByUsername - user: {} found by username: {}", result, username);
        return result;
    }

    public User findById(Long id) {
        User result = userRepository.findById(id).orElse(null);

        if (result == null){
            log.warn("IN - findById - no user found by id: {}", id);
            return null;
        }

        log.info("IN - findById - user: {} found by id: {}", result, id);
        return result;
    }

    public List<User> getAll() {
        List<User> result = userRepository.findAll();
        log.info("IN - getAll - {} users found", result.size());
        return result;
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
        log.info("IN delete - user with id: {} successfully deleted", id);
    }
}
