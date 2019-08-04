package ru.fratask.practice.map.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.fratask.practice.map.entity.Location;
import ru.fratask.practice.map.entity.keys.LocationPK;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
}
