# A data mapper, between data object.

![why]

[why]: ./data_mapper.png

## Usage

	@Test
    public void testMapping() {
        Value from = new Value();
        from.integerValue = 1;
        from.textValue = "test value";
        assertThat(mapper.apply(from)).isEqualTo(from);
    }

    @Test
    public void testComplexMapping() {
	
        Value from = new Value();
        from.integerValue = 1;
        from.textValue = "test value";

        mapper.addChildRuleByName("integerValue",
                new AssemblerBuilder<Integer, Integer, Value, Value>()
                        .childMapper(v -> v)
                        .emptyIfError(0)
                        .fromValueGetter(value -> value.integerValue)
                        .toValueSetter((value, integer) -> {
                            value.setIntegerValue(integer * 2);
                        })
                        .build()
        );

        assertThat(mapper.apply(from)).isNotEqualTo(from);
        assertThat(mapper.apply(from).getIntegerValue()).isEqualTo(from.getIntegerValue() * 2);
    }

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

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.
