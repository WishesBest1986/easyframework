package com.neusoft.easyframework.business.security.service;

import com.neusoft.easyframework.business.security.dao.MenuDao;
import com.neusoft.easyframework.business.security.dao.ResourceDao;
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
public class MenuService {
    @Autowired
    private MenuDao menuDao;
    @Autowired
    private ResourceDao resourceDao;

    public void save(Menu entity) {
        menuDao.save(entity);
    }

    public void delete(Long id) {
        // unbind resource menuId
        String hql = "FROM Resource as r WHERE r.menu.id = ?";
        Resource resource = resourceDao.findUnique(hql, id);
        if (resource != null) {
            resource.setMenu(null);
            resourceDao.save(resource);
        }

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

    /**
     * 根据用户ID查询该用户允许访问的所有菜单列表
     */
    public List<Menu> getAllowedAccessMenu(Long userId) {
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append("SELECT * FROM( ");
        // 获取sec_menu表中定义且未关联资源表sec_resource的所有菜单列表
        sqlBuffer.append("SELECT m.id, m.name, m.parent_menu, m.description, m.ordernum from sec_menu m ");
        sqlBuffer.append("WHERE NOT EXISTS (SELECT re.id FROM sec_resource re WHERE re.menu = m.id) ");
        sqlBuffer.append("UNION ALL ");
        // 获取sec_resource表中已关联且未设置权限的菜单列表
        sqlBuffer.append("SELECT m.id, m.name, m.parent_menu, re.source as description, m.ordernum FROM sec_resource re ");
        sqlBuffer.append("LEFT OUTER JOIN sec_menu m ON re.menu = m.id ");
        sqlBuffer.append("WHERE re.menu IS NOT NULL AND NOT EXISTS (SELECT ar.authority_id from sec_authority_resource ar WHERE ar.resource_id = re.id) ");
        sqlBuffer.append("UNION ALL ");
        // 获取sec_resource表中已关联且设置权限，并根据当前登录帐号拥有的权限的菜单列表
        sqlBuffer.append("SELECT m.id, m.name, m.parent_menu, re.source as description, m.ordernum FROM sec_user u ");
        sqlBuffer.append("LEFT OUTER JOIN sec_user_role ur ON u.id = ur.user_id ");
        sqlBuffer.append("LEFT OUTER JOIN sec_role r ON ur.role_id = r.id ");
        sqlBuffer.append("LEFT OUTER JOIN sec_role_authority ra ON r.id = ra.role_id ");
        sqlBuffer.append("LEFT OUTER JOIN sec_authority a ON ra.authority_id = a.id ");
        sqlBuffer.append("LEFT OUTER JOIN sec_authority_resource ar ON a.id = ar.authority_id ");
        sqlBuffer.append("LEFT OUTER JOIN sec_resource re ON ar.resource_id = re.id ");
        sqlBuffer.append("LEFT OUTER JOIN sec_menu m ON re.menu = m.id ");
        sqlBuffer.append("WHERE u.id = ? AND re.menu IS NOT NULL ");
        sqlBuffer.append(") tbl ORDER BY ordernum");
        String sql = sqlBuffer.toString();
        SQLQuery query = menuDao.createSQLQuery(sql, userId);
        query.addEntity(Menu.class);
        return query.list();
    }
}
