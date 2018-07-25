package org.m4m.resource.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ResourceModel {
    String rel();
    DomainModel publishes();
}
