package com.etiansoft.tools.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.etiansoft.tools.common.DateTool;

public class PathFilter implements Filter {

	private String postfix;
	private String homePage;

	@Override
	public void init(FilterConfig config) throws ServletException {
		postfix = config.getInitParameter("postfix");
		homePage = config.getInitParameter("homePage");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		response.setCharacterEncoding("UTF-8");
		String path = req.getContextPath();
		String ctx = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + path;
		if (req.getServerPort() == 80) {
			ctx = req.getScheme() + "://" + req.getServerName() + path;
		}
		req.setAttribute("ctx", ctx);
		req.setAttribute("postfix", postfix);
		req.setAttribute("date", DateTool.currentTime());
		String pathInfo = req.getRequestURI();
		if (pathInfo == null || pathInfo.equals(path + "/") || "".equals(pathInfo)) {
			resp.sendRedirect(ctx + homePage);
			return;
		}
		if (path != null && !path.isEmpty() && !path.equals("/")) {
			pathInfo = ctx + pathInfo.substring(path.length());
			req.setAttribute("path", pathInfo);
		}

		String requestURI = req.getRequestURI();
		if (requestURI.endsWith(".js")) {
			response.setContentType("text/javascript;charset=UTF-8");
		}

		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
	}
}
