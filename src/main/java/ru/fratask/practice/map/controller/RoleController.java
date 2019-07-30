package ru.fratask.practice.map.controller;

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
import ru.fratask.practice.map.repository.RoleRepository;
import ru.fratask.practice.map.repository.UserRepository;

import java.util.List;

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
    public ResponseEntity addRoleForUser(@RequestBody UserRolesDto userRolesDto){
        User user = userRepository.getOne(userRolesDto.getUserId());
        Role role = roleRepository.getOne(userRolesDto.getRoleId());
        List<Role> userRoles = user.getRoles();
        userRoles.add(role);
        user.setRoles(userRoles);
        userRepository.save(user);
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
