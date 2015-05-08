package com.neusoft.easyframework.business.security.service;

import com.neusoft.easyframework.business.security.dao.AuthorityDao;
import com.neusoft.easyframework.business.security.dao.ResourceDao;
import com.neusoft.easyframework.business.security.entity.Authority;
import com.neusoft.easyframework.business.security.entity.Resource;
import com.neusoft.easyframework.core.orm.Page;
import com.neusoft.easyframework.core.orm.PropertyFilter;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by neusoft on 15-5-8.
 */
@Service
public class ResourceService {
    @Autowired
    private ResourceDao resourceDao;
    @Autowired
    private AuthorityDao authorityDao;

    public void save(Resource entity) {
        resourceDao.save(entity);
    }

    public void delete(Long id) {
        List<Authority> authorities = authorityDao.getAll();
        for (Authority authority : authorities) {
            for (Resource resource : authority.getResources()) {
                if (resource.getId().longValue() == id.longValue()) {
                    authority.getResources().remove(resource);
                    authorityDao.save(authority);

                    break;
                }
            }
        }

        resourceDao.delete(id);
    }

    public Resource get(Long id) {
        return resourceDao.get(id);
    }

    public List<Resource> getAll() {
        return resourceDao.getAll();
    }

    public Page<Resource> findPage(final Page<Resource> page, final List<PropertyFilter> filters) {
        return resourceDao.findPage(page, filters);
    }

    /**
     * 根据用户ID查询该用户具有权限访问的资源与不需要授权的资源
     */
    public List<Resource> getAuthorizedResource(Long userId) {
        String sql = "";
        SQLQuery query = resourceDao.createSQLQuery(sql, userId);
        query.addEntity(Resource.class);
        return query.list();
    }

}
