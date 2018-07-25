package org.m4m.service;

import org.m4m.resource.annotation.DomainModel;
import org.m4m.resource.annotation.ServiceModel;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

@Service
public class DomainStringService implements StringService {

    @Override
    @ServiceModel(serves = @DomainModel(name = "String", action = "toUpperCase"))
    @Secured("CAN_STRING_TO_UPPER")
    public String toUpperCase(final String input) {
        assert input != null : "input cannot be null";
        return input.toUpperCase();
    }

    @Override
    @ServiceModel(serves = @DomainModel(name = "String", action = "toLowerCase"))
    @Secured("CAN_STRING_TO_LOWER")
    public String toLowerCase(String input) {
        assert input != null : "input cannot be null";
        return input.toLowerCase();
    }

    @Override
    @ServiceModel(serves = @DomainModel(name = "String", action = "toCamelCase"))
    public String toCamelCase(String input) {
        assert input != null : "input cannot be null";
        StringBuilder sb = new StringBuilder();

        final char delimChar = '_';
        boolean lower = false;
        for (int charInd = 0; charInd < input.length(); ++charInd) {
            final char valueChar = input.charAt(charInd);
            if (valueChar == delimChar) {
                lower = false;
            } else if (lower) {
                sb.append(Character.toLowerCase(valueChar));
            } else {
                sb.append(Character.toUpperCase(valueChar));
                lower = true;
            }
        }

        return sb.toString();
    }

    @Override
    @ServiceModel(serves = @DomainModel(name = "String", action = "toSnakeCase"))
    public String toSnakeCase(String input) {
        assert input != null : "input cannot be null";
        throw new UnsupportedOperationException("toSnakeCase not implemented yet");
    }

}
