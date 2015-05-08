package com.neusoft.easyframework.business.security.entity;

import javax.persistence.*;

/**
 * Created by neusoft on 15-5-8.
 */
@Entity
@Table(name = "sec_resource")
public class Resource extends SecurityEntity {
    private String name;

    private String source;

    private Menu menu;

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

    @ManyToOne
    @JoinColumn(name = "menu", nullable = true)
    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }
}
