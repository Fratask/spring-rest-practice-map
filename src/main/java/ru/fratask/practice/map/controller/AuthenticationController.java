package ru.fratask.practice.map.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.fratask.practice.map.entity.User;
import ru.fratask.practice.map.service.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class AuthenticationController {

    private final UserService userService;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationController(UserService userService, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestParam String username, @RequestParam String password){
        User registredUser = new User();
        registredUser.setUsername(username);
        registredUser.setPassword(password);
        userService.register(registredUser);
        return ResponseEntity.ok(registredUser);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestParam String username, @RequestParam String password){
        User user = userService.findByUsername(username);
        if (user == null){
            throw new UsernameNotFoundException("User with username: " + username + " not found");
        }
        if (!user.getPassword().equals(passwordEncoder.encode(password))){
            throw new UsernameNotFoundException("Invalid username or password");
        }
        UUID uuid = UUID.randomUUID();

        Map<Object, Object> response = new HashMap<>();
        response.put("username", username);
        response.put("token", uuid);
        return ResponseEntity.ok(response);

    }
}
