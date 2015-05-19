package com.neusoft.easyframework.business.security.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by neusoft on 15-5-7.
 */
@Entity
@Table(name = "sec_org")
public class Org extends SecurityEntity {
    public static final Long ROOT_ORG_ID = 0L;

    private String name;

    private String active;

    private String description;

    private String type;

    private Org parentOrg;

    @JsonBackReference
    private List<Org> orgs = new ArrayList<Org>();

    private List<User> users = new ArrayList<User>();

    public Org() {

    }

    public Org(Long id) {
        this.id = id;
    }

    @Column(name = "name", nullable = false, length = 200)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "active")
    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    @Column(name = "description", length = 500)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "type", length = 200)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @ManyToOne
    @JoinColumn(name = "parentOrg", nullable = true)
    public Org getParentOrg() {
        return parentOrg;
    }

    public void setParentOrg(Org parentOrg) {
        this.parentOrg = parentOrg;
    }

    @OneToMany(mappedBy = "parentOrg", cascade = CascadeType.ALL)
    public List<Org> getOrgs() {
        return orgs;
    }

    public void setOrgs(List<Org> orgs) {
        this.orgs = orgs;
    }

    @OneToMany(mappedBy = "org", cascade = CascadeType.ALL)
    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
