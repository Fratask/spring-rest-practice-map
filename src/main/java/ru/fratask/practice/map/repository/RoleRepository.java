package ru.fratask.practice.map.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fratask.practice.map.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
