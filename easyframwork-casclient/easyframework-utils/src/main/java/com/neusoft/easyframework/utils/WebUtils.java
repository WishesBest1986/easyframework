package com.neusoft.easyframework.utils;


import javax.servlet.http.HttpServletRequest;

/**
 * Created by neusoft on 15-5-21.
 */
public class WebUtils {
    /**
     * 判断请求是否为AJAX请求
     * @param request
     * @return
     */
    public static boolean isAjax(HttpServletRequest request) {
        boolean ajax = "XMLHttpRequest".equalsIgnoreCase(request.getHeader("x-requested-with"));
        String ajaxFlag = null == request.getParameter("ajax") ? "false" : request.getParameter("ajax");

        return ajax || ajaxFlag.equalsIgnoreCase("true");
    }
}
