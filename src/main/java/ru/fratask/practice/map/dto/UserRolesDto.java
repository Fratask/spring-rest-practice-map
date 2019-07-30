package ru.fratask.practice.map.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
public class UserRolesDto {

    Long userId;
    Long roleId;
}
