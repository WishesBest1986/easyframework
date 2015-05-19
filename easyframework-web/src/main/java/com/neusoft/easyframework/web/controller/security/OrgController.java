package com.neusoft.easyframework.web.controller.security;

import com.neusoft.easyframework.business.security.service.OrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
