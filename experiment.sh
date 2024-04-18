mvn --file source/pom.xml clean package --define skipTests=true

experimentRunName=test-neural
seed=6639355510491368
maxInputs=500
maxCounterexamples=1
unset DISPLAY

# Single example test run
# mvn --file source/pom.xml --define java.awt.headless=true --define resynth.synthesis.javadoc=true --define resynth.fuzzing.random=false --define resynth.verification.stage1.maxInputs=$maxInputs --define resynth.verification.stage1.maxCounterexamples=$maxCounterexamples --define resynth.verification.stage2.maxInputs=0 --define resynth.verification.stage2.maxCounterexamples=0 --define resynth.seed=$seed --define failIfNoTests=false --define trimStackTrace=false --define test=uk.ac.ox.cs.refactoring.synthesis.experiment.java_awt_ListTest#clear test -pl synthesis -am
# exit 0

# Find all benchmark names
allTestNames=()
for testCompilationUnit in source/synthesis/src/test/java/uk/ac/ox/cs/refactoring/synthesis/experiment/*Test.java; do
  fileName=`basename $testCompilationUnit`
  className=${fileName::-5}
  methodNames=`grep -A 1 '@Test' $testCompilationUnit | grep 'void' | sed 's/\s*void\s*\([^(]*\).*/\1/'`
  for methodName in $methodNames; do
    testName="$className#$methodName"
    allTestNames+=($testName)
  done
done

# Run CEGIS-Neural
mkdir source/synthesis/target/surefire-reports
for testName in ${allTestNames[@]}; do
  echo "Running $testName.."
  mvn --file source/pom.xml -Dengine="neural" --define java.awt.headless=true --define resynth.synthesis.javadoc=true --define resynth.fuzzing.random=false --define resynth.verification.stage1.maxInputs=$maxInputs --define resynth.verification.stage1.maxCounterexamples=$maxCounterexamples --define resynth.verification.stage2.maxInputs=0 --define resynth.verification.stage2.maxCounterexamples=0 --define resynth.seed=$seed --define failIfNoTests=false --define trimStackTrace=false --define test="uk.ac.ox.cs.refactoring.synthesis.experiment.$testName" test >"source/synthesis/target/surefire-reports/$testName.log" 2>&1
done
mvn --file source/pom.xml --projects='!instrument' --also-make --define exec.classpathScope=test --define exec.mainClass=uk.ac.ox.cs.refactoring.synthesis.cli.Experiment compile test-compile exec:java
mv source/synthesis/target/surefire-reports/summary.json source/synthesis/target/surefire-reports/type.json
cp -r source/synthesis/target/surefire-reports source/synthesis/$experimentRunName/neural
exit 0

# Run CEGIS-Fuzz w/ code hints
mkdir source/synthesis/target/surefire-reports
for testName in ${allTestNames[@]}; do
  mvn --file source/pom.xml --define java.awt.headless=true --define resynth.synthesis.javadoc=true --define resynth.fuzzing.random=false --define resynth.verification.stage1.maxInputs=$maxInputs --define resynth.verification.stage1.maxCounterexamples=$maxCounterexamples --define resynth.verification.stage2.maxInputs=0 --define resynth.verification.stage2.maxCounterexamples=0 --define resynth.seed=$seed --define failIfNoTests=false --define trimStackTrace=false --define test="uk.ac.ox.cs.refactoring.synthesis.experiment.$testName" test >"source/synthesis/target/surefire-reports/$testName.log" 2>&1
done
mvn --file source/pom.xml --projects='!instrument' --also-make --define exec.classpathScope=test --define exec.mainClass=uk.ac.ox.cs.refactoring.synthesis.cli.Experiment compile test-compile exec:java
mv source/synthesis/target/surefire-reports/summary.json source/synthesis/target/surefire-reports/type.json
cp -r source/synthesis/target/surefire-reports source/synthesis/$experimentRunName/zest-javadoc

# Run CEGIS-Fuzz w/ type
mvn --file source/pom.xml clean package --define skipTests=true
mkdir source/synthesis/target/surefire-reports
for testName in ${allTestNames[@]}; do
  mvn --file source/pom.xml --define java.awt.headless=true --define resynth.synthesis.javadoc=false --define resynth.fuzzing.random=false --define resynth.verification.stage1.maxInputs=$maxInputs --define resynth.verification.stage1.maxCounterexamples=$maxCounterexamples --define resynth.verification.stage2.maxInputs=0 --define resynth.verification.stage2.maxCounterexamples=0 --define resynth.seed=$seed --define failIfNoTests=false --define trimStackTrace=false --define test="uk.ac.ox.cs.refactoring.synthesis.experiment.$testName" test >"source/synthesis/target/surefire-reports/$testName.log" 2>&1
done
mvn --file source/pom.xml --projects='!instrument' --also-make --define exec.classpathScope=test --define exec.mainClass=uk.ac.ox.cs.refactoring.synthesis.cli.Experiment compile test-compile exec:java
mv source/synthesis/target/surefire-reports/summary.json source/synthesis/target/surefire-reports/code-hints.json
cp -r source/synthesis/target/surefire-reports source/synthesis/$experimentRunName/zest

# Generate experimental table.tex
mvn --file source/pom.xml --projects='!instrument' --also-make --define exec.classpathScope=test --define exec.mainClass=uk.ac.ox.cs.refactoring.synthesis.cli.TableGenerator --define exec.args="source/synthesis/$experimentRunName/zest/type.json source/synthesis/$experimentRunName/zest-javadoc/code-hints.json" compile exec:java
