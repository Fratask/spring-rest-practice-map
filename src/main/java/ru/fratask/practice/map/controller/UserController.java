package ru.fratask.practice.map.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.fratask.practice.map.config.TokenStorage;
import ru.fratask.practice.map.dto.UserDto;
import ru.fratask.practice.map.entity.BehaviorModel;
import ru.fratask.practice.map.entity.Location;
import ru.fratask.practice.map.entity.Role;
import ru.fratask.practice.map.entity.User;
import ru.fratask.practice.map.exception.TokenNotFoundException;
import ru.fratask.practice.map.exception.UserNotFoundException;
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
    public ResponseEntity behaviorModelAnalyze(@RequestBody UserDto userDto) throws TokenNotFoundException, UserNotFoundException, Exception {
        if (!tokenStorage.getTokenUserIdBiMap().containsKey(userDto.getToken())) {
            log.info("IN - controller.UserController.behaviorModelAnalyze - token not found: {}", userDto.getToken());
            throw new TokenNotFoundException("Invalid token");
        }
        Optional<User> usersAnalyzerOptional = userRepository.findById(tokenStorage.getTokenUserIdBiMap().get(userDto.getToken()));
        if (!usersAnalyzerOptional.isPresent()) {
            log.info("IN - controller.UserController.behaviorModelAnalyze - user not found with token: {}", userDto.getToken());
            throw new UserNotFoundException("Invalid token");
        }
        User usersAnalyzer = usersAnalyzerOptional.get();
        if (!usersAnalyzer.haveRole("admin") && !usersAnalyzer.getUsername().equals(userDto.getUsername())) {
            log.info("IN - controller.UserController.behaviorModelAnalyze - for userAnalyzer: {} with roles: {}",usersAnalyzer, usersAnalyzer.getRoles());
            throw new Exception("Permission denied");
        }
        BehaviorModel behaviorModel = analyzeBehaviorModel(userDto);
        return ResponseEntity.ok(behaviorModel);
    }

    private BehaviorModel analyzeBehaviorModel(UserDto userDto) throws Exception, UserNotFoundException {
        double maxSpeed = 0;
        double averageSpeed;
        double averageSpeedUp;
        long time = 0;
        double distance = 0;

        Optional<User> userOptional = userRepository.findByUsername(userDto.getUsername());
        if (!userOptional.isPresent()) {
            log.info("IN - controller.UserController.analyzeBehaviorModel - user not found with username: {}", userDto.getUsername());
            throw new UserNotFoundException("Invalid username");
        }
        List<Location> locations = locationService.findAllForUsername(userDto.getUsername());
        if (locations.size() == 0){
            log.info("IN - controller.UserController.analyzeBehaviorModel - location list is empty for username: {}", userDto.getUsername());
            throw new Exception("Locations list is empty");
        }
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


        User user = userOptional.get();
        user.setBehaviorModel(behaviorModel);
        userRepository.save(user);
        return behaviorModel;
    }
}
