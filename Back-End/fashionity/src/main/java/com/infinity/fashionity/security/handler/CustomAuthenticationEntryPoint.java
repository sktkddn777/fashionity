package com.infinity.fashionity.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infinity.fashionity.global.dto.ErrorResponse;
import com.infinity.fashionity.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper mapper;
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(ErrorCode.UNAUTHENTICATED_MEMBER.getStatus().value());
        response.getWriter().write(mapper.writeValueAsString(ErrorResponse.of(ErrorCode.UNAUTHENTICATED_MEMBER)));
    }
}
