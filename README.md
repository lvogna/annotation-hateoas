# annotation-hateoas

<b>Problem</b>

This project aims to make the usage of [HATEOAS](https://en.wikipedia.org/wiki/HATEOAS) easier. 

Given a service defined as

```java
public class DomainStringService implements StringService {
  public String toLowerCase(String input) {
        assert input != null : "input cannot be null";
        return input.toLowerCase();
  }
}
```

And a controller defined as

```java
public class HateoasStringController {
   public HttpEntity<StringResource> toLowerCase(@PathVariable String string) {
        final String toLowerString = stringService.toLowerCase(string);
        return new ResponseEntity<>(new StringResource(toLowerString), HttpStatus.OK);
    }
}
```

Then
1) the security must written in one place, preferably the Service;
2) the security should be inherited by the controller;
3) the security should affect the links provided by HATEOAS;
4) the authorized links should be added to the response transparently, without boilerplate in the controller;

<b>Approach</b>

DomainModel
- derives ServiceModel;

```java
    @ServiceModel(@DomainModel(uniqueIdentifier))
    @Secured("CAN_TO_LOWER_CASE")
    public HttpEntity<StringResource> toLowerCase(@PathVariable String string);
```
    
- derives ResourceModel;

```java
   @ResourceModel(rel="myToLowerCaseOperation", @DomainModel(uniqueIdentifier))
   public HttpEntity<StringResource> toLowerCase(@PathVariable String string);
```

An async process scans for the annotation placeholders and registers services and rest controller according to domain model definitions. An aspect intercepts rest calls after the rest calls are served in order to decorate the response with authorized links.

-----------------------------
The (4) point is still ongoing. 
