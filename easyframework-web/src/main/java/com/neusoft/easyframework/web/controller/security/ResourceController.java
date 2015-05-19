package com.neusoft.easyframework.web.controller.security;

import com.neusoft.easyframework.business.security.entity.Menu;
import com.neusoft.easyframework.business.security.entity.Resource;
import com.neusoft.easyframework.business.security.service.ResourceService;
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
 * Created by neusoft on 15-5-19.
 */
@Controller
@RequestMapping(value = "/security/resource")
public class ResourceController {
    @Autowired
    private ResourceService resourceService;

    @RequestMapping
    public String manager() {
        return "security/resourceManager";
    }

    @ResponseBody
    @RequestMapping(value = "list")
    public GridModel<Resource> list(Page<Resource> page, HttpServletRequest request) {
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);

        Page<Resource> resourcePage = resourceService.findPage(page, filters);

        GridModel<Resource> gridModel = new GridModel<Resource>();
        gridModel.setTotal(resourcePage.getTotalCount());
        gridModel.setRows(resourcePage.getResult());

        return gridModel;
    }

    @ResponseBody
    @RequestMapping(value = "doModify")
    public JsonModel doModify(Resource resource, Long menuId) {
        JsonModel jsonModel = new JsonModel();

        if (menuId != null && menuId.longValue() > 0) {
            Menu menu = new Menu(menuId);
            resource.setMenu(menu);
        }

        try {
            resourceService.save(resource);
            jsonModel.setSuccess(true);
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
            resourceService.delete(id);
            jsonModel.setSuccess(true);
        } catch (Exception e) {
            jsonModel.setMsg(e.getMessage());
        }

        return jsonModel;
    }
}
