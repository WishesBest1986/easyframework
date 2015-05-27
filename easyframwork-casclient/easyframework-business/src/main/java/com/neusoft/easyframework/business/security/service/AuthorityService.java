package com.neusoft.easyframework.business.security.service;

import com.neusoft.easyframework.business.security.dao.AuthorityDao;
import com.neusoft.easyframework.business.security.dao.RoleDao;
import com.neusoft.easyframework.business.security.dao.UserDao;
import com.neusoft.easyframework.business.security.entity.Authority;
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
public class AuthorityService {
    @Autowired
    private AuthorityDao authorityDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private UserDao userDao;

    public void save(Authority entity) {
        authorityDao.save(entity);
    }

    public void delete(Long id) {
        List<Role> roles = roleDao.getAll();
        for (Role role : roles) {
            for (Authority authority : role.getAuthorities()) {
                if (authority.getId().longValue() == id.longValue()) {
                    role.getAuthorities().remove(authority);
                    roleDao.save(role);

                    break;
                }
            }
        }

        List<User> users = userDao.getAll();
        for (User user : users) {
            for (Authority authority : user.getAuthorities()) {
                if (authority.getId().longValue() == id.longValue()) {
                    user.getAuthorities().remove(authority);
                    userDao.save(user);

                    break;
                }
            }
        }

        authorityDao.delete(id);
    }

    public Authority get(Long id) {
        return authorityDao.get(id);
    }

    public List<Authority> getAll() {
        return authorityDao.getAll();
    }

    public Page<Authority> findPage(final Page<Authority> page, final List<PropertyFilter> filters) {
        return authorityDao.findPage(page, filters);
    }
}
