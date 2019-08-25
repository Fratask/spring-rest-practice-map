package ru.fratask.practice.map.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.fratask.practice.map.config.TokenStorage;
import ru.fratask.practice.map.dto.LocationDto;
import ru.fratask.practice.map.entity.Location;
import ru.fratask.practice.map.service.LocationService;
import ru.fratask.practice.map.service.UserService;

@Slf4j
@RestController
@RequestMapping(value = "/api/location/")
public class LocationController {

    private final LocationService locationService;

    private final UserService userService;

    private final TokenStorage tokenStorage;

    @Autowired
    public LocationController(LocationService locationService, UserService userService, UserService userService1, TokenStorage tokenStorage) {
        this.locationService = locationService;
        this.userService = userService1;
        this.tokenStorage = tokenStorage;
    }

    @PutMapping
    public ResponseEntity addLocation(@RequestBody LocationDto locationDto) {
        Location registeredLocation;
        registeredLocation = locationDto.toLocation();
        if (!tokenStorage.getTokenUserIdBiMap().containsKey(locationDto.getToken())){
            log.info("IN - controller.LocationController.addLocation - token: {} not found in tokenStorage", locationDto.getToken());
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid token");
        }
        Long userId = tokenStorage.getTokenUserIdBiMap().get(locationDto.getToken());
        registeredLocation.setUser(userService.findById(userId));
        locationService.register(registeredLocation);
        log.info("IN - controller.LocationController.addLocation - location: {} added", registeredLocation);
        return ResponseEntity.status(HttpStatus.OK).body(registeredLocation);
    }


    @GetMapping("roads")
    public ResponseEntity countOfRoadsForUser(@RequestParam Long token){
        //TODO
        log.info("IN - controller.LocationController.countOfRoadsForUser - ");
        return null;
    }

    @GetMapping("road/locations")
    public ResponseEntity countOfLocationsForUsersRoad(@RequestParam Long token, Long roadId){
        //TODO
        log.info("IN - controller.LocationController.addLocation - ");
        return null;
    }


}
