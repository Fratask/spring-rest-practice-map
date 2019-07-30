package ru.fratask.practice.map.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fratask.practice.map.entity.Role;
import ru.fratask.practice.map.entity.User;

public interface UserRolesRepository extends JpaRepository<User, Role> {
}
