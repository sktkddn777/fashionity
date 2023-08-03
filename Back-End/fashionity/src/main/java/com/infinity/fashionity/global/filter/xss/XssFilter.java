package com.infinity.fashionity.global.filter.xss;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
@Slf4j
public class XssFilter implements Filter {

    /**
     * Filter가 연결된 다음번 Filter로 전달하기 전 처리할 일을 작성
     * */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest xssFilteredRequest = new XssRequestWrapper((HttpServletRequest) request);//request객체를 xssRequestWrapper로 감싼 뒤 다음 필터 호출
        chain.doFilter(xssFilteredRequest,response);
    }

    /**
     * Filter가 생성될 때 해야할 작업이 있다면 init에서 작성
     * */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    /**
     * Filter가 해제될 때 해야할 작업이 있다면 destroy에서 작성
     * */
    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
