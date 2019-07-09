package ru.fratask.practice.map.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.fratask.practice.map.dto.AuthenticationRequestDto;
import ru.fratask.practice.map.entity.User;
import ru.fratask.practice.map.service.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/auth/")
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

        if (user == null){
            throw new UsernameNotFoundException("User not found with username: " + requestDto.getUsername());
        }
        if (!BCrypt.checkpw(requestDto.getPassword(), user.getPassword())){
            throw new BadCredentialsException("Invalid username or password!");
        }

        UUID uuid = UUID.randomUUID();

        Map<Object, Object> response = new HashMap<>();
        response.put("username", requestDto.getUsername());
        response.put("token", uuid);
        return ResponseEntity.ok(response);

    }
}
