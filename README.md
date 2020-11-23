# Refactoring synthesis

## Prerequisites
- Maven
- JDK 14

## Build
To build all Java projects:
```shell
mvn package --define skipTests --file source/pom.xml
```

## Tests
To run the project's unit tests:
```shell
mvn test --file source/pom.xml
```

## Fuzzing experiments
To run fuzzing performance experiments:
```shell
mvn test --define enableFuzzerExperiments --file source/pom.xml --projects fuzzer-experiments
```
