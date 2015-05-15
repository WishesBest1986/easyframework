package com.neusoft.easyframework.web.controller.security;

import com.neusoft.easyframework.business.security.entity.User;
import com.neusoft.easyframework.business.security.service.UserService;
import com.neusoft.easyframework.web.entity.GridModel;
import com.neusoft.easyframework.web.entity.JsonModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
        return "security/userList";
    }

    @ResponseBody
    @RequestMapping(value = "list")
    public GridModel<User> list() {
        Long total = 1L;
        List<User> users = userService.getAll();

        GridModel<User> gridModel = new GridModel<User>();
        gridModel.setTotal(total);
        gridModel.setRows(users);

        return gridModel;
    }
}
