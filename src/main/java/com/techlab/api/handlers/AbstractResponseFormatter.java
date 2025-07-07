package com.techlab.api.handlers;

import com.techlab.api.annotations.CustomResponseFormatter;
import com.techlab.api.formatters.ResponseFormatterInterface;
import org.springframework.context.ApplicationContext;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractResponseFormatter {
    protected final ApplicationContext applicationContext;

    static final Map<Class<? extends ResponseFormatterInterface>, ResponseFormatterInterface> formatterCache =
            new ConcurrentHashMap<>();

    public AbstractResponseFormatter(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    protected CustomResponseFormatter getCustomFormatter(MethodParameter returnType) {
        CustomResponseFormatter methodAnnotation = AnnotationUtils.findAnnotation(Objects.requireNonNull(returnType.getMethod(), "getCustomFormatter() received empty returnType."), CustomResponseFormatter.class);

        if (methodAnnotation != null) {
            return methodAnnotation;
        }

        return AnnotationUtils.findAnnotation(returnType.getDeclaringClass(), CustomResponseFormatter.class);
    }

    protected ResponseFormatterInterface getFormatter(Class<? extends ResponseFormatterInterface> formatterClass) {
        return formatterCache.computeIfAbsent(formatterClass, clazz -> {
            try {
                return applicationContext.getBean(clazz);
            } catch (Exception e) {
                try {
                    return clazz.getDeclaredConstructor().newInstance();
                } catch (Exception ex) {
                    throw new RuntimeException("Unable to instantiate custom ResponseFormatterInterface: " + clazz, e);
                }
            }
        });
    }
}
