package ru.fratask.practice.map.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.fratask.practice.map.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
