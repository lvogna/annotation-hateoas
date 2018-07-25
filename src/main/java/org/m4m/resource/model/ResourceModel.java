package org.m4m.resource.model;

import java.lang.reflect.Method;
import java.util.Objects;

public class ResourceModel extends DomainModel {
    private final String rel;
    private final Class<?> actualClass;
    private final Method actualMethod;

    public ResourceModel(String name, String action, String rel, Class<?> actualClass, Method actualMethod) {
        super(name, action);
        assert rel != null : "rel cannot be null";
        assert !rel.isEmpty() : "rel cannot be empty";
        assert actualClass != null : "actualClass cannot be null";
        assert actualMethod != null : "actualMethod cannot be null";
        this.rel = rel;
        this.actualClass = actualClass;
        this.actualMethod = actualMethod;
    }

    public String getRel() {
        return rel;
    }

    public Class<?> getActualClass() {
        return actualClass;
    }

    public Method getActualMethod() {
        return actualMethod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResourceModel)) return false;
        if (!super.equals(o)) return false;
        ResourceModel that = (ResourceModel) o;
        return Objects.equals(getRel(), that.getRel());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getRel());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ResourceModel{");
        sb.append("super=").append(super.toString());
        sb.append(", rel='").append(rel).append('\'');
        sb.append('}');
        return sb.toString();
    }


}
