package com.etiansoft.tools.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class ServletContextProvider {

	public static HttpServletRequest getRequest() {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		return attributes.getRequest();
	}

	public static HttpSession getSession() {
		return getRequest().getSession();
	}

	public static String getRealPath(String path) {
		return getSession().getServletContext().getRealPath(path);
	}
}
