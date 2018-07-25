package org.m4m.resource.model;

import java.util.Objects;

public class ServiceProvider<T extends DomainModel>  {

    private T model;

    public ServiceProvider(T model) {
        this.model = model;
    }

    public T getModel() {
        return model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceProvider<?> that = (ServiceProvider<?>) o;
        return Objects.equals(getModel(), that.getModel());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getModel());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ServiceProvider{");
        sb.append("model=").append(model);
        sb.append('}');
        return sb.toString();
    }
}
