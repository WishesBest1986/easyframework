package com.neusoft.easyframework.business.security.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by neusoft on 15-5-6.
 */
@Entity
@Table(name = "sec_user")
public class User extends SecurityEntity {
    private String username;

    @Column(name = "username", unique = true, nullable = false, length = 50)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
