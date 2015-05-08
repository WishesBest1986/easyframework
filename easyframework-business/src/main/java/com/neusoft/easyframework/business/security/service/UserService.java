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

    public void delete(Long id) {
        userDao.delete(id);
    }

    public User get(Long id) {
        return userDao.get(id);
    }

    public User findUserByName(String username) {
        return userDao.findUniqueBy("username", username);
    }

    public List<User> getAll() {
        return userDao.getAll();
    }

    public Page<User> findPage(Page<User> page, List<PropertyFilter> filters) {
        return userDao.findPage(page, filters);
    }
}
