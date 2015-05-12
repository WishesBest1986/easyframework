package com.neusoft.easyframework.business.security.shiro;

import com.neusoft.easyframework.business.security.entity.User;
import com.neusoft.easyframework.business.security.service.UserService;
import com.neusoft.easyframework.utils.EncodeUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.List;


/**
 * Created by neusoft on 15-5-12.
 */
public class ShiroAuthorizingRealm extends AuthorizingRealm {
    private static Logger logger = LoggerFactory.getLogger(ShiroAuthorizingRealm.class);

    public ShiroAuthorizingRealm() {
        super();
        setAuthenticationTokenClass(UsernamePasswordToken.class);
    }

    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        ShiroPrincipal principal = (ShiroPrincipal)super.getAvailablePrincipal(principalCollection);
        String username = principal.getUser().getUsername();
        Long userId = principal.getId();
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

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken)authenticationToken;
        String username = token.getUsername();
        if (username == null) {
            logger.warn("用户名不能为空");
            throw new AccountException("用户名不能为空");
        }

        User user = null;
        try {
            user = userService.findUserByName(username);
        } catch (Exception e) {
            logger.warn("获取用户失败\n" + e.getMessage());
        }

        if (user == null) {
            logger.warn("用户不存在");
            throw new UnknownAccountException("用户不存在");
        }

        if (!user.isEnabled()) {
            logger.warn("用户被禁止使用");
            throw new UnknownAccountException("用户被禁止使用");
        }

        logger.info("用户【" + username + "】登录成功");

        byte[] salt = EncodeUtils.hexDecode(user.getSalt());
        ShiroPrincipal principal = new ShiroPrincipal(user);
        List<String> authorities = userService.getAuthoritiesName(user.getId());
        List<String> roles = userService.getRolesName(user.getId());
        principal.setAuthorities(authorities);
        principal.setRoles(roles);
        return new SimpleAuthenticationInfo(principal, user.getPassword(), ByteSource.Util.bytes(salt), getName());
    }

    @PostConstruct
    public void initCredentialsMatcher() {
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(UserService.HASH_ALGORITHM);
        matcher.setHashIterations(UserService.HASH_INTERATIONS);
        setCredentialsMatcher(matcher);
    }
}
