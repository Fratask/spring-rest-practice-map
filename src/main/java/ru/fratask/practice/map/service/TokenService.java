package ru.fratask.practice.map.service;

import ru.fratask.practice.map.entity.User;

import java.util.List;

public interface TokenService {

    String register();

    User findByUsername(String username);

    User findById(Long id);

    List<User> getAll();

    void delete(Long id);
}
