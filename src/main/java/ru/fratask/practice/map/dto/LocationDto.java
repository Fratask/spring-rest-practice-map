package ru.fratask.practice.map.dto;

import lombok.Data;

import java.util.Date;

@Data
public class LocationDto {
    private Long locationId;
    private Long roadId;
    private Float latitude;
    private Float longitude;
    private Float elevation;
    private Date time;
    private String token;
}
