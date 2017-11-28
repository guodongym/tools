package com.bitnei.tools.web.interceptors;

import com.bitnei.tools.constant.Constants;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Locale;

public class LocaleChangeInterceptor extends org.springframework.web.servlet.i18n.LocaleChangeInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException {
		HttpSession session = request.getSession();

		boolean result = super.preHandle(request, response, handler);
		String newLocale = request.getParameter(getParamName());
		if (newLocale != null) {
			Locale locale = StringUtils.parseLocaleString(newLocale);
			session.setAttribute(Constants.LOCALE, locale.toString());
		}

		if (session.getAttribute(Constants.LOCALE) == null) {
			LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
			Locale locale = localeResolver.resolveLocale(request);
			session.setAttribute(Constants.LOCALE, locale.toString());
		}
		return result;
	}
}