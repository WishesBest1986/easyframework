package com.neusoft.easyframework.business.security.service;

import com.neusoft.easyframework.business.security.dao.RoleDao;
import com.neusoft.easyframework.business.security.dao.UserDao;
import com.neusoft.easyframework.business.security.entity.Role;
import com.neusoft.easyframework.business.security.entity.User;
import com.neusoft.easyframework.core.orm.Page;
import com.neusoft.easyframework.core.orm.PropertyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by neusoft on 15-5-8.
 */
@Service
public class RoleService {
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private UserDao userDao;

    public void save(Role entity) {
        roleDao.save(entity);
    }

    public void delete(Long id) {
        List<User> users = userDao.getAll();
        for (User user : users) {
            for (Role role : user.getRoles()) {
                if (role.getId().longValue() == id.longValue()) {
                    user.getRoles().remove(role);
                    userDao.save(user);

                    break;
                }
            }
        }
        roleDao.delete(id);
    }

    public Role get(Long id) {
        return roleDao.get(id);
    }

    public List<Role> getAll() {
        return roleDao.getAll();
    }

    public Page<Role> findPage(final Page<Role> page, final List<PropertyFilter> filters) {
        return roleDao.findPage(page, filters);
    }
}
