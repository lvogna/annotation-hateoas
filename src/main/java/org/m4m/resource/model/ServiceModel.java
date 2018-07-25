package org.m4m.resource.model;

import java.lang.reflect.Method;
import java.util.Objects;

public class ServiceModel extends DomainModel {

    private final Method actualMethod;

    public ServiceModel(String name, String action, Method actualMethod) {
        super(name, action);
        assert actualMethod != null : "actualMethod cannot be null";
        this.actualMethod = actualMethod;
    }

    public Method getActualMethod() {
        return actualMethod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServiceModel)) return false;
        if (!super.equals(o)) return false;
        ServiceModel that = (ServiceModel) o;
        return Objects.equals(getActualMethod(), that.getActualMethod());
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), getActualMethod());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ServiceModel{");
        sb.append("super=").append(super.toString());
        sb.append(", actualMethod=").append(actualMethod);
        sb.append('}');
        return sb.toString();
    }
}
