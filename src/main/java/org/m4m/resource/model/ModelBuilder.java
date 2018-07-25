package org.m4m.resource.model;

import java.lang.reflect.Method;

public class ModelBuilder {
    public static DomainModel map(org.m4m.resource.annotation.DomainModel domainModel) {
        assert domainModel != null : "domainModel cannot be null";
        return new DomainModel(domainModel.name(), domainModel.action());
    }

    public static ResourceModel map(org.m4m.resource.annotation.ResourceModel resourceModel, Class<?> actualClass, Method actualMethod) {
        assert resourceModel != null : "resourceModel cannot be null";
        assert actualClass != null : "actualClass cannot be null";
        assert actualMethod != null : "actualMethod cannot be null";
        return new ResourceModel(
                resourceModel.publishes().name(),
                resourceModel.publishes().action(),
                resourceModel.rel(),
                actualClass,
                actualMethod
        );
    }

    public static ServiceModel map(org.m4m.resource.annotation.ServiceModel serviceModel, Method actualMethod) {
        assert serviceModel != null : "serviceModel cannot be null";
        assert actualMethod != null : "actualMethod cannot be null";
        return new ServiceModel(
                serviceModel.serves().name(),
                serviceModel.serves().action(),
                actualMethod
        );
    }
}
