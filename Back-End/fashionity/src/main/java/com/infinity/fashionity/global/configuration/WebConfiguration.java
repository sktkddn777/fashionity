package com.infinity.fashionity.global.configuration;

import com.infinity.fashionity.global.filter.xss.XssFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.Filter;

/**
 * 공통적인 웹 설정 파일. ex) Filter, Interceptor 등등
 * */
@Configuration//환경설정파일 컴포넌트로 등록한다는 어노테이션
@RequiredArgsConstructor//@Not Null 혹은 final 필드만을 가진 생성자를 만들어줌
@EnableWebMvc
public class WebConfiguration implements WebMvcConfigurer {
    private final XssFilter xssFilter;

    /*
    * xss공격을 막아주는 Filter를 등록
    * */
    @Bean
    public FilterRegistrationBean<Filter> xssFilterRegistrationBean(){
        FilterRegistrationBean<Filter> xssFilterRegistrationBean = new FilterRegistrationBean<>();
        xssFilterRegistrationBean.setFilter(this.xssFilter);
        xssFilterRegistrationBean.setOrder(1);
        xssFilterRegistrationBean.setName("xss-filter");
        xssFilterRegistrationBean.addUrlPatterns("/*");

        return xssFilterRegistrationBean;
    }
}
