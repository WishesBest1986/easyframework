package com.neusoft.easyframework.web.controller.security;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by neusoft on 15-5-12.
 */
@Controller
public class PassportController {

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginForm() {
        return "security/login";
    }
}
