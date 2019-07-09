package ru.fratask.practice.map.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Table(name = "users")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "users")
    private Set<Location> locations;

    @Enumerated(EnumType.STRING)
    @Column(name = "behavior_model")
    private BehaviorModel behaviorModel;

}
