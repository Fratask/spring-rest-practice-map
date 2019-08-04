package ru.fratask.practice.map.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String AUTH_ENDPOINT = "/api/auth/**";
    private static final String LOCATION_ENDPOINT = "/api/location/**";
    private static final String USER_ENDPOINT = "/api/user/**";
    private static final String ROLE_ENDPOINT = "/api/role/**";



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(USER_ENDPOINT).permitAll()
                .antMatchers(ROLE_ENDPOINT).permitAll()
                .antMatchers(AUTH_ENDPOINT).permitAll()
                .antMatchers(LOCATION_ENDPOINT).permitAll()
                .anyRequest().authenticated();
    }
}
