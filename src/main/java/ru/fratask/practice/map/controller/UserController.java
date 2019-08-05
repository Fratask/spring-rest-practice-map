package ru.fratask.practice.map.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.fratask.practice.map.config.TokenStorage;
import ru.fratask.practice.map.dto.UserDto;
import ru.fratask.practice.map.entity.BehaviorModel;
import ru.fratask.practice.map.entity.Location;
import ru.fratask.practice.map.entity.Role;
import ru.fratask.practice.map.entity.User;
import ru.fratask.practice.map.repository.UserRepository;
import ru.fratask.practice.map.service.LocationService;

import java.util.List;
import java.util.Optional;

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
    public ResponseEntity behaviorModelAnalyze(@RequestBody UserDto userDto){
        if (tokenStorage.getTokenUserIdBiMap().containsKey(userDto.getToken())) {
            Optional<User> usersAnalyzerOptional = userRepository.findById(tokenStorage.getTokenUserIdBiMap().get(userDto.getToken()));
            if (usersAnalyzerOptional.isPresent()) {
                User usersAnalyzer = usersAnalyzerOptional.get();
                if (usersAnalyzer.haveRole("admin") || usersAnalyzer.getUsername().equals(userDto.getUsername())) {
                    BehaviorModel behaviorModel = analyzeBehaviorModel(userDto);
                    if (behaviorModel == null) {
                        return ResponseEntity.ok("not found");
                    } else {
                        return ResponseEntity.ok(behaviorModel);
                    }
                } else {
                    return ResponseEntity.ok("not found");
                }
            }
        }
        return ResponseEntity.ok("not found");
    }

    private BehaviorModel analyzeBehaviorModel(UserDto userDto){
        double maxSpeed = 0;
        double averageSpeed;
        double averageSpeedUp;
        long time = 0;
        double distance = 0;

        List<Location> locations = locationService.findAllForUsername(userDto.getUsername());
        if (locations.size() == 0){
            return null;
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

        Optional<User> userOptional = userRepository.findByUsername(userDto.getUsername());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setBehaviorModel(behaviorModel);
            userRepository.save(user);
            return behaviorModel;
        } else {
            return null;
        }
    }
}
