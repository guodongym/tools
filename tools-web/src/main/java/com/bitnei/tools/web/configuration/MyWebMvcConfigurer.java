package com.bitnei.tools.web.configuration;

import com.bitnei.tools.web.interceptor.ResponseResultInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器配置
 *
 * @author zhaogd
 * @date 2020/2/4
 */
@Configuration
public class MyWebMvcConfigurer implements WebMvcConfigurer {

    @Bean
    ResponseResultInterceptor responseResultInterceptor() {
        return new ResponseResultInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
        registry.addInterceptor(responseResultInterceptor()).addPathPatterns("/**");
    }
}
