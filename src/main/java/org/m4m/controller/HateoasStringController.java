package org.m4m.controller;

import org.m4m.resource.StringResource;
import org.m4m.resource.annotation.DomainModel;
import org.m4m.resource.annotation.ResourceModel;
import org.m4m.service.StringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


//@DomainModel(name = "String", actions = {"toUpperCase"})
@RestController
public class HateoasStringController {

    private final StringService stringService;

    @Autowired
    public HateoasStringController(StringService stringService) {
        this.stringService = stringService;
    }

    @ResourceModel(rel = "String::toUpperCase", publishes = @DomainModel(name = "String", action = "toUpperCase"))
    @RequestMapping(value = "/toUpperCase", method = RequestMethod.GET)
    public HttpEntity<StringResource> toUpperCaseWithRequestParam(@RequestParam(value = "string") String string) {
        final String toUpperString = stringService.toUpperCase(string);
        return new ResponseEntity<>(new StringResource(toUpperString),
                HttpStatus.OK);
    }

    @ResourceModel(rel = "String::toUpper2", publishes = @DomainModel(name = "String", action = "toUpperCase"))
    @RequestMapping(value = "/string/{string}/toUpperCase", method = RequestMethod.GET)
    public HttpEntity<StringResource> toUpperCaseWithPathVariable(@PathVariable String string) {
        final String toUpperString = stringService.toUpperCase(string);
        return new ResponseEntity<>(new StringResource(toUpperString),
                HttpStatus.OK);
    }

    @ResourceModel(rel = "String::toLowerCase", publishes = @DomainModel(name = "String", action = "toLowerCase"))
    @RequestMapping(value = "/string/{string}/toLowerCase", method = RequestMethod.GET)
    public HttpEntity<StringResource> toLowerCase(@PathVariable String string) {
        final String toLowerString = stringService.toLowerCase(string);
        return new ResponseEntity<>(new StringResource(toLowerString),
                HttpStatus.OK);
    }

    @ResourceModel(rel = "String::toCamelCase", publishes = @DomainModel(name = "String", action = "toCamelCase"))
    @RequestMapping(value = "/string/{string}/toCamelCase", method = RequestMethod.GET)
    public HttpEntity<StringResource> toCamelCase(@PathVariable String string) {
        final String toCamelCase = stringService.toCamelCase(string);
        return new ResponseEntity<>(new StringResource(toCamelCase),
                HttpStatus.OK);
    }


}
