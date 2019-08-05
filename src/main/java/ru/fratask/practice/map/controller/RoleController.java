package ru.fratask.practice.map.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.fratask.practice.map.dto.RoleDto;
import ru.fratask.practice.map.dto.UserRolesDto;
import ru.fratask.practice.map.entity.Role;
import ru.fratask.practice.map.entity.User;
import ru.fratask.practice.map.exception.RoleNotFoundException;
import ru.fratask.practice.map.exception.UserNotFoundException;
import ru.fratask.practice.map.repository.RoleRepository;
import ru.fratask.practice.map.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(value = "/api/role")
public class RoleController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    @Autowired
    public RoleController(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @PostMapping("/addRoleForUser")
    public ResponseEntity addRoleForUser(@RequestBody UserRolesDto userRolesDto) throws UserNotFoundException, RoleNotFoundException {
        Optional<User> userOptional = userRepository.findById(userRolesDto.getUserId());
        Optional<Role> roleOptional = roleRepository.findById(userRolesDto.getRoleId());
        if (!userOptional.isPresent()){
            log.info("IN - controller.RoleController.addRoleForUser - user with id: {} not found", userRolesDto.getUserId());
            throw new UserNotFoundException("Invalid user id");
        }
        if (!roleOptional.isPresent()){
            log.info("IN - controller.RoleController.addRoleForUser - role with id: {} not found", userRolesDto.getRoleId());
            throw new RoleNotFoundException("Invalid role id");
        }
        User user = userOptional.get();
        Role role = roleOptional.get();
        List<Role> userRoles = user.getRoles();
        userRoles.add(role);
        user.setRoles(userRoles);
        userRepository.save(user);
        log.info("IN - controller.RoleController.addRoleForUser - role: {} added for user: {}", role, user);
        return ResponseEntity.ok("Role " + role + " success add to user " + user);
    }

    @PostMapping("/addRole")
    public ResponseEntity addNewRole(@RequestBody RoleDto roleDto){
        Role role = new Role();
        role.setName(roleDto.getRoleName());
        roleRepository.save(role);
        return ResponseEntity.ok(role);
    }
}
