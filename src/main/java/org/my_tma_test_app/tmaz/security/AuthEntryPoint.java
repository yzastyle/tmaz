package org.my_tma_test_app.tmaz.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.my_tma_test_app.tmaz.security.exception.RestApiErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;

@RequiredArgsConstructor
public class AuthEntryPoint implements org.springframework.security.web.AuthenticationEntryPoint {

        private final ObjectMapper objectMapper;
        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(objectMapper.writeValueAsString(RestApiErrorDto.toResponse(HttpStatus.UNAUTHORIZED,
                    authException.getMessage(), null)));
        }
}
