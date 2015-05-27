package com.neusoft.easyframework.business.security.dao;

import com.neusoft.easyframework.business.security.entity.Role;
import com.neusoft.easyframework.core.orm.hibernate.HibernateDao;
import org.springframework.stereotype.Repository;

/**
 * Created by neusoft on 15-5-8.
 */
@Repository
public class RoleDao extends HibernateDao<Role, Long> {

}
