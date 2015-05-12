package com.neusoft.easyframework.business.security.service;

import com.neusoft.easyframework.business.security.dao.UserDao;
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
        String sql = "SELECT a.name FROM sec_user u " +
                "LEFT OUTER JOIN sec_user_role ur ON u.id = ur.user_id " +
                "LEFT OUTER JOIN sec_role r ON ur.role_id = r.id " +
                "LEFT OUTER JOIN sec_role_authority ra ON r.id = ra.role_id " +
                "LEFT OUTER JOIN sec_authority a ON ra.authority_id = a.id " +
                "WHERE u.id = ?";
        SQLQuery query = userDao.createSQLQuery(sql, userId);
        return query.list();
    }

    /**
     * 根据用户ID查询该用户拥有的角色列表
     * @param userId
     * @return
     */
    public List<String> getRolesName(Long userId) {
        String sql = "SELECT r.name FROM sec_user u " +
                "LEFT OUTER JOIN sec_user_role ur ON u.id = ur.user_id " +
                "LEFT OUTER JOIN sec_role r ON ur.role_id = r.id " +
                "WHERE u.id = ?";
        SQLQuery query = userDao.createSQLQuery(sql, userId);
        return query.list();
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
