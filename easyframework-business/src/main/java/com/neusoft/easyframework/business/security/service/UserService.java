package com.neusoft.easyframework.business.security.service;

import com.neusoft.easyframework.business.security.dao.UserDao;
import com.neusoft.easyframework.business.security.entity.User;
import com.neusoft.easyframework.core.orm.Page;
import com.neusoft.easyframework.core.orm.PropertyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by neusoft on 15-5-6.
 */
@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    public void save(User user) {
        userDao.save(user);
    }

    public User get(Long id) {
        return userDao.get(id);
    }

    public User findUserByName(String username) {
        return userDao.findUniqueBy("username", username);
    }

    public Page<User> findPage(final Page<User> page, final List<PropertyFilter> filters) {
        return userDao.findPage(page, filters);
    }
}
