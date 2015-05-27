package com.neusoft.easyframework.business.security.shiro;

import com.neusoft.easyframework.business.security.entity.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by neusoft on 15-5-12.
 */
public class ShiroPrincipal implements Serializable {
    private User user;
    private List<String> authorities = new ArrayList<String>();
    private List<String> roles = new ArrayList<String>();

    public ShiroPrincipal(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }

    public Long getId() {
        return this.user.getId();
    }

    /**
     * <shiro:principal /> 标签显示使用
     */
    @Override
    public String toString() {
        return this.user.getFullname();
    }
}
