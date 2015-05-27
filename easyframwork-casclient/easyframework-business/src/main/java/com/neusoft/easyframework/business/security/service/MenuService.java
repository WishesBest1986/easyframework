package com.neusoft.easyframework.business.security.service;

import com.neusoft.easyframework.business.security.dao.MenuDao;
import com.neusoft.easyframework.business.security.entity.Menu;
import com.neusoft.easyframework.core.orm.Page;
import com.neusoft.easyframework.core.orm.PropertyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by neusoft on 15-5-8.
 */
@Service
public class MenuService {
    @Autowired
    private MenuDao menuDao;

    public void save(Menu entity) {
        menuDao.save(entity);
    }

    public void delete(Long id) {
        menuDao.delete(id);
    }

    public Menu get(Long id) {
        return menuDao.get(id);
    }

    public List<Menu> getAll() {
        return menuDao.getAll();
    }

    public Page<Menu> findPage(final Page<Menu> page, final List<PropertyFilter> filters) {
        return menuDao.findPage(page, filters);
    }
}
