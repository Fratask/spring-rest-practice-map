package ru.fratask.practice.map.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.fratask.practice.map.config.TokenStorage;
import ru.fratask.practice.map.dto.AuthenticationRequestDto;
import ru.fratask.practice.map.entity.BehaviorModel;
import ru.fratask.practice.map.entity.Status;
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

    private final TokenStorage tokenStorage;

    @Autowired
    public AuthenticationController(UserService userService, TokenStorage tokenStorage) {
        this.userService = userService;
        this.tokenStorage = tokenStorage;
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody AuthenticationRequestDto authenticationRequestDto){
        User registeredUser = new User();
        registeredUser.setUsername(authenticationRequestDto.getUsername());
        registeredUser.setPassword(authenticationRequestDto.getPassword());
        registeredUser.setBehaviorModel(BehaviorModel.Normal);
        registeredUser.setStatus(Status.ACTIVE);
        registeredUser = userService.register(registeredUser);
        if (registeredUser == null){
            log.info("IN - controller.AuthenticationController.register - invalid username or password");
            throw new UsernameNotFoundException("Invalid username or password");
        }
        log.info("IN - controller.AuthenticationController.register - user: {} registered");
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AuthenticationRequestDto requestDto){
        User user = userService.findByUsername(requestDto.getUsername());

        if (user == null){
            log.info("IN - controller.AuthenticationController.login - user not found");
            throw new UsernameNotFoundException("User not found with username: " + requestDto.getUsername());
        }
        if (!BCrypt.checkpw(requestDto.getPassword(), user.getPassword())){
            log.info("IN - controller.AuthenticationController.login - invalid username or password");
            throw new BadCredentialsException("Invalid username or password!");
        }


        UUID uuid = UUID.randomUUID();
        tokenStorage.getTokenUserIdBiMap().inverse().put(user.getId(), uuid.toString());
        Map<Object, Object> response = new HashMap<>();
        response.put("username", requestDto.getUsername());
        response.put("token", uuid);
        log.info("IN - controller.AuthenticationController.login - user: {} log in with token: {}", user, uuid);
        return ResponseEntity.ok(response);

    }
}
