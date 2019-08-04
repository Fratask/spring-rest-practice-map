package ru.fratask.practice.map.entity.keys;

import java.io.Serializable;

public class LocationPK implements Serializable {

    private Long locationId;

    private Long user;

    private Long roadId;

    public LocationPK(){}

    public LocationPK(Long locationId, Long user, Long roadId) {
        this.locationId = locationId;
        this.user = user;
        this.roadId = roadId;
    }
}
