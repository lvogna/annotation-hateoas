package org.m4m.resource.model;

import java.util.Objects;

public class DomainModel {

    private final String name;
    private final String action;

    public DomainModel(String name, String action) {
        assert name != null : "name cannot be null";
        assert !name.isEmpty() : "name cannot be empty";
        assert action != null : "action cannot be null";
        assert !action.isEmpty() : "action cannot be empty";
        this.name = name;
        this.action = action;
    }

    public String getName() {
        return name;
    }

    public String getAction() {
        return action;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DomainModel)) return false;
        DomainModel that = (DomainModel) o;
        return Objects.equals(getName(), that.getName()) &&
                Objects.equals(getAction(), that.getAction());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getAction());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DomainModel{");
        sb.append("name='").append(name).append('\'');
        sb.append(", action='").append(action).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
