package ru.fratask.practice.map.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.fratask.practice.map.dto.LocationDto;
import ru.fratask.practice.map.entity.Location;
import ru.fratask.practice.map.security.jwt.JwtTokenProvider;
import ru.fratask.practice.map.service.LocationService;
import ru.fratask.practice.map.service.UserService;

@RestController(value = "/api/location/")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/add")
    public ResponseEntity addLocation(@RequestBody LocationDto locationDto){
        Location registeredLocation = new Location();
        registeredLocation = locationDto.toLocation();
        locationService.register(registeredLocation);
        return ResponseEntity.ok(registeredLocation);
    }
}