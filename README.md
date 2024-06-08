# Refactoring synthesis

## Prerequisites
- Maven
- JDK 15

## Build
To build all Java projects:
```shell
mvn package --define skipTests --file source/pom.xml
```

## Experiments
To run the JQF-Fuzz experiments, run:
```shell
./experiments.sh
```
in the root directory of the repository.

To run the neural experiments, run:
```shell
./run.sh
```
in the root directory of the repository.
For both experiments, results summaries can be found in `source/synthesis/target/surefire-reports`.

Warning: A full experimental run can
take multiple hours. The script also contains a commented out
"Single example test run" which can be enabled to just run one benchmark.


## Contributing

The following contains instructions for developers contributing to the project.

### Tests
To run the project's unit tests:
```shell
mvn test --file source/pom.xml
```

### Fuzzing experiments
To run fuzzing performance experiments:
```shell
mvn test --define enableFuzzerExperiments --file source/pom.xml --projects fuzzer-experiments
```
