package org.awesky.webapp.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class ArchonAccessInterceptor implements HandlerInterceptor {

	public void afterCompletion(HttpServletRequest req, HttpServletResponse resp, Object obj, Exception exp)
			throws Exception {
	}

	public void postHandle(HttpServletRequest req, HttpServletResponse resp, Object obj, ModelAndView view)
			throws Exception {
	}

	public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object obj) throws Exception {
		String url = req.getServletPath();
		int port = req.getRemotePort();
		if("/login".equals(url) && port > 10000) {

		}
		return true;
	}

}
