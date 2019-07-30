package ru.fratask.practice.map.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fratask.practice.map.entity.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
