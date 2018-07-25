package org.m4m.resource.collector;


import org.m4m.resource.annotation.ServiceModel;
import org.m4m.resource.model.ModelBuilder;
import org.m4m.resource.model.ServiceProvider;
import org.m4m.resource.service.ModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;


@Component
public class ModelCollector implements ApplicationContextAware {

    private final static Logger logger = LoggerFactory.getLogger(ModelCollector.class);

    private ApplicationContext applicationContext;

    private final ModelService modelService;

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        assert applicationContext != null : "applicationContext cannot be null";
        this.applicationContext = applicationContext;
    }

    @Autowired
    public ModelCollector(ModelService modelService) {
        this.modelService = modelService;
    }

    @PostConstruct
    public void collect() {
        //TODO: clean up with behaviour parameterization
        Arrays.asList(applicationContext.getBeanDefinitionNames()).forEach(beanName -> {
            final Object bean = applicationContext.getBean(beanName);
            final Class<?> targetClass = org.springframework.aop.support.AopUtils.getTargetClass(bean);
            Arrays.asList(targetClass.getDeclaredMethods()).forEach(method -> {
                final ServiceModel serviceModelAnnotation = AnnotationUtils.getAnnotation(method, ServiceModel.class);
                if (serviceModelAnnotation != null) {
                    logger.debug("serviceModelAnnotation [{}] found on method [{}]", serviceModelAnnotation, method);
                    final org.m4m.resource.model.DomainModel domainModelAnnotation = ModelBuilder.map(serviceModelAnnotation.serves());
                    final org.m4m.resource.model.ServiceModel serviceModel = ModelBuilder.map(serviceModelAnnotation, method);
                    logger.info("collecting [resource={}] from [{}]", serviceModel, getSourceName(targetClass, method));
                    modelService.registerService(domainModelAnnotation, new ServiceProvider<>(serviceModel));
                }
                org.m4m.resource.annotation.ResourceModel resourceModelAnnotation = AnnotationUtils.getAnnotation(method, org.m4m.resource.annotation.ResourceModel.class);
                if (resourceModelAnnotation != null) {
                    logger.debug("resourceModelAnnotation [{}] found on method [{}]", resourceModelAnnotation, method);
                    final org.m4m.resource.model.DomainModel domainModelAnnotation = ModelBuilder.map(resourceModelAnnotation.publishes());
                    final org.m4m.resource.model.ResourceModel resourceModel = ModelBuilder.map(resourceModelAnnotation, targetClass, method);
                    logger.info("collecting [resource={}] from [{}]", resourceModel, getSourceName(targetClass, method));
                    modelService.registerRestService(domainModelAnnotation, new ServiceProvider<>(resourceModel));
                }
            });
        });

        modelService.listOrphanSupportedServices().forEach(resourceModel ->
                {
                    final ServiceProvider<org.m4m.resource.model.ServiceModel> serviceProvider = modelService.readServiceProvider(resourceModel);
                    logger.warn("orphan service [{}]@[{}] will not be consumed by any rest service", resourceModel, serviceProvider);
                }
        );

        modelService.listSupportedServices().forEach(resourceModel ->
                {
                    final ServiceProvider<org.m4m.resource.model.ServiceModel> serviceProvider = modelService.readServiceProvider(resourceModel);
                    logger.info("service [{}]@[{}] ready to serve business needs", resourceModel, serviceProvider);
                }
        );
        modelService.listSupportedRestServices().forEach(resourceModel ->
                {
                    final Set<ServiceProvider<org.m4m.resource.model.ResourceModel>> restServiceProviders = modelService.listRestServiceProviders(resourceModel);
                    logger.info("rest service [{}]@[{}] ready to serve http needs", resourceModel, restServiceProviders);
                }
        );
    }


    private String getSourceName(Class<?> beanClass, Method method) {
        assert beanClass != null : "beanClass cannot be null";
        assert method != null : "method cannot be null";
        return String.format("%s::%s", beanClass.getSimpleName(), method.getName());
    }

}
