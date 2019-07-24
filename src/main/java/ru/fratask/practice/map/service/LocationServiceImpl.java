package ru.fratask.practice.map.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.fratask.practice.map.entity.Location;
import ru.fratask.practice.map.repository.LocationRepository;

import java.util.LinkedList;
import java.util.List;

@Service
@Slf4j
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;

    @Autowired
    public LocationServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public Location register(Location location) {
        if (locationRepository.findById(location.getLocationId()).isPresent()) {
            log.info("IN - serviceLocationServiceImpl.register - location: {} successfully registered", location);
            return locationRepository.save(location);
        } else {
            log.warn("IN - service.LocationServiceImpl.register - location: {} already registered", location);
            return location;
        }
    }

    @Override
    public Location findById(Long id) {
        if (locationRepository.findById(id).isPresent()){
            Location location = locationRepository.findById(id).get();
            log.info("IN - service.LocationServiceImp.findById - location: {} was found with id {}", location, id);
            return location;
        } else {
            log.warn("IN - service.LocationServiceImp.findById - location was not found with id {}", id);
            return null;
        }
    }

    @Override
    public List<Location> getAll() {
        List<Location> result = locationRepository.findAll();
        log.info("IN - service.LocationServiceImp.getAll - {} locations found", result.size());
        return result;
    }

    @Override
    public void delete(Long id) {
        locationRepository.deleteById(id);
        log.info("IN - service.LocationServiceImp.delete - location with id {} was deleted", id);
    }

    @Override
    public List<Location> findAllForUsername(String username){
        List<Location> allLocations = locationRepository.findAll();
        List<Location> targetLocations = new LinkedList<>();

        for (Location location: allLocations){
            if (location.getUser().getUsername().equals(username)){
                targetLocations.add(location);
            }
        }
        log.info("IN - service.LocationServiceImp.findAllForUsername - {} locations found for username {}", targetLocations.size(), username);
        return targetLocations;
    }
}
