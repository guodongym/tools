package com.bitnei.tools.web.interceptors;

import com.bitnei.tools.constant.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;

public class LoginInterceptor extends HandlerInterceptorAdapter {

    private List<String> excludeUrls;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UrlPathHelper helper = new UrlPathHelper();
        String relativePath = helper.getOriginatingRequestUri(request);
        String contextPath = helper.getOriginatingContextPath(request);

        HttpSession session = request.getSession();
        Object user = session.getAttribute(Constants.LOGIN_USER);
        if (user != null) {
            return true;
        }

        if (!StringUtils.isEmpty(contextPath) && !"/".equals(contextPath)) {
            relativePath = relativePath.substring(contextPath.length());
        }

        if (relativePath.startsWith("/static/")) {
            return true;
        }

        List<String> excludeUrls = getExcludeUrls();
        if (excludeUrls.contains(relativePath)) {
            return true;
        }

        if (!Constants.LOGIN_URL.equals(relativePath)) {
            response.sendRedirect(contextPath + Constants.LOGIN_URL);
            return false;
        }

        return false;
    }

    public List<String> getExcludeUrls() {
        if (excludeUrls == null) {
            excludeUrls = Collections.emptyList();
        }
        return excludeUrls;
    }

    public void setExcludeUrls(List<String> excludeUrls) {
        this.excludeUrls = excludeUrls;
    }
}
