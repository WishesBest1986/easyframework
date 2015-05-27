package com.neusoft.easyframework.web.controller.security;

import com.neusoft.easyframework.business.security.entity.Org;
import com.neusoft.easyframework.business.security.entity.Role;
import com.neusoft.easyframework.business.security.entity.User;
import com.neusoft.easyframework.business.security.service.UserService;
import com.neusoft.easyframework.core.orm.Page;
import com.neusoft.easyframework.core.orm.PropertyFilter;
import com.neusoft.easyframework.web.entity.GridModel;
import com.neusoft.easyframework.web.entity.JsonModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by neusoft on 15-5-15.
 */
@Controller
@RequestMapping(value = "/security/user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping
    public String manager() {
        return "security/userManager";
    }

    @ResponseBody
    @RequestMapping(value = "list")
    public GridModel<User> list(Page<User> page, HttpServletRequest request) {
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);

        Page<User> userPage = userService.findPage(page, filters);

        GridModel<User> gridModel = new GridModel<User>();
        gridModel.setTotal(userPage.getTotalCount());
        gridModel.setRows(userPage.getResult());

        return gridModel;
    }

    @ResponseBody
    @RequestMapping(value = "doModify")
    public JsonModel doModify(User user, Long orgId, Long[] roleIds) {
        JsonModel jsonModel = new JsonModel();

        if (orgId != null && orgId.longValue() > 0) {
            Org org = new Org(orgId);
            user.setOrg(org);
        }

        if (roleIds != null) {
            for (Long roleId : roleIds) {
                if (roleId != null && roleId.longValue() > 0) {
                    Role role = new Role(roleId);
                    user.getRoles().add(role);
                }
            }
        }

        try {
            User editUser = null;
            if (user.getId() != null) {
                editUser = userService.get(user.getId()); // 防止编辑丢失其他关联信息

                if (userService.isUserNameUnique(user.getUsername(), editUser.getUsername())) {
                    editUser.setUsername(user.getUsername());
                    editUser.setFullname(user.getFullname());
                    editUser.setPlainPassword(user.getPlainPassword());
                    editUser.setEnabled(user.isEnabled());
                    editUser.setOrg(user.getOrg());
                    editUser.setRoles(user.getRoles());

                    userService.save(editUser);
                    jsonModel.setSuccess(true);
                } else {
                    jsonModel.setMsg("登录名已经被使用");
                }
            } else {
                if (userService.isUserNameUnique(user.getUsername(), null)) {
                    editUser = user;

                    userService.save(editUser);
                    jsonModel.setSuccess(true);
                } else {
                    jsonModel.setMsg("登录名已经被使用");
                }
            }
        } catch (Exception e) {
            jsonModel.setMsg(e.getMessage());
        }

        return jsonModel;
    }

    @ResponseBody
    @RequestMapping(value = "doDelete")
    public JsonModel doDelete(Long id) {
        JsonModel jsonModel = new JsonModel();

        try {
            User user = userService.get(id);
            if (user.isReserved()) {
                jsonModel.setMsg("保留用户不允许删除");
            } else {
                userService.delete(id);
                jsonModel.setSuccess(true);
            }
        } catch (Exception e) {
            jsonModel.setMsg(e.getMessage());
        }

        return jsonModel;
    }
}
