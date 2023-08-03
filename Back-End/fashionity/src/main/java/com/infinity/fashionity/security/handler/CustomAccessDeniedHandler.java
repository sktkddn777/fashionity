package com.infinity.fashionity.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infinity.fashionity.global.dto.ErrorResponse;
import com.infinity.fashionity.global.exception.ErrorCode;
import com.nimbusds.jose.util.StandardCharset;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper mapper;
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(ErrorCode.HANDLE_ACCESS_DENIED.getStatus().value());
        response.getWriter().write(mapper.writeValueAsString(ErrorResponse.of(ErrorCode.HANDLE_ACCESS_DENIED)));
    }
}

