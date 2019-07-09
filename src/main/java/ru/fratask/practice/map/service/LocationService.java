package ru.fratask.practice.map.service;


import ru.fratask.practice.map.entity.Location;

import java.util.List;

public interface LocationService {

    Location register(Location location);

    Location findById(Long id);

    List<Location> getAll();

    void delete(Long id);
}
