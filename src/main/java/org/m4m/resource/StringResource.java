package org.m4m.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;

public class StringResource extends ResourceSupport {

    private final String string;

    public StringResource(@JsonProperty("string")String string) {
        assert string!=null :"string cannot be null";
        this.string = string;
    }

    public String getString() {
        return string;
    }
}
