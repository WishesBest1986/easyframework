package com.neusoft.easyframework.web.controller.security;

import com.neusoft.easyframework.business.security.entity.Org;
import com.neusoft.easyframework.business.security.service.OrgService;
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
 * Created by neusoft on 15-5-19.
 */
@Controller
@RequestMapping(value = "/security/org")
public class OrgController {
    @Autowired
    private OrgService orgService;

    @RequestMapping
    public String manager() {
        return "security/orgManager";
    }

    @ResponseBody
    @RequestMapping(value = "list")
    public GridModel<Org> list(Page<Org> page, HttpServletRequest request) {
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);

        Page<Org> orgPage = orgService.findPage(page, filters);

        GridModel<Org> gridModel = new GridModel<Org>();
        gridModel.setTotal(orgPage.getTotalCount());
        gridModel.setRows(orgPage.getResult());

        return gridModel;
    }

    @ResponseBody
    @RequestMapping(value = "doModify")
    public JsonModel doModify(Org org, Long parentOrgId) {
        JsonModel jsonModel = new JsonModel();

        if (parentOrgId != null && parentOrgId.longValue() > 0) {
            Org parent = new Org(parentOrgId);
            org.setParentOrg(parent);
        }

        try {
            Org editOrg = null;
            if (org.getId() != null) {
                editOrg = orgService.get(org.getId()); // 防止编辑丢失子节点

                editOrg.setName(org.getName());
                editOrg.setDescription(org.getDescription());
                editOrg.setParentOrg(org.getParentOrg());
            } else {
                editOrg = org;
            }

            orgService.save(editOrg);
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
            orgService.delete(id);
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

        List<Org> orgs = orgService.getAll();
        for (Org org : orgs) {
            EasyUITreeModel model = new EasyUITreeModel();
            model.setId(org.getId().toString());
            model.setText(org.getName());
            if (org.getParentOrg() == null) {
                recursionCreateTreeModel(model, org.getOrgs());
                treeModelList.add(model);
            }
        }

        return treeModelList;
    }

    private void recursionCreateTreeModel(EasyUITreeModel treeModel, List<Org> subOrgs) {
        for (Org subOrg : subOrgs) {
            EasyUITreeModel subModel = new EasyUITreeModel();
            subModel.setId(subOrg.getId().toString());
            subModel.setPid(treeModel.getId().toString());
            subModel.setText(subOrg.getName());
            if (subOrg.getOrgs() != null && subOrg.getOrgs().size() > 0) {
                recursionCreateTreeModel(subModel, subOrg.getOrgs());
            }
            treeModel.getChildren().add(subModel);
        }
    }
}
