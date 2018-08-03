# annotation-hateoas

<b>Problem</b>

This project aims to make the usage of [HATEOAS](https://en.wikipedia.org/wiki/HATEOAS) easier. 

[Given](https://docs.cucumber.io/gherkin/reference/) a [service](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/stereotype/Service.html) defined as

```java
public class DomainStringService implements StringService {
  public String toLowerCase(String input) {
        assert input != null : "input cannot be null";
        return input.toLowerCase();
  }
}
```

[And](https://docs.cucumber.io/gherkin/reference/) a [controller](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/bind/annotation/RestController.html) defined as

```java
public class HateoasStringController {
   public HttpEntity<StringResource> toLowerCase(@PathVariable String string) {
        final String toLowerString = stringService.toLowerCase(string);
        return new ResponseEntity<>(new StringResource(toLowerString), HttpStatus.OK);
    }
}
```

[Then](https://docs.cucumber.io/gherkin/reference/)
1) the security must written in one place, preferably the Service;
2) the security should be inherited by the controller;
3) the security should affect the links provided by HATEOAS;
4) the authorized links should be added to the response transparently, without boilerplate in the controller;

<b>Approach</b>

[DomainModel](https://domainlanguage.com/ddd/)
- derives [ServiceModel](http://docs.oasis-open.org/soa-rm/soa-ra/v1.0/cs01/soa-ra-v1.0-cs01.html);

```java
    @ServiceModel(@DomainModel(uniqueIdentifier))
    @Secured("CAN_TO_LOWER_CASE")
    public HttpEntity<StringResource> toLowerCase(@PathVariable String string);
```
    
- derives [ResourceModel](https://www.ics.uci.edu/~fielding/pubs/dissertation/rest_arch_style.htm);

```java
   @ResourceModel(rel="myToLowerCaseAction", @DomainModel(uniqueIdentifier))
   public HttpEntity<StringResource> toLowerCase(@PathVariable String string);
```

An async process scans for the annotation placeholders and registers services and rest controller according to domain model definitions. An aspect intercepts rest calls after the rest calls are served in order to decorate the response with authorized links.

-----------------------------
The (4) point is still ongoing. 
