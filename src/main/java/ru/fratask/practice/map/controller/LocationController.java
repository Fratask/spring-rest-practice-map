package ru.fratask.practice.map.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.fratask.practice.map.config.TokenStorage;
import ru.fratask.practice.map.dto.LocationDto;
import ru.fratask.practice.map.entity.Location;
import ru.fratask.practice.map.service.LocationService;
import ru.fratask.practice.map.service.UserService;

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

    @PostMapping("/add")
    public ResponseEntity addLocation(@RequestBody LocationDto locationDto){
        Location registeredLocation;
        registeredLocation = locationDto.toLocation();
        Long userId = tokenStorage.getTokenUserIdBiMap().get(locationDto.getToken());
        registeredLocation.setUser(userService.findById(userId));
        locationService.register(registeredLocation);
        return ResponseEntity.ok(registeredLocation);
    }

    @GetMapping("roads")
    public ResponseEntity countOfRoadsForUser(@RequestParam Long token){
        //TODO
        return null;
    }

    @GetMapping("road/locations")
    public ResponseEntity countOfLocationsForUsersRoad(@RequestParam Long token, Long roadId){
        //TODO
        return null;
    }


}
