package com.neusoft.easyframework.web.controller.security;

import com.neusoft.easyframework.business.security.entity.Menu;
import com.neusoft.easyframework.business.security.service.MenuService;
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
 * Created by neusoft on 15-5-18.
 */
@Controller
@RequestMapping("/security/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;

    @RequestMapping
    public String manager() {
        return "security/menuManager";
    }

    @ResponseBody
    @RequestMapping(value = "list")
    public GridModel<Menu> list(Page<Menu> page, HttpServletRequest request) {
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);

        Page<Menu> menuPage = menuService.findPage(page, filters);

        GridModel<Menu> gridModel = new GridModel<Menu>();
        gridModel.setTotal(menuPage.getTotalCount());
        gridModel.setRows(menuPage.getResult());

        return gridModel;
    }
}
