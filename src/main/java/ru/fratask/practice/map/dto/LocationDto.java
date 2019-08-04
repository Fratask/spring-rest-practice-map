package ru.fratask.practice.map.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.fratask.practice.map.config.TokenStorage;
import ru.fratask.practice.map.entity.Location;
import ru.fratask.practice.map.service.UserService;

import java.util.Date;

@Data
@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationDto {
    private Long locationId;
    private Long roadId;
    private Double latitude;
    private Double longitude;
    private Double elevation;
    private Date time;
    private String token;


    public Location toLocation(){
        Location location = new Location();
        location.setLocationId(locationId);
        location.setRoadId(roadId);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        location.setElevation(elevation);
        location.setRoadId(roadId);
        location.setTime(time);
        return location;
    }

    public static LocationDto fromLocation(Location location){
        LocationDto locationDto = new LocationDto();
        locationDto.setRoadId(location.getRoadId());
        locationDto.setLatitude(location.getLatitude());
        locationDto.setLongitude(location.getLongitude());
        locationDto.setElevation(location.getElevation());
        locationDto.setTime(location.getTime());
        locationDto.setToken(TokenStorage.getInstance().getTokenUserIdBiMap().inverse().get(location.getUser().getId()));
        return locationDto;
    }

}
