package com.neusoft.easyframework.business.security.service;

import com.neusoft.easyframework.business.security.dao.UserDao;
import com.neusoft.easyframework.business.security.entity.Menu;
import com.neusoft.easyframework.business.security.entity.Org;
import com.neusoft.easyframework.business.security.entity.User;
import com.neusoft.easyframework.core.orm.Page;
import com.neusoft.easyframework.core.orm.PropertyFilter;
import com.neusoft.easyframework.utils.DigestUtils;
import com.neusoft.easyframework.utils.EncodeUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by neusoft on 15-5-6.
 */
@Service
public class UserService {
    public static final String HASH_ALGORITHM = "SHA-1";
    public static final int HASH_INTERATIONS = 1024;
    private static final int SALT_SIZE = 8;

    @Autowired
    private UserDao userDao;

    public void save(User entity) {
        if (StringUtils.isNotBlank(entity.getPlainPassword())) {
            encryptPassword(entity);
        }
        userDao.save(entity);
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

    public boolean isUserNameUnique(String newUserName, String oldUserName) {
        return userDao.isPropertyUnique("username", newUserName, oldUserName);
    }

    public boolean isPasswordMatch(User user, String password) {
        byte[] salt = EncodeUtils.hexDecode(user.getSalt());
        byte[] hashPassword = DigestUtils.sha1(password.getBytes(), salt, HASH_INTERATIONS);
        String decodePassword = EncodeUtils.hexEncode(hashPassword);
        return decodePassword.equals(user.getPassword());
    }

    public List<User> getAll() {
        return userDao.getAll();
    }

    public Page<User> findPage(final Page<User> page, final List<PropertyFilter> filters) {
        return userDao.findPage(page, filters);
    }


    public List<User> getByOrg(Long orgId) {
        if (orgId == null || orgId == Org.ROOT_ORG_ID) {
            return getAll();
        } else {
            return userDao.find("FROM User u WHERE u.org = ?", new Org(orgId));
        }
    }

    public Page<User> findByOrgPage(final Page<User> page, Long orgId) {
        if (orgId == null || orgId == Org.ROOT_ORG_ID) {
            return userDao.findPage(page);
        } else {
            return userDao.findPage(page, "FROM User u WHERE u.org = ?", new Org(orgId));
        }
    }


    /**
     * 根据用户ID查询该用户所拥有的权限列表
     * @param userId
     * @return
     */
    public List<String> getAuthoritiesName(Long userId) {
//        String sql = "SELECT a.name FROM sec_user u " +
//                "LEFT OUTER JOIN sec_user_role ur ON u.id = ur.user_id " +
//                "LEFT OUTER JOIN sec_role r ON ur.role_id = r.id " +
//                "LEFT OUTER JOIN sec_role_authority ra ON r.id = ra.role_id " +
//                "LEFT OUTER JOIN sec_authority a ON ra.authority_id = a.id " +
//                "WHERE u.id = ?";
        SQLQuery query = null;

        User user = userDao.findUniqueBy("id", userId);

        // 对于保留用户，拥有所有权限
        if (user != null && user.isReserved()) {
            String sql = "SELECT name FROM sec_authority";
            query = userDao.createSQLQuery(sql);
        } else {
            String sql = "SELECT a.name FROM sec_authority a " +
                    "JOIN sec_role_authority ra ON a.id = ra.authority_id " +
                    "JOIN sec_role r ON ra.role_id = r.id " +
                    "JOIN sec_user_role ur ON r.id = ur.role_id " +
                    "JOIN sec_user u ON ur.user_id = u.id " +
                    "WHERE u.id = ?";
            query = userDao.createSQLQuery(sql, userId);
        }

        List<String> result = null;
        if (query != null) {
            result = query.list();
        }
        return result;
    }

    /**
     * 根据用户ID查询该用户拥有的角色列表
     * @param userId
     * @return
     */
    public List<String> getRolesName(Long userId) {
//        String sql = "SELECT r.name FROM sec_user u " +
//                "LEFT OUTER JOIN sec_user_role ur ON u.id = ur.user_id " +
//                "LEFT OUTER JOIN sec_role r ON ur.role_id = r.id " +
//                "WHERE u.id = ?";
        String sql = "SELECT r.name FROM sec_role r " +
                "JOIN sec_user_role ur ON r.id = ur.role_id " +
                "JOIN sec_user u ON ur.user_id = u.id " +
                "WHERE u.id = ?";
        SQLQuery query = userDao.createSQLQuery(sql, userId);
        return query.list();
    }

    /**
     * 根据用户ID查询该用户允许访问的所有菜单列表
     */
    public List<Menu> getAllowedAccessMenu(Long userId) {
        SQLQuery query = null;

        User user = userDao.findUniqueBy("id", userId);

        // 对于保留用户，拥有所有权限
        if (user != null && user.isReserved()) {
            String sql = "SELECT m.id, m.name, m.parent_menu, m.description, m.resource, m.ordernum FROM sec_menu m ORDER BY m.ordernum";
            query = userDao.createSQLQuery(sql);
        } else {
            StringBuffer sqlBuffer = new StringBuffer();
            // sec_menu表中定义且未关联资源表sec_resource的所有菜单列表 +
            // sec_resource表中已关联且未设置权限的菜单列表 +
            // sec_resource表中已关联且设置权限，并根据当前登录帐号拥有的权限的菜单列表
            sqlBuffer.append("SELECT m.id, m.name, m.parent_menu, m.description, m.resource, m.ordernum FROM sec_menu m ");
            sqlBuffer.append("LEFT JOIN sec_resource re ON m.resource = re.id ");
            sqlBuffer.append("LEFT JOIN sec_authority_resource ar ON re.id = ar.resource_id ");
            sqlBuffer.append("LEFT JOIN sec_authority a ON ar.authority_id = a.id ");
            sqlBuffer.append("LEFT JOIN sec_role_authority ra ON a.id = ra.authority_id ");
            sqlBuffer.append("LEFT JOIN sec_role r ON ra.role_id = r.id ");
            sqlBuffer.append("LEFT JOIN sec_user_role ur ON r.id = ur.role_id ");
            sqlBuffer.append("LEFT JOIN sec_user u ON ur.user_id = u.id ");
            sqlBuffer.append("WHERE u.id = ? OR a.id IS NULL ");
            sqlBuffer.append("ORDER BY m.ordernum");
            String sql = sqlBuffer.toString();
            query = userDao.createSQLQuery(sql, userId);
        }

        List<Menu> result = null;
        if (query != null) {
            query.addEntity(Menu.class);
            result = query.list();
        }
        return result;
    }

    /**
     * 设定安全的密码，生成随机的salt并经过1024次sha-1 hash
     */
    private void encryptPassword(User user) {
        byte[] salt = DigestUtils.generateSalt(SALT_SIZE);
        user.setSalt(EncodeUtils.hexEncode(salt));

        byte[] hashPassword = DigestUtils.sha1(user.getPlainPassword().getBytes(), salt, HASH_INTERATIONS);
        user.setPassword(EncodeUtils.hexEncode(hashPassword));
    }
}
