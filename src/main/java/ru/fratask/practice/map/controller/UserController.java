package ru.fratask.practice.map.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.fratask.practice.map.config.TokenStorage;
import ru.fratask.practice.map.dto.UserDto;
import ru.fratask.practice.map.entity.BehaviorModel;
import ru.fratask.practice.map.entity.Location;
import ru.fratask.practice.map.entity.User;
import ru.fratask.practice.map.repository.UserRepository;
import ru.fratask.practice.map.service.LocationService;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(value = "/api/user/")
public class UserController {

    private final LocationService locationService;
    private final UserRepository userRepository;
    private final TokenStorage tokenStorage;

    @Autowired
    public UserController(LocationService locationService, UserRepository userRepository, TokenStorage tokenStorage) {
        this.locationService = locationService;
        this.userRepository = userRepository;
        this.tokenStorage = tokenStorage;
    }

    @PostMapping("/behavior/analyze")
    public ResponseEntity behaviorModelAnalyze(@RequestBody UserDto userDto) {
        Optional<User> userOptional = userRepository.findByUsername(userDto.getUsername());
        if (!userOptional.isPresent()) { // check user exist in database
            log.info("IN - controller.UserController.behaviorModelAnalyze - user not found with username: {}", userDto.getUsername());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid username");
        }
        if (!tokenStorage.getTokenUserIdBiMap().containsKey(userDto.getToken())) { // check token exist in tokenStorage
            log.info("IN - controller.UserController.behaviorModelAnalyze - token not found: {}", userDto.getToken());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token not found");
        }
        Optional<User> usersAnalyzerOptional = userRepository.findById(tokenStorage.getTokenUserIdBiMap().get(userDto.getToken()));
        if (!usersAnalyzerOptional.isPresent()) { // check user exist with token in tokenStorage
            log.info("IN - controller.UserController.behaviorModelAnalyze - user not found with token: {}", userDto.getToken());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token");
        }
        User usersAnalyzer = usersAnalyzerOptional.get();
        if (!usersAnalyzer.haveRole("admin") && !usersAnalyzer.getUsername().equals(userDto.getUsername())) { // check user permission
            log.info("IN - controller.UserController.behaviorModelAnalyze - for userAnalyzer: {} with roles: {}",usersAnalyzer, usersAnalyzer.getRoles());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Permission denied");
        }
        if (locationService.findAllForUsername(userDto.getUsername()).size() == 0){ // check user's locations existing
            log.info("IN - controller.UserController.behaviorModelAnalyze - location list is empty for username: {}", userDto.getUsername());
        }
        BehaviorModel behaviorModel = analyzeBehaviorModel(userDto);
        if (behaviorModel == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with this username not found!");
        }
        ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
        builder.allow(HttpMethod.POST);
        builder.contentType(MediaType.APPLICATION_JSON);
        builder.eTag("W/MyeTag\"");
        return builder.body(behaviorModel);
    }

    private BehaviorModel analyzeBehaviorModel(UserDto userDto) {
        double maxSpeed = 0;
        double averageSpeed;
        double averageSpeedUp;
        long time = 0;
        double distance = 0;
        List<Location> locations = locationService.findAllForUsername(userDto.getUsername());

        Location prevLocation = locations.get(1);
        for (Location location: locations){
            time = time + location.timeBetweenLocationsInMilliseconds(prevLocation);
            distance = distance + location.distanceToLocationInKm(prevLocation);
            prevLocation = location;
            double speed = distance/time;
            if (maxSpeed < speed){
                maxSpeed = speed;
            }
        }
        averageSpeed = distance/time;
        averageSpeedUp = averageSpeed/time;

        BehaviorModel behaviorModel;
        if (maxSpeed < 90) {
            if (averageSpeed > 60) {
                if (averageSpeedUp > 10) {
                    behaviorModel = BehaviorModel.Agressive;
                } else {
                    behaviorModel = BehaviorModel.Normal;
                }
            } else {
                if (averageSpeedUp > 10) {
                    behaviorModel = BehaviorModel.Normal;
                } else {
                    behaviorModel = BehaviorModel.Passive;
                }
            }
        } else {
            behaviorModel = BehaviorModel.Agressive;
        }

        Optional<User> userOptional = userRepository.findByUsername(userDto.getUsername());
        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
        } else {
            return null;
        }
        user.setBehaviorModel(behaviorModel);
        userRepository.save(user);
        return behaviorModel;
    }
}
