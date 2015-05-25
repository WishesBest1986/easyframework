package com.neusoft.easyframework.business.security.service;

import com.neusoft.easyframework.business.security.dao.MenuDao;
import com.neusoft.easyframework.business.security.entity.Menu;
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

//    /**
//     * 根据用户ID查询该用户允许访问的所有菜单列表
//     */
//    public List<Menu> getAllowedAccessMenu(Long userId) {
//        StringBuffer sqlBuffer = new StringBuffer();
//        // sec_menu表中定义且未关联资源表sec_resource的所有菜单列表 +
//        // sec_resource表中已关联且未设置权限的菜单列表 +
//        // sec_resource表中已关联且设置权限，并根据当前登录帐号拥有的权限的菜单列表
//        sqlBuffer.append("SELECT m.id, m.name, m.parent_menu, m.description, m.resource, m.ordernum FROM sec_menu m ");
//        sqlBuffer.append("LEFT JOIN sec_resource re ON m.resource = re.id ");
//        sqlBuffer.append("LEFT JOIN sec_authority_resource ar ON re.id = ar.resource_id ");
//        sqlBuffer.append("LEFT JOIN sec_authority a ON ar.authority_id = a.id ");
//        sqlBuffer.append("LEFT JOIN sec_role_authority ra ON a.id = ra.authority_id ");
//        sqlBuffer.append("LEFT JOIN sec_role r ON ra.role_id = r.id ");
//        sqlBuffer.append("LEFT JOIN sec_user_role ur ON r.id = ur.role_id ");
//        sqlBuffer.append("LEFT JOIN sec_user u ON ur.user_id = u.id ");
//        sqlBuffer.append("WHERE u.id = ? OR a.id IS NULL ");
//        sqlBuffer.append("ORDER BY m.ordernum");
//        String sql = sqlBuffer.toString();
//        SQLQuery query = menuDao.createSQLQuery(sql, userId);
//        query.addEntity(Menu.class);
//        return query.list();
//    }
}
