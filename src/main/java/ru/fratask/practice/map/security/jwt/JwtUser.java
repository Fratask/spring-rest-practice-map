package ru.fratask.practice.map.security.jwt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.fratask.practice.map.entity.BehaviorModel;

import java.util.Collection;

public class JwtUser implements UserDetails {

    private final Long id;
    private final String username;
    private final String password;
    private final BehaviorModel behaviorModel;
    private final Collection<? extends GrantedAuthority> authorities;
    private final boolean enabled;

    public JwtUser(
            Long id,
            String username,
            String password,
            BehaviorModel behaviorModel,
            Collection<? extends GrantedAuthority> authorities,
            boolean enabled
    ) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.behaviorModel = behaviorModel;
        this.authorities = authorities;
        this.enabled = enabled;
    }

    @JsonIgnore
    public Long getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public BehaviorModel getBehaviorModel() {
        return behaviorModel;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

}
