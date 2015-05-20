package com.neusoft.easyframework.business.security.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by neusoft on 15-5-8.
 */
@Entity
@Table(name = "sec_authority")
public class Authority extends SecurityEntity {
    private String name;

    private String description;

    private List<Resource> resources = new ArrayList<Resource>();

    @JsonBackReference
    private List<Role> roles = new ArrayList<Role>();

    private List<User> users = new ArrayList<User>();

    public Authority() {

    }

    public Authority(Long id) {
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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "sec_authority_resource", joinColumns = {@JoinColumn(name = "authority_id")}, inverseJoinColumns = {@JoinColumn(name = "resource_id")})
    public List<Resource> getResources() {
        return resources;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }

    @ManyToMany(mappedBy = "authorities")
    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @ManyToMany(mappedBy = "authorities")
    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
