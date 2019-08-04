package ru.fratask.practice.map.entity.keys;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class UserRolesKey implements Serializable {

    @Column(name = "user_id")
    Long userId;

    @Column(name = "role_id")
    Long roleId;
}
