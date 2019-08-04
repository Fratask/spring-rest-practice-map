package ru.fratask.practice.map.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.fratask.practice.map.config.TokenStorage;
import ru.fratask.practice.map.entity.User;
import ru.fratask.practice.map.service.LocationService;


@Data
@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {
    private String username;
    private String token;

    @Autowired
    private static LocationService locationService;;


    public User toUser(){
        User user = new User();
        user.setUsername(username);

        return user;
    }

    public static UserDto fromUser(User user) {
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setToken(TokenStorage.getInstance().getTokenUserIdBiMap().inverse().get(user.getId()));
        return userDto;
    }
}
