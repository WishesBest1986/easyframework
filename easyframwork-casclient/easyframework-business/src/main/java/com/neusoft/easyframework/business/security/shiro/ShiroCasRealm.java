package com.neusoft.easyframework.business.security.shiro;

import com.neusoft.easyframework.business.security.entity.User;
import com.neusoft.easyframework.business.security.service.UserService;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cas.CasRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by neusoft on 15-5-26.
 */
public class ShiroCasRealm extends CasRealm {
    private static Logger logger = LoggerFactory.getLogger(ShiroCasRealm.class);

    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

//        ShiroPrincipal principal = (ShiroPrincipal)super.getAvailablePrincipal(principalCollection);
//        String username = principal.getUser().getUsername();
//        Long userId = principal.getId();
        String username = (String) super.getAvailablePrincipal(principalCollection);
        User user = userService.findUserByName(username);
        Long userId = user.getId();
        ShiroPrincipal principal = new ShiroPrincipal(user);
        logger.info("用户【" + username + "】授权开始...");
        try {
            List<String> authorities = userService.getAuthoritiesName(userId);
            List<String> roles = userService.getRolesName(userId);
            principal.setAuthorities(authorities);
            principal.setRoles(roles);

            logger.info("用户【" + username + "】授权初始化成功...");
            logger.info("用户【" + username + "】角色列表为:" + roles);
            logger.info("用户【" + username + "】权限列表为:" + authorities);
        } catch (RuntimeException e) {
            throw new AuthorizationException("用户【" + username + "】授权失败");
        }

        info.addStringPermissions(principal.getAuthorities());
        info.addRoles(principal.getRoles());

        return info;
    }
}
