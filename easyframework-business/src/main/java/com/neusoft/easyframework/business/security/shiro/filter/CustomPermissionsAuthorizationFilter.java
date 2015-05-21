package com.neusoft.easyframework.business.security.shiro.filter;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义权限鉴权过滤器，扩展AJAX异步请求认证功能
 * Created by neusoft on 15-5-21.
 */
public class CustomPermissionsAuthorizationFilter extends PermissionsAuthorizationFilter {
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {

        Subject subject = getSubject(request, response);
        // If the subject isn't identified, redirect to login URL
        if (subject.getPrincipal() == null) {
            saveRequestAndRedirectToLogin(request, response);
        } else {
            if (com.neusoft.easyframework.utils.WebUtils.isAjax((HttpServletRequest) request)) {
                WebUtils.toHttp(response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
            } else {
                // If subject is known but not authorized, redirect to the unauthorized URL if there is one
                // If no unauthorized URL is specified, just return an unauthorized HTTP status code
                String unauthorizedUrl = getUnauthorizedUrl();
                //SHIRO-142 - ensure that redirect _or_ error code occurs - both cannot happen due to response commit:
                if (StringUtils.hasText(unauthorizedUrl)) {
                    WebUtils.issueRedirect(request, response, unauthorizedUrl);
                } else {
                    WebUtils.toHttp(response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
                }
            }
        }
        return false;

    }
}
