package ru.fratask.practice.map.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.fratask.practice.map.entity.Location;
import ru.fratask.practice.map.repository.LocationRepository;

import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;

    @Autowired
    public LocationServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public Location register(Location location) {
        return locationRepository.save(location);
    }

    @Override
    public Location findById(Long id) {
        return locationRepository.findById(id).get();
    }

    @Override
    public List<Location> getAll() {
        return locationRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        locationRepository.deleteById(id);
    }
}
