mvn --file source/pom.xml clean package --define skipTests=true

# model=codellama2
model=claude3
budget=5

experimentRunName=$(date +"%Y%m%dT%H%M")-$model
seed=6639355510491368
maxInputs=500
maxCounterexamples=1
unset DISPLAY

# Single example test run
# mvn --file source/pom.xml -Dengine="neural" -Dmodel="$model" -Dcodehints -Dbudget="$budget" --define java.awt.headless=true --define resynth.synthesis.javadoc=true --define resynth.fuzzing.random=false --define resynth.verification.stage1.maxInputs=$maxInputs --define resynth.verification.stage1.maxCounterexamples=$maxCounterexamples --define resynth.verification.stage2.maxInputs=0 --define resynth.verification.stage2.maxCounterexamples=0 --define resynth.seed=$seed --define failIfNoTests=false --define trimStackTrace=false --define test=uk.ac.ox.cs.refactoring.synthesis.experiment.javax_swing_AbstractButtonTest#getLabel test -pl synthesis -am
# mvn --file source/pom.xml -Dengine="neural" -Dmodel="$model" -Dcodehints -Dbudget="$budget" --define java.awt.headless=true --define resynth.synthesis.javadoc=true --define resynth.fuzzing.random=false --define resynth.verification.stage1.maxInputs=$maxInputs --define resynth.verification.stage1.maxCounterexamples=$maxCounterexamples --define resynth.verification.stage2.maxInputs=0 --define resynth.verification.stage2.maxCounterexamples=0 --define resynth.seed=$seed --define failIfNoTests=false --define trimStackTrace=false --define test=uk.ac.ox.cs.refactoring.synthesis.experiment.javax_swing_JMenuBarTest#getMenuBar test -pl synthesis -am
# exit 0

if [ -d $experimentRunName ]; then
  echo "Folder exists"
  exit 1
fi

mkdir $experimentRunName

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

# Run CEGIS-Neural w/ code hints
mvn --file source/pom.xml clean package --define skipTests=true
mkdir source/synthesis/target/surefire-reports
for testName in ${allTestNames[@]}; do
  echo "Running $testName.."
  mvn --file source/pom.xml -Dengine="neural" -Dmodel="$model" -Dbudget="$budget" -Dcodehints --define java.awt.headless=true --define resynth.synthesis.javadoc=true --define resynth.fuzzing.random=false --define resynth.verification.stage1.maxInputs=$maxInputs --define resynth.verification.stage1.maxCounterexamples=$maxCounterexamples --define resynth.verification.stage2.maxInputs=0 --define resynth.verification.stage2.maxCounterexamples=0 --define resynth.seed=$seed --define failIfNoTests=false --define trimStackTrace=false --define test="uk.ac.ox.cs.refactoring.synthesis.experiment.$testName" test >"source/synthesis/target/surefire-reports/$testName.log" 2>&1
done
mvn --file source/pom.xml --projects='!instrument' --also-make --define exec.classpathScope=test --define exec.mainClass=uk.ac.ox.cs.refactoring.synthesis.cli.Experiment compile test-compile exec:java
mv source/synthesis/target/surefire-reports/summary.json source/synthesis/target/surefire-reports/code-hints.json
cp -r source/synthesis/target/surefire-reports $experimentRunName/code-hints

# # Run CEGIS-Neural
# mvn --file source/pom.xml clean package --define skipTests=true
# mkdir source/synthesis/target/surefire-reports
# for testName in ${allTestNames[@]}; do
#   echo "Running $testName.."
#   mvn --file source/pom.xml -Dengine="neural" -Dmodel="$model" -Dbudget="$budget" --define java.awt.headless=true --define resynth.synthesis.javadoc=true --define resynth.fuzzing.random=false --define resynth.verification.stage1.maxInputs=$maxInputs --define resynth.verification.stage1.maxCounterexamples=$maxCounterexamples --define resynth.verification.stage2.maxInputs=0 --define resynth.verification.stage2.maxCounterexamples=0 --define resynth.seed=$seed --define failIfNoTests=false --define trimStackTrace=false --define test="uk.ac.ox.cs.refactoring.synthesis.experiment.$testName" test >"source/synthesis/target/surefire-reports/$testName.log" 2>&1
# done
# mvn --file source/pom.xml --projects='!instrument' --also-make --define exec.classpathScope=test --define exec.mainClass=uk.ac.ox.cs.refactoring.synthesis.cli.Experiment compile test-compile exec:java
# mv source/synthesis/target/surefire-reports/summary.json source/synthesis/target/surefire-reports/type.json
# cp -r source/synthesis/target/surefire-reports $experimentRunName/type



# Generate experimental table.tex
# mvn --file source/pom.xml --projects='!instrument' --also-make --define exec.classpathScope=test --define exec.mainClass=uk.ac.ox.cs.refactoring.synthesis.cli.TableGenerator --define exec.args="source/synthesis/$experimentRunName/type/type.json source/synthesis/$experimentRunName/code-hints/code-hints.json" compile exec:java
