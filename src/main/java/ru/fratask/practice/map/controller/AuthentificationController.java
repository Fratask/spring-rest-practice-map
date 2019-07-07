package ru.fratask.practice.map.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.fratask.practice.map.entity.User;
import ru.fratask.practice.map.service.UserService;

@RestController
public class AuthentificationController {

    private final UserService userService;

    @Autowired
    public AuthentificationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestParam String username, @RequestParam String password){
        User registredUser = new User();
        registredUser.setUsername(username);
        registredUser.setPassword(password);
        userService.register(registredUser);
        return ResponseEntity.ok(registredUser);
    }
}
