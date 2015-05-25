package com.neusoft.easyframework.business.security.service;

import com.neusoft.easyframework.business.security.dao.AuthorityDao;
import com.neusoft.easyframework.business.security.dao.MenuDao;
import com.neusoft.easyframework.business.security.dao.ResourceDao;
import com.neusoft.easyframework.business.security.entity.Authority;
import com.neusoft.easyframework.business.security.entity.Menu;
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
    private MenuDao menuDao;
    @Autowired
    private AuthorityDao authorityDao;


    public void save(Resource entity) {
        resourceDao.save(entity);
    }

    public void delete(Long id) {
        List<Menu> menus = menuDao.getAll();
        for (Menu menu : menus) {
            if (menu.getResource().getId().longValue() == id.longValue()) {
                menu.setResource(null);
                menuDao.save(menu);
            }
        }

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
     * 根据用户ID查询该用户具有权限访问的资源与不需要授权的菜单资源
     */
    public List<Resource> getAuthorizedMenuResource(Long userId) {
        String sql = "SELECT re.id, re.name, re.source, re.menu FROM sec_user u " +
                "LEFT OUTER JOIN sec_role_user ru ON u.id = ru.user_id " +
                "LEFT OUTER JOIN sec_role r ON ru.role_id = r.id " +
                "LEFT OUTER JOIN sec_role_authority ra ON r.id = ra.role_id " +
                "LEFT OUTER JOIN sec_authority a ON ra.authority_id = a.id " +
                "LEFT OUTER JOIN sec_authority_resource ar ON a.id = ar.authority_id " +
                "LEFT OUTER JOIN sec_resource re ON ar.resource_id = re.id " +
                "WHERE u.id = ? AND re.menu IS NOT NULL" +
                "UNION ALL " +
                "(SELECT re.id, re.name, re.source, re.menu FROM sec_resource re " +
                "WHERE re.menu IS NOT NULL AND NOT EXISTS( " +
                "SELECT ar.authority_id FROM sec_authority_resource ar WHERE ar.resource_id = re.id))";
        SQLQuery query = resourceDao.createSQLQuery(sql, userId);
        query.addEntity(Resource.class);
        return query.list();
    }

}
