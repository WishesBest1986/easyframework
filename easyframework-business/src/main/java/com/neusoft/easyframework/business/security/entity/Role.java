package com.neusoft.easyframework.business.security.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by neusoft on 15-5-8.
 */
@Entity
@Table(name = "sec_role")
public class Role extends SecurityEntity {
    private String name;

    private String description;

    @JsonBackReference
    private List<User> users = new ArrayList<User>();

    @JsonBackReference
    private List<Authority> authorities = new ArrayList<Authority>();

    public Role() {

    }

    public Role(Long id) {
        this.id = id;
    }

    @Column(name = "name", unique = true, nullable = false, length = 200)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "description", length = 500)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ManyToMany(mappedBy = "roles")
    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "sec_role_authority", joinColumns = {@JoinColumn(name = "role_id")}, inverseJoinColumns = {@JoinColumn(name = "authority_id")})
    public List<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }
}
