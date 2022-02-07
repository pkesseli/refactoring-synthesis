$repetitions=1
mvn --file source/pom.xml clean install --define skipTests=true
mvn --file source/pom.xml --projects=synthesis --define exec.classpathScope=test --define exec.mainClass=uk.ac.ox.cs.refactoring.synthesis.cli.Experiment --define exec.args="--repetitions=$repetitions --javadoc=false --random-guidance=false --stage-one-max-inputs=500 --stage-one-max-counterexamples=1 --stage-two-max-inputs=0 --stage-two-max-counterexamples=0 --json-report-file-path=./zest.json" exec:java 2>&1 | Tee-Object zest.log
mvn --file source/pom.xml --projects=synthesis --define exec.classpathScope=test --define exec.mainClass=uk.ac.ox.cs.refactoring.synthesis.cli.Experiment --define exec.args="--repetitions=$repetitions --javadoc=true --random-guidance=false --stage-one-max-inputs=500 --stage-one-max-counterexamples=1 --stage-two-max-inputs=0 --stage-two-max-counterexamples=0 --json-report-file-path=./zest-javadoc.json" exec:java  2>&1 | Tee-Object zest-javadoc.log
mvn --file source/pom.xml --projects=synthesis --define exec.classpathScope=test --define exec.mainClass=uk.ac.ox.cs.refactoring.synthesis.cli.Experiment --define exec.args="--repetitions=$repetitions --javadoc=false --random-guidance=true --stage-one-max-inputs=500 --stage-one-max-counterexamples=1 --stage-two-max-inputs=0 --stage-two-max-counterexamples=0 --json-report-file-path=./random.json" exec:java  2>&1 | Tee-Object random.log
mvn --file source/pom.xml --projects=synthesis --define exec.classpathScope=test --define exec.mainClass=uk.ac.ox.cs.refactoring.synthesis.cli.Experiment --define exec.args="--repetitions=$repetitions --javadoc=true --random-guidance=true --stage-one-max-inputs=500 --stage-one-max-counterexamples=1 --stage-two-max-inputs=0 --stage-two-max-counterexamples=0 --json-report-file-path=./random-javadoc.json" exec:java  2>&1 | Tee-Object random-javadoc.log