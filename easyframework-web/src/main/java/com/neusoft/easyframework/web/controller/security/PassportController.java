package com.neusoft.easyframework.web.controller.security;

import com.neusoft.easyframework.business.security.entity.User;
import com.neusoft.easyframework.web.entity.JsonModel;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by neusoft on 15-5-12.
 */
@Controller
public class PassportController {

    @RequestMapping(value = "/login")
    public String login() {
        return "security/login";
    }

    @ResponseBody
    @RequestMapping(value = "/doLogin", method = RequestMethod.POST)
    public JsonModel doLogin(User user, HttpServletRequest request) {
        JsonModel jsonModel = new JsonModel();

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        String remember = WebUtils.getCleanParam(request, "rememberMe");

        try {
            if (StringUtils.isNotBlank(remember) && remember.equalsIgnoreCase("on")) {
                token.setRememberMe(true);
            }
            subject.login(token);

            jsonModel.setSuccess(true);
        } catch (UnknownAccountException ue) {
            token.clear();
            jsonModel.setMsg("用户名不存在");
        } catch (IncorrectCredentialsException ie) {
            token.clear();
            jsonModel.setMsg("用户名/密码错误");
        } catch (DisabledAccountException de) {
            token.clear();
            jsonModel.setMsg("用户被禁止使用");
        } catch (Exception e) {
            token.clear();
            jsonModel.setMsg("其他错误");
        }

        return jsonModel;
    }
}
