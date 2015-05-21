package com.neusoft.easyframework.web.controller.security;

import com.neusoft.easyframework.business.security.entity.Authority;
import com.neusoft.easyframework.business.security.entity.Role;
import com.neusoft.easyframework.business.security.service.RoleService;
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
@RequestMapping(value = "/security/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @RequestMapping
    public String manager() {
        return "security/roleManager";
    }

    @ResponseBody
    @RequestMapping(value = "list")
    public GridModel<Role> list(Page<Role> page, HttpServletRequest request) {
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);

        Page<Role> rolePage = roleService.findPage(page, filters);

        GridModel<Role> gridModel = new GridModel<Role>();
        gridModel.setTotal(rolePage.getTotalCount());
        gridModel.setRows(rolePage.getResult());

        return gridModel;
    }

    @ResponseBody
    @RequestMapping(value = "doModify")
    public JsonModel doModify(Role role, Long[] authorityIds) {
        JsonModel jsonModel = new JsonModel();

        if (authorityIds != null) {
            for (Long authorityId : authorityIds) {
                if (authorityId != null && authorityId.longValue() > 0) {
                    Authority authority = new Authority(authorityId);
                    role.getAuthorities().add(authority);
                }
            }
        }

        try {
            Role editRole = null;
            if (role.getId() != null) {
                editRole = roleService.get(role.getId()); // 防止编辑丢失其他关联信息

                editRole.setName(role.getName());
                editRole.setDescription(role.getDescription());
                editRole.setAuthorities(role.getAuthorities());
            } else {
                editRole = role;
            }

            roleService.save(editRole);
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
            roleService.delete(id);
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

        List<Role> roles = roleService.getAll();
        for (Role role : roles) {
            EasyUITreeModel treeModel = new EasyUITreeModel();
            treeModel.setId(role.getId().toString());
            treeModel.setText(role.getName());

            rootTreeModel.getChildren().add(treeModel);
        }

        return treeModelList;
    }
}
