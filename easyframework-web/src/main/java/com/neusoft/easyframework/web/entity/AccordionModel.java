package com.neusoft.easyframework.web.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by neusoft on 15-5-23.
 */
public class AccordionModel implements Serializable {
    private String id;
    private String title;
    private List<ZTreeModel> treeModels = new ArrayList<ZTreeModel>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ZTreeModel> getTreeModels() {
        return treeModels;
    }

    public void setTreeModels(List<ZTreeModel> treeModels) {
        this.treeModels = treeModels;
    }
}
