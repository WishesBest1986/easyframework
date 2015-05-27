package com.neusoft.easyframework.web.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by neusoft on 15-5-15.
 */
public class GridModel<T> implements Serializable {
    private Long total = 0L;

    private List<T> rows = new ArrayList<T>();

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
