package ru.fratask.practice.map.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.fratask.practice.map.dto.AuthenticationRequestDto;
import ru.fratask.practice.map.entity.User;
import ru.fratask.practice.map.service.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@Slf4j
public class AuthenticationController {

    private final UserService userService;


    @Autowired
    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody AuthenticationRequestDto authenticationRequestDto){
        User registeredUser = new User();
        registeredUser.setUsername(authenticationRequestDto.getUsername());
        registeredUser.setPassword(authenticationRequestDto.getPassword());
        registeredUser = userService.register(registeredUser);
        if (registeredUser == null){
            throw new UsernameNotFoundException("Invalid username or password");
        }
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AuthenticationRequestDto requestDto){
        User user = userService.findByUsername(requestDto.getUsername());

        UUID uuid = UUID.randomUUID();

        Map<Object, Object> response = new HashMap<>();
        response.put("username", requestDto.getUsername());
        response.put("token", uuid);
        return ResponseEntity.ok(response);

    }
}
