mvn --file source/pom.xml clean package --define skipTests=true

seed=6639355510491368
maxInputs=500
maxCounterexamples=1

# Single example test run
# mvn --file source/pom.xml --define resynth.synthesis.javadoc=true --define resynth.fuzzing.random=false --define resynth.verification.stage1.maxInputs=$maxInputs --define resynth.verification.stage1.maxCounterexamples=$maxCounterexamples --define resynth.verification.stage2.maxInputs=0 --define resynth.verification.stage2.maxCounterexamples=0 --define resynth.seed=$seed --define failIfNoTests=false --define trimStackTrace=false --define test=java_awt_TextFieldTest#minimumSize clean test >zest-javadoc.log 2>&1
# mvn --file source/pom.xml --projects='!instrument' --also-make --define exec.classpathScope=test --define exec.mainClass=uk.ac.ox.cs.refactoring.synthesis.cli.Experiment compile exec:java
# mv source/synthesis/target/surefire-reports/summary.json source/synthesis/zest-javadoc.json
# exit 0

mvn --file source/pom.xml --define resynth.synthesis.javadoc=false --define resynth.fuzzing.random=false --define resynth.verification.stage1.maxInputs=$maxInputs --define resynth.verification.stage1.maxCounterexamples=$maxCounterexamples --define resynth.verification.stage2.maxInputs=0 --define resynth.verification.stage2.maxCounterexamples=0 --define resynth.seed=$seed --define failIfNoTests=false --define trimStackTrace=false --define test="uk.ac.ox.cs.refactoring.synthesis.experiment.**" clean test >zest.log 2>&1
mvn --file source/pom.xml --projects='!instrument' --also-make --define exec.classpathScope=test --define exec.mainClass=uk.ac.ox.cs.refactoring.synthesis.cli.Experiment compile exec:java
cp -r source/synthesis/target/surefire-reports source/synthesis/results-zest

mvn --file source/pom.xml --define resynth.synthesis.javadoc=true --define resynth.fuzzing.random=false --define resynth.verification.stage1.maxInputs=$maxInputs --define resynth.verification.stage1.maxCounterexamples=$maxCounterexamples --define resynth.verification.stage2.maxInputs=0 --define resynth.verification.stage2.maxCounterexamples=0 --define resynth.seed=$seed --define failIfNoTests=false --define trimStackTrace=false --define test="uk.ac.ox.cs.refactoring.synthesis.experiment.**" clean test >zest-javadoc.log 2>&1
mvn --file source/pom.xml --projects='!instrument' --also-make --define exec.classpathScope=test --define exec.mainClass=uk.ac.ox.cs.refactoring.synthesis.cli.Experiment compile exec:java
cp -r source/synthesis/target/surefire-reports source/synthesis/results-zest-javadoc

mvn --file source/pom.xml --define resynth.synthesis.javadoc=false --define resynth.fuzzing.random=true --define resynth.verification.stage1.maxInputs=$maxInputs --define resynth.verification.stage1.maxCounterexamples=$maxCounterexamples --define resynth.verification.stage2.maxInputs=0 --define resynth.verification.stage2.maxCounterexamples=0 --define resynth.seed=$seed --define failIfNoTests=false --define trimStackTrace=false --define test="uk.ac.ox.cs.refactoring.synthesis.experiment.**" clean test >random.log 2>&1
mvn --file source/pom.xml --projects='!instrument' --also-make --define exec.classpathScope=test --define exec.mainClass=uk.ac.ox.cs.refactoring.synthesis.cli.Experiment compile exec:java
cp -r source/synthesis/target/surefire-reports source/synthesis/results-random

mvn --file source/pom.xml --define resynth.synthesis.javadoc=true --define resynth.fuzzing.random=true --define resynth.verification.stage1.maxInputs=$maxInputs --define resynth.verification.stage1.maxCounterexamples=$maxCounterexamples --define resynth.verification.stage2.maxInputs=0 --define resynth.verification.stage2.maxCounterexamples=0 --define resynth.seed=$seed --define failIfNoTests=false --define trimStackTrace=false --define test="uk.ac.ox.cs.refactoring.synthesis.experiment.**" test >random-javadoc.log 2>&1
mvn --file source/pom.xml --projects='!instrument' --also-make --define exec.classpathScope=test --define exec.mainClass=uk.ac.ox.cs.refactoring.synthesis.cli.Experiment compile exec:java
cp -r source/synthesis/target/surefire-reports source/synthesis/results-random-javadoc

# mvn --file source/pom.xml --projects='!instrument' --also-make --define exec.classpathScope=test --define exec.mainClass=uk.ac.ox.cs.refactoring.synthesis.cli.TableGenerator --define exec.args='source/synthesis/results-zest/summary.json source/synthesis/results-zest-javadoc/summary.json' compile exec:java
