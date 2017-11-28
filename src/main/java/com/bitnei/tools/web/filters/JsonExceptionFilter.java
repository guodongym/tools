/**
 * @项目名称: tools
 * @文件名称: JsonExceptionFilter.java
 * @author tianlihu
 * @Date: 2015-7-7
 * @Copyright: 2015 www.etiansoft.com Inc. All rights reserved.
 * 注意：本内容仅限于北京逸天科技有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
package com.bitnei.tools.web.filters;

import com.alibaba.fastjson.JSONObject;
import com.bitnei.tools.entity.ArcExceptionResponse;
import com.bitnei.tools.exception.JsonException;
import org.springframework.http.HttpStatus;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class JsonExceptionFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } catch (ServletException e) {
            if (e.getCause() instanceof JsonException) {
                printJsonException(response, e.getCause());
            } else if (e.getCause().getCause() instanceof JsonException) {
                printJsonException(response, e.getCause().getCause());
            } else {
                throw e;
            }
        }
    }

    @Override
    public void destroy() {
    }

    private void printJsonException(ServletResponse response, Throwable cause) throws IOException {
        String json = JSONObject.toJSONString(ArcExceptionResponse.create(HttpStatus.INTERNAL_SERVER_ERROR.value(), cause.getMessage()));
        HttpServletResponse resp = (HttpServletResponse) response;
        PrintWriter writer = resp.getWriter();
        writer.write(json);
        writer.flush();
        writer.close();
    }
}
