package ru.fratask.practice.map.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "locations")
public class Location {

    @Id
    @Column(name = "id_location")
    private Long locationId;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @Column(name = "id_road")
    private Long roadId;

    @Column(name = "latitude")
    private Float latitude;

    @Column(name = "longitude")
    private Float longitude;

    @Column(name = "elevation")
    private Float elevation;

    @Column(name = "time")
    private Date time;
}
