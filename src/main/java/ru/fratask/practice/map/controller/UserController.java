package ru.fratask.practice.map.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.fratask.practice.map.dto.UserDto;
import ru.fratask.practice.map.entity.BehaviorModel;
import ru.fratask.practice.map.entity.Location;

import java.util.List;

@RestController(value = "/api/user/")
public class UserController {

    @PostMapping("/behavior/analyze")
    public ResponseEntity behaviorModelAnalyze(@RequestBody UserDto userDto){
        BehaviorModel response = analyzeBehaviorModel(userDto);
        return ResponseEntity.ok(response);
    }

    private BehaviorModel analyzeBehaviorModel(UserDto userDto){
        double maxSpeed = 0;
        double averageSpeed;
        double averageSpeedUp;
        long time = 0;
        double distance = 0;

        List<Location> locations = userDto.getLocations();
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
