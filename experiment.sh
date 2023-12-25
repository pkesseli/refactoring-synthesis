mvn --file source/pom.xml clean package --define skipTests=true

experimentRunName=experiment-CHANGE-ME
seed=6639355510491368
maxInputs=500
maxCounterexamples=1
unset DISPLAY

# Parallel GPT experiments
# Find all benchmark names
allTestNames=()
for testCompilationUnit in source/synthesis/src/test/java/uk/ac/ox/cs/refactoring/synthesis/gpt/*Test.java; do
  fileName=`basename $testCompilationUnit`
  className=${fileName::-5}
  methodNames=`grep -A 1 '@Test' $testCompilationUnit | grep 'void' | sed 's/\s*void\s*\([^(]*\).*/\1/'`
  for methodName in $methodNames; do
    testName="$className#$methodName"
    allTestNames+=($testName)
  done
done

# Run GPT
mkdir source/synthesis/target/surefire-reports
N=4
counter=0
for testName in ${allTestNames[@]}; do
  BUILD=target/tmp-"$counter"
  let counter++
  (
    echo "starting task $testName.."
    mvn --file source/pom.xml --define java.awt.headless=true --define resynth.synthesis.javadoc=true --define resynth.fuzzing.random=false --define resynth.verification.stage1.maxInputs=$maxInputs --define resynth.verification.stage1.maxCounterexamples=$maxCounterexamples --define resynth.verification.stage2.maxInputs=0 --define resynth.verification.stage2.maxCounterexamples=0 --define resynth.seed=$seed --define failIfNoTests=false --define trimStackTrace=false --define test="uk.ac.ox.cs.refactoring.synthesis.gpt.$testName" test -PcustomisedBuildDirectory -Dalternative="$BUILD" > "source/synthesis/target/surefire-reports/$testName.log" 2>&1

    cp source/synthesis/$BUILD/surefire-reports/* source/synthesis/target/surefire-reports/
    rm -rf source/synthesis/$BUILD
  ) &

  # allow to execute up to $N jobs in parallel
  if [[ $(jobs -r -p | wc -l) -ge $N ]]; then
    # now there are $N jobs already running, so wait here for any job
    # to be finished so there is a place to start next one.
    wait -n
  fi
done
mvn --file source/pom.xml --projects='!instrument' --also-make --define exec.classpathScope=test --define exec.mainClass=uk.ac.ox.cs.refactoring.synthesis.cli.Experiment compile test-compile exec:java
mv source/synthesis/target/surefire-reports/summary.json source/synthesis/target/surefire-reports/type.json
cp -r source/synthesis/target/surefire-reports source/synthesis/gpt-results
exit 0


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
