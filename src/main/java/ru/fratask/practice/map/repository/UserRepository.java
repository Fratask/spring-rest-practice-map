package ru.fratask.practice.map.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fratask.practice.map.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String name);
}
