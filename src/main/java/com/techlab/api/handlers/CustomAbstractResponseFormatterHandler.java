package com.techlab.api.handlers;

import com.techlab.api.annotations.CustomResponseFormatter;
import com.techlab.api.formatters.ResponseFormatterInterface;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.logging.Level;
import java.util.logging.Logger;

@Order(2)
@ControllerAdvice
public class CustomAbstractResponseFormatterHandler extends AbstractResponseFormatter implements ResponseBodyAdvice<Object> {
    Logger logger = Logger.getLogger(String.valueOf(CustomAbstractResponseFormatterHandler.class));

    @Autowired
    public CustomAbstractResponseFormatterHandler(ApplicationContext applicationContext) {
        super(applicationContext);
    }

    @Override
    public boolean supports(MethodParameter returnType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        CustomResponseFormatter annotation = this.getCustomFormatter(returnType);

        return annotation != null;
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response) {
        CustomResponseFormatter annotation = this.getCustomFormatter(returnType);

        if (annotation == null) {
            return body;
        }

        try {
            ResponseFormatterInterface responseFormatter = this.getFormatter(annotation.value());

            if (!responseFormatter.shouldFormat(body, returnType)) {
                return body;
            }

            return responseFormatter.formatResponse(body, request, response, returnType);
        } catch (Error e) {
            this.logger.logp(Level.SEVERE, this.getClass().getName(), "CustomAbstractResponseFormatterHandler::beforeBodyWrite", e.getMessage(), e);

            return body;
        }
    }
}