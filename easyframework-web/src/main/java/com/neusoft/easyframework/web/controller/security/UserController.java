package com.neusoft.easyframework.web.controller.security;

import com.neusoft.easyframework.business.security.entity.User;
import com.neusoft.easyframework.business.security.service.UserService;
import com.neusoft.easyframework.core.orm.Page;
import com.neusoft.easyframework.core.orm.PropertyFilter;
import com.neusoft.easyframework.web.entity.GridModel;
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
}
