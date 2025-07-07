package com.techlab.api.handlers;

import com.techlab.api.annotations.CustomResponseFormatter;
import com.techlab.api.formatters.ResponseFormatterInterface;
import com.techlab.api.formatters.StandardResponseFormatter;
import com.techlab.api.responses.ResponseInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.logging.Level;
import java.util.logging.Logger;

@Order
@ControllerAdvice
public class StandardAbstractResponseFormatterHandler extends AbstractResponseFormatter implements ResponseBodyAdvice<Object> {
    Logger logger = Logger.getLogger(String.valueOf(StandardAbstractResponseFormatterHandler.class));

    @Autowired
    public StandardAbstractResponseFormatterHandler(ApplicationContext applicationContext) {
        super(applicationContext);
    }

    @Override
    public boolean supports(MethodParameter returnType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        CustomResponseFormatter annotation = this.getCustomFormatter(returnType);

        return annotation == null || annotation.override();
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response) {
        if (body instanceof ResponseInterface) {
            return body;
        }

        try {
            ResponseFormatterInterface standardFormatter = this.getFormatter(StandardResponseFormatter.class);

            if (!standardFormatter.shouldFormat(body, returnType)) {
                return body;
            }

            return standardFormatter.formatResponse(body, request, response, returnType);
        } catch (Error e) {
            this.logger.logp(Level.SEVERE, this.getClass().getName(), "StandardAbstractResponseFormatterHandler::beforeBodyWrite", e.getMessage(), e);

            return body;
        }
    }
}
