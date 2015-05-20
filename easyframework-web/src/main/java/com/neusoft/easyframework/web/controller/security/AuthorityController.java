package com.neusoft.easyframework.web.controller.security;

import com.neusoft.easyframework.business.security.entity.Authority;
import com.neusoft.easyframework.business.security.entity.Resource;
import com.neusoft.easyframework.business.security.service.AuthorityService;
import com.neusoft.easyframework.core.orm.Page;
import com.neusoft.easyframework.core.orm.PropertyFilter;
import com.neusoft.easyframework.web.entity.EasyUITreeModel;
import com.neusoft.easyframework.web.entity.GridModel;
import com.neusoft.easyframework.web.entity.JsonModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by neusoft on 15-5-20.
 */
@Controller
@RequestMapping(value = "/security/authority")
public class AuthorityController {
    @Autowired
    private AuthorityService authorityService;

    @RequestMapping
    public String manager() {
        return "security/authorityManager";
    }

    @ResponseBody
    @RequestMapping(value = "list")
    public GridModel<Authority> list(Page<Authority> page, HttpServletRequest request) {
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);

        Page<Authority> authorityPage = authorityService.findPage(page, filters);

        GridModel<Authority> gridModel = new GridModel<Authority>();
        gridModel.setTotal(authorityPage.getTotalCount());
        gridModel.setRows(authorityPage.getResult());

        return gridModel;
    }

    @ResponseBody
    @RequestMapping(value = "doModify")
    public JsonModel doModify(Authority authority, Long[] resourceIds) {
        JsonModel jsonModel = new JsonModel();

        if (resourceIds != null) {
            for (Long resourceId : resourceIds) {
                if (resourceId != null && resourceId.longValue() > 0) {
                    Resource resource = new Resource(resourceId);
                    authority.getResources().add(resource);
                }
            }
        }

        try {
            Authority editAuthority = null;
            if (authority.getId() != null) {
                editAuthority = authorityService.get(authority.getId()); // 防止编辑丢失其他关联信息

                editAuthority.setName(authority.getName());
                editAuthority.setDescription(authority.getDescription());
                editAuthority.setResources(authority.getResources());
            } else {
                editAuthority = authority;
            }

            authorityService.save(editAuthority);
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
            authorityService.delete(id);
            jsonModel.setSuccess(true);
        } catch (Exception e) {
            jsonModel.setMsg(e.getMessage());
        }

        return jsonModel;
    }

    @ResponseBody
    @RequestMapping(value = "allTree")
    public List<EasyUITreeModel> allTree() {
        List<EasyUITreeModel> treeModelList = new ArrayList<EasyUITreeModel>();

        EasyUITreeModel rootTreeModel = new EasyUITreeModel();
        rootTreeModel.setId("");
        rootTreeModel.setText("全选");
        treeModelList.add(rootTreeModel);

        List<Authority> authorities = authorityService.getAll();
        for (Authority authority : authorities) {
            EasyUITreeModel treeModel = new EasyUITreeModel();
            treeModel.setId(authority.getId().toString());
            treeModel.setText(authority.getName());

            rootTreeModel.getChildren().add(treeModel);
        }

        return treeModelList;
    }
}
