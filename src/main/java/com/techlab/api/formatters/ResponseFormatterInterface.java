package com.techlab.api.formatters;

import org.springframework.core.MethodParameter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;

public interface ResponseFormatterInterface {
    /**
     * Format response
     * @param body Controller original response
     * @param request HTTP request
     * @param response HTTP response
     * @param methodParameter Info about the method that returned the response.
     * @return Formatted response
     */
    Object formatResponse(Object body,
                          ServerHttpRequest request,
                          ServerHttpResponse response,
                          MethodParameter methodParameter);

    /**
     * Determines whenever the response should be formatted
     * @param originalResponse Controller original response
     * @param methodParameter Info about the method that returned the response.
     * @return true to execute the format, false to do not
     */
    default boolean shouldFormat(Object originalResponse, MethodParameter methodParameter) {
        return true;
    }
}
