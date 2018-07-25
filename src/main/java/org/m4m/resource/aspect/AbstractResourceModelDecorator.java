package org.m4m.resource.aspect;

import org.m4m.resource.annotation.ResourceModel;
import org.m4m.resource.service.ModelService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;

public class AbstractResourceModelDecorator {
    protected final ModelService modelService;

    @Autowired
    public AbstractResourceModelDecorator(ModelService modelService) {
        this.modelService = modelService;
    }

    protected ResourceModel getResourceModel(JoinPoint joinPoint) throws NoSuchMethodException {
        final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        final String methodName = signature.getMethod().getName();
        final Class<?>[] parameterTypes = signature.getMethod().getParameterTypes();
        final Class<?> actualClass = joinPoint.getTarget().getClass();
        final Method actualMethod = actualClass.getMethod(methodName, parameterTypes);
        return actualMethod.getAnnotation(ResourceModel.class);
    }
}
