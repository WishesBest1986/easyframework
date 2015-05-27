package com.neusoft.easyframework.business.security.dao;

import com.neusoft.easyframework.business.security.entity.User;
import com.neusoft.easyframework.core.orm.hibernate.HibernateDao;
import org.springframework.stereotype.Repository;

/**
 * Created by neusoft on 15-5-6.
 */
@Repository
public class UserDao extends HibernateDao<User, Long> {

}
