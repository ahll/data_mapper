# A data mapper, between data object.

![why]

[why]: ./data_mapper.png

Divide and rule for complex attributes, and clone all same attribute. 


## Getting Started

This is Maven based, Java SE project with Git version control. 

### Prerequisites

[```Require JAVA 1.8 SDK```](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)

[```Require Maven 3.0```](https://maven.apache.org/download.cgi)

[```Require SourceTree```](https://www.sourcetreeapp.com/)

## Instructions 


### Build and packages

```console
mvn clean install
```

### Testing

||**Command**|**Notes**|
| --- | --- | --- |
| **Unit Tests** | ```mvn test``` | Unit tests files should end with prefix **-Test**. |
| **Integration Tests** | ```mvn integration-test``` | Integration test files should end with prefix **-IT**. |

#### Options

|||
| --- | --- |
|```-DskipUTs=true```| Skipping unit tests. |
|```-DskipITs=true```| Skipping integration tests. |
|```-DskipTests=true```| Skipping all tests. |


## Resources

* [Maven](https://maven.apache.org/) - Dependency Management

## Version

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the tags on this [repository](https://github.com/your/project/tags). 

## Author


## License

Copyright (c) 2018, stico and/or its affiliates. All rights reserved.
