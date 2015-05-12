package com.neusoft.easyframework.business.security.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by neusoft on 15-5-6.
 */
@Entity
@Table(name = "sec_user")
public class User extends SecurityEntity {
    private String username;

    private String password;

    private String plainPassword;

    private String salt;

    private String fullname;

    private int type;

    private boolean enabled;

    private Org org;

    private List<Role> roles = new ArrayList<Role>();

    private List<Authority> authorities = new ArrayList<Authority>();

    public User() {

    }

    @Column(name = "username", unique = true, nullable = false, length = 50)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "password", length = 50)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "enabled")
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Transient
    public String getPlainPassword() {
        return plainPassword;
    }

    public void setPlainPassword(String plainPassword) {
        this.plainPassword = plainPassword;
    }

    @Column(name = "salt")
    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Column(name = "fullname", length = 100)
    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    @Column(name = "type")
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @ManyToOne
    @JoinColumn(name = "org", nullable = true)
    public Org getOrg() {
        return org;
    }

    public void setOrg(Org org) {
        this.org = org;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "sec_user_role", joinColumns = {@JoinColumn(name = "user_id")}, inverseJoinColumns = {@JoinColumn(name = "role_id")})
    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "sec_user_authority", joinColumns = {@JoinColumn(name = "user_id")}, inverseJoinColumns = {@JoinColumn(name = "authority_id")})
    public List<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }
}
