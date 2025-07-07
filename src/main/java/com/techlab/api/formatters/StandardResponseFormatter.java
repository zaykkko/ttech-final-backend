package com.techlab.api.formatters;

import com.techlab.api.responses.StandardResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;

public class StandardResponseFormatter implements ResponseFormatterInterface {
    @Override
    public Object formatResponse(Object body, ServerHttpRequest request, ServerHttpResponse response, MethodParameter methodParameter) {
        HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();

        return new StandardResponse(HttpStatus.valueOf(servletResponse.getStatus()), body);
    }
}
