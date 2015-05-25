package com.neusoft.easyframework.web.controller.security;

import com.neusoft.easyframework.business.security.entity.Resource;
import com.neusoft.easyframework.business.security.service.ResourceService;
import com.neusoft.easyframework.business.security.shiro.ShiroDefinitionSectionMetaSourceService;
import com.neusoft.easyframework.core.orm.Page;
import com.neusoft.easyframework.core.orm.PropertyFilter;
import com.neusoft.easyframework.web.entity.EasyUITreeModel;
import com.neusoft.easyframework.web.entity.GridModel;
import com.neusoft.easyframework.web.entity.JsonModel;
import org.apache.commons.lang.StringUtils;
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
@RequestMapping(value = "/security/resource")
public class ResourceController {
    @Autowired
    private ResourceService resourceService;

    @Autowired
    private ShiroDefinitionSectionMetaSourceService definitionSectionMetaSourceService;

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
    public JsonModel doModify(Resource resource) {
        JsonModel jsonModel = new JsonModel();

        try {
            boolean needRefreshPermissionMetaSource = false;
            Resource editResource = null;
            if (resource.getId() != null) {
                editResource = resourceService.get(resource.getId()); // 防止编辑丢失其他关联信息
                needRefreshPermissionMetaSource = (editResource.getSource() != null && !editResource.getSource().equals(resource.getSource())) ||
                        (editResource.getSource() == null && resource.getSource() != null);

                editResource.setName(resource.getName());
                editResource.setSource(resource.getSource());
            } else {
                editResource = resource;
                needRefreshPermissionMetaSource = StringUtils.isNotBlank(editResource.getSource());
            }

            resourceService.save(editResource);
            if (needRefreshPermissionMetaSource) {
                definitionSectionMetaSourceService.updatePermission();
            }
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
            Resource resource = resourceService.get(id);
            boolean needRefreshPermissionMetaSource = StringUtils.isNotBlank(resource.getSource());

            resourceService.delete(id);

            // 删除资源后，需要动态更新授权元数据信息
            if (needRefreshPermissionMetaSource) {
                definitionSectionMetaSourceService.updatePermission();
            }

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

        List<Resource> resources = resourceService.getAll();
        for (Resource resource : resources) {
            EasyUITreeModel treeModel = new EasyUITreeModel();
            treeModel.setId(resource.getId().toString());
            treeModel.setText(resource.getName());

            rootTreeModel.getChildren().add(treeModel);
        }

        return treeModelList;
    }
}
