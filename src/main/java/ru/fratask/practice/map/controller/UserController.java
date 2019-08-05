package ru.fratask.practice.map.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.fratask.practice.map.dto.UserDto;
import ru.fratask.practice.map.entity.BehaviorModel;
import ru.fratask.practice.map.entity.Location;
import ru.fratask.practice.map.service.LocationService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/user/")
public class UserController {

    private final LocationService locationService;

    @Autowired
    public UserController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping("/behavior/analyze")
    public ResponseEntity behaviorModelAnalyze(@RequestBody UserDto userDto){
        BehaviorModel behaviorModel = analyzeBehaviorModel(userDto);
        return ResponseEntity.ok(behaviorModel);
    }

    private BehaviorModel analyzeBehaviorModel(UserDto userDto){
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
        return behaviorModel;
    }
}
