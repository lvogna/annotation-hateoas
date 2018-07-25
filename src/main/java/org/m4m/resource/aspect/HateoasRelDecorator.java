package org.m4m.resource.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.m4m.resource.annotation.ResourceModel;
import org.m4m.resource.model.DomainModel;
import org.m4m.resource.model.ModelBuilder;
import org.m4m.resource.model.ServiceProvider;
import org.m4m.resource.service.ModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Aspect
@Component
public class HateoasRelDecorator extends AbstractResourceModelDecorator {

    private final static Logger logger = LoggerFactory.getLogger(HateoasRelDecorator.class);

    public HateoasRelDecorator(ModelService modelService) {
        super(modelService);
    }

    @Before(value = "@annotation(org.m4m.resource.annotation.ResourceModel)")
    public void addRelations(JoinPoint joinPoint) throws Throwable {
        final Set<String> userRoles = getUserRoles();
        final ResourceModel currentResourceModelAnnotation = getResourceModel(joinPoint);
        final List<String> mandatoryRoles = modelService.listConfiguredRoles(ModelBuilder.map(currentResourceModelAnnotation.publishes()));
        if (!userRoles.containsAll(mandatoryRoles)) {
            throw new AccessDeniedException(String.format("[%s] needed, [%s] found", mandatoryRoles, userRoles));
        }
    }

    @AfterReturning(value = "@annotation(org.m4m.resource.annotation.ResourceModel)", returning = "retVal")
    public void addRelations(JoinPoint joinPoint, HttpEntity<?> retVal) throws Throwable {
        try {
            final Set<String> userRoles = getUserRoles();
            // input
            final Object[] inputParams = joinPoint.getArgs();
            final ResourceModel currentResourceModelAnnotation = getResourceModel(joinPoint);
            final DomainModel currentDomainModel = ModelBuilder.map(currentResourceModelAnnotation.publishes());
            final Set<ServiceProvider<org.m4m.resource.model.ResourceModel>> allResourceProviders = modelService.listRestServiceProviders(currentDomainModel);
            allResourceProviders.forEach(localResourceProvider -> {
                final org.m4m.resource.model.ResourceModel localResourceModel = localResourceProvider.getModel();
                final Class<?> actualClass = localResourceModel.getActualClass();
                final Method actualMethod = localResourceModel.getActualMethod();
                final ResourceModel localResourceModelAnnotation = AnnotationUtils.getAnnotation(actualMethod, ResourceModel.class);
                final String rel = localResourceModel.getRel();
                logger.debug("decorating [{}] with rel [{}]", currentDomainModel, rel);
                final List<String> mandatoryRoles = modelService.listConfiguredRoles(ModelBuilder.map(localResourceModelAnnotation.publishes()));
                if (userRoles.containsAll(mandatoryRoles)) {
                    try {
                        final Object dummyObject = methodOn(actualClass);
                        actualMethod.invoke(dummyObject, inputParams);
                        final ControllerLinkBuilder dummyClass = linkTo(dummyObject);
                        final Link link = currentResourceModelAnnotation.equals(localResourceModelAnnotation) ? dummyClass.withSelfRel() : dummyClass.withRel(rel);
                        final ResourceSupport body = (ResourceSupport) retVal.getBody();
                        body.add(link);
                    } catch (Exception e) {
                        logger.warn("failed to invoke on [{}::{}] having params [{}]", localResourceModel.getName(), localResourceModel.getAction(), inputParams);
                    }
                } else {
                    logger.debug("rel [{}] not included as userRoles [{}] do not match required roles [{}]", rel, userRoles, mandatoryRoles);
                }
            });
        } catch (Exception e) {
            logger.error("error occurred decorating http response", e);
            throw e;
        }
    }

    private Set<String> getUserRoles() {
        final Set<String> userRoles;
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            userRoles = authentication.
                    getAuthorities().
                    stream().
                    map(authority -> authority.getAuthority()).
                    collect(Collectors.toSet());
        } else {
            userRoles = Collections.EMPTY_SET;
        }
        return userRoles;
    }
}
