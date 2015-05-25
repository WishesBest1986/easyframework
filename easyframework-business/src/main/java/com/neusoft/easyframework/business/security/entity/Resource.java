package com.neusoft.easyframework.business.security.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by neusoft on 15-5-8.
 */
@Entity
@Table(name = "sec_resource")
public class Resource extends SecurityEntity {
    private String name;

    private String source;

    @JsonBackReference
    private List<Authority> authorities = new ArrayList<Authority>();

    public Resource() {

    }

    public Resource(Long id) {
        this.id = id;
    }

    @Column(name = "name", unique = true, nullable = false, length = 200)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "source")
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @ManyToMany(mappedBy = "resources")
    public List<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }
}
