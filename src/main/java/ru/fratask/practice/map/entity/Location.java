package ru.fratask.practice.map.entity;

import lombok.Data;
import ru.fratask.practice.map.entity.keys.LocationPK;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "locations")
@IdClass(LocationPK.class)
public class Location implements Serializable {

    @Id
    @Column(name = "id_location")
    private Long locationId;

    @Id
    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @Id
    @Column(name = "id_road")
    private Long roadId;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "elevation")
    private Double elevation;

    @Column(name = "time")
    private Date time;

    public double distanceToLocationInKm(Location location){
        double distance;
        int R = 6373; // radius of the earth in kilometres
        double lat1 = latitude;
        double lat2 = location.getLatitude();
        double lon1 = longitude;
        double lon2 = location.getLongitude();
        double lat1rad = Math.toRadians(lat1);
        double lat2rad = Math.toRadians(lat2);
        double deltaLat = Math.toRadians(lat2-lat1);
        double deltaLon = Math.toRadians(lon2-lon1);

        double a = Math.sin(deltaLat/2) * Math.sin(deltaLat/2) +
                Math.cos(lat1rad) * Math.cos(lat2rad) *
                        Math.sin(deltaLon/2) * Math.sin(deltaLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        distance = R * c;
        return distance;
    }

    public long timeBetweenLocationsInMilliseconds(Location location){
        return location.getTime().getTime() - time.getTime();
    }
}
