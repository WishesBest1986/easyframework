package com.neusoft.easyframework.web.controller.security;

import com.neusoft.easyframework.business.security.entity.Menu;
import com.neusoft.easyframework.business.security.service.MenuService;
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

    @ResponseBody
    @RequestMapping(value = "doModify")
    public JsonModel doModify(Menu menu, Long parentMenuId) {
        JsonModel jsonModel = new JsonModel();

        if (parentMenuId != null && parentMenuId.longValue() > 0) {
            Menu parent = new Menu(parentMenuId);
            menu.setParentMenu(parent);
        }

        try {
            Menu editMenu = null;
            if (menu.getId() != null) {
                editMenu = menuService.get(menu.getId()); // 防止编辑丢失子节点

                editMenu.setName(menu.getName());
                editMenu.setDescription(menu.getDescription());
                editMenu.setOrderNum(menu.getOrderNum());
                editMenu.setParentMenu(menu.getParentMenu());
            } else {
                editMenu = menu;
            }

            menuService.save(editMenu);
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
            menuService.delete(id);
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

        List<Menu> menus = menuService.getAll();
        for (Menu menu : menus) {
            EasyUITreeModel model = new EasyUITreeModel();
            model.setId(menu.getId().toString());
            model.setText(menu.getName());
            if (menu.getParentMenu() == null) {
                recursionCreateTreeModel(model, menu.getSubMenus());
                treeModelList.add(model);
            }
        }

        return treeModelList;
    }

    private void recursionCreateTreeModel(EasyUITreeModel treeModel, List<Menu> subMenus) {
        for (Menu subMenu : subMenus) {
            EasyUITreeModel subModel = new EasyUITreeModel();
            subModel.setId(subMenu.getId().toString());
            subModel.setText(subMenu.getName());
            if (subMenu.getSubMenus() != null && subMenu.getSubMenus().size() > 0) {
                recursionCreateTreeModel(subModel, subMenu.getSubMenus());
            }
            treeModel.getChildren().add(subModel);
        }
    }
}
