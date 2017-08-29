package org.awesky.webapp.interceptors;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by vme on 2017/8/26.
 */
public class XSSInterceptor implements HandlerInterceptor {

    public XSSInterceptor() {


    }

    public void afterCompletion(HttpServletRequest req, HttpServletResponse resp, Object obj, Exception exp)
            throws Exception {
    }

    public void postHandle(HttpServletRequest req, HttpServletResponse resp, Object obj, ModelAndView view)
            throws Exception {
    }

    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object obj) throws Exception {

        return true;
    }

}
