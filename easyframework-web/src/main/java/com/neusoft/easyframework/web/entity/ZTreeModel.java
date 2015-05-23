package com.neusoft.easyframework.web.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by neusoft on 15-5-23.
 */
public class ZTreeModel implements Serializable {
    private Long id;
    private Long pId;
    private String name;
    private String url;
    private String icon;
    private boolean open;
    private boolean checked;
    private String href;
    private List<ZTreeModel> children = new ArrayList<ZTreeModel>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getpId() {
        return pId;
    }

    public void setpId(Long pId) {
        this.pId = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public List<ZTreeModel> getChildren() {
        return children;
    }

    public void setChildren(List<ZTreeModel> children) {
        this.children = children;
    }
}
