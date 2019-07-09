package ru.fratask.practice.map.security.jwt;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.fratask.practice.map.entity.BehaviorModel;
import ru.fratask.practice.map.entity.Role;
import ru.fratask.practice.map.entity.Status;
import ru.fratask.practice.map.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public final class JwtUserFactory {

    public JwtUserFactory() {
    }

    public static JwtUser create(User user) {
        return new JwtUser(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getBehaviorModel(),
                mapToGrantedAuthorities(new ArrayList<>(user.getRoles())),
                user.getStatus().equals(Status.ACTIVE)
        );
    }


    private static List<GrantedAuthority> mapToGrantedAuthorities(List<Role> userRoles) {
        return userRoles.stream()
                .map(role ->
                        new SimpleGrantedAuthority(role.getName())
                ).collect(Collectors.toList());
    }

}