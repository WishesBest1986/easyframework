package com.neusoft.easyframework.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by neusoft on 15-5-6.
 */
@Controller
public class PlatformController {

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return "system/index";
    }
}
