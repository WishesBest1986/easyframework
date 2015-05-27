package com.neusoft.easyframework.business.security.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by neusoft on 15-5-8.
 */
@Entity
@Table(name = "sec_menu")
public class Menu extends SecurityEntity {
    public static final Long ROOT_MENU = 0L;

    private String name;

    private String description;

    private Integer orderNum;

    private Menu parentMenu;

    private Resource resource;

    @JsonBackReference
    private List<Menu> subMenus = new ArrayList<Menu>();

    public Menu() {

    }

    public Menu(Long id) {
        this.id = id;
    }

    public Menu(Long id, String name, String description, Integer orderNum) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.orderNum = orderNum;
    }

    @Column(name = "name", nullable = false, length = 200)
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

    @Column(name = "ordernum")
    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    @ManyToOne
    @JoinColumn(name = "parentMenu")
    public Menu getParentMenu() {
        return parentMenu;
    }

    public void setParentMenu(Menu parentMenu) {
        this.parentMenu = parentMenu;
    }

    @ManyToOne
    @JoinColumn(name = "resource", nullable = true)
    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "parentMenu")
    public List<Menu> getSubMenus() {
        return subMenus;
    }

    public void setSubMenus(List<Menu> subMenus) {
        this.subMenus = subMenus;
    }
}
