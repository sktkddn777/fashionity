package com.infinity.fashionity.global.config;

import com.infinity.fashionity.security.filter.ExceptionHandlerFilter;
import com.infinity.fashionity.security.filter.JwtAuthenticationEntryPoint;
import com.infinity.fashionity.security.filter.JwtAuthenticationFilter;
import com.infinity.fashionity.security.handler.CustomAccessDeniedHandler;
import com.infinity.fashionity.security.handler.CustomAuthenticationEntryPoint;
import com.infinity.fashionity.security.oauth.handler.OAuth2AuthenticationFailureHandler;
import com.infinity.fashionity.security.oauth.handler.OAuth2AuthenticationSuccessHandler;
import com.infinity.fashionity.security.oauth.repository.HttpCookieOAuth2AuthorizationRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final ExceptionHandlerFilter exceptionHandlerFilter;
    private final String[] allowedUrls = {"/api/v1/auth/**", "/api/v1/posts"};

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .cors()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .antMatchers(allowedUrls).permitAll()
                .antMatchers(HttpMethod.DELETE,"/api/v1/posts/**")
                .authenticated()
                .antMatchers(HttpMethod.POST,"/api/v1/posts/**")
                .authenticated()
                .antMatchers(HttpMethod.PUT,"/api/v1/posts/**")
                .authenticated()
                .anyRequest()
                .authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler)
                .and()
                .oauth2Login()
                .authorizationEndpoint().baseUri("/oauth2/authorization") // 위 주소로 요청이 가면 oauth 로그인 진행한다!
                .authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository)
                .and()
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(exceptionHandlerFilter, JwtAuthenticationFilter.class)
                .build();

    }

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true); //  자격증명과 함께 요청 여부 (???)
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3333"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "PUT", "PATCH", "DELETE"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
