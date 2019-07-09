package ru.fratask.practice.map.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fratask.practice.map.entity.Location;
import ru.fratask.practice.map.entity.User;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<User> findByUsername(String name);
}
