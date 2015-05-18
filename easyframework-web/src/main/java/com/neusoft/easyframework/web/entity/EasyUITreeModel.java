package com.neusoft.easyframework.web.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by neusoft on 15-5-18.
 */
public class EasyUITreeModel implements Serializable {
    private String id;
    private String pid;
    private String iconCls;
    private String text;
    private String state = "open"; // open/closed
    private Object attributes;
    private List<EasyUITreeModel> children = new ArrayList<EasyUITreeModel>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Object getAttributes() {
        return attributes;
    }

    public void setAttributes(Object attributes) {
        this.attributes = attributes;
    }

    public List<EasyUITreeModel> getChildren() {
        return children;
    }

    public void setChildren(List<EasyUITreeModel> children) {
        this.children = children;
    }
}
