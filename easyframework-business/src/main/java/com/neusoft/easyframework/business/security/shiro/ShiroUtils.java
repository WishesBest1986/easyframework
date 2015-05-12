package com.neusoft.easyframework.business.security.shiro;

import com.neusoft.easyframework.business.security.entity.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 * Created by neusoft on 15-5-12.
 */
public class ShiroUtils {
    /**
     * 获取当前登录的认证实体
     * @return
     */
    public static ShiroPrincipal getPrincipal() {
        Subject subject = SecurityUtils.getSubject();
        return (ShiroPrincipal)subject.getPrincipal();
    }

    /**
     * 获取当前登录的认证实体ID
     * @return
     */
    public static Long getUserId() {
        ShiroPrincipal principal = getPrincipal();
        if (principal != null) {
            return principal.getId();
        }
        return -1L;
    }

    /**
     * 获取当前登录的认证实体用户
     * @return
     */
    public static User getUser() {
        ShiroPrincipal principal = getPrincipal();
        if (principal != null) {
            return principal.getUser();
        }
        return null;
    }
}
