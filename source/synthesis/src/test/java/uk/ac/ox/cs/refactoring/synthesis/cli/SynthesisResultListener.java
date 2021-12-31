package uk.ac.ox.cs.refactoring.synthesis.cli;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.TestSource;
import org.junit.platform.engine.support.descriptor.MethodSource;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;

import uk.ac.ox.cs.refactoring.synthesis.statistics.Benchmark;
import uk.ac.ox.cs.refactoring.synthesis.statistics.Report;
import uk.ac.ox.cs.refactoring.synthesis.statistics.Reports;

class SynthesisResultListener implements TestExecutionListener {

  private static final Pattern PARAMTER_FORMAT = Pattern.compile("^\\[\\d+\\]\\s+([^,]+),\\s+\\d+$");

  private final Report report;

  SynthesisResultListener(final Report report) {
    this.report = report;
  }

  @Override
  public void executionFinished(final TestIdentifier testIdentifier, final TestExecutionResult testExecutionResult) {
    final String benchmarkName = getBenchmarkName(testIdentifier);
    if (benchmarkName == null)
      return;

    final Benchmark benchmark = report.Benchmarks.get(benchmarkName);
    switch (testExecutionResult.getStatus()) {
      case ABORTED:
      case FAILED:
        ++benchmark.Unsound;
      case SUCCESSFUL:
    }
  }

  @Override
  public void executionSkipped(final TestIdentifier testIdentifier, final String reason) {
    final String benchmarkName = getBenchmarkName(testIdentifier);
    if (benchmarkName == null)
      return;

    final Benchmark noResultResult = new Benchmark();
    noResultResult.NoResultFound = true;
    report.Benchmarks.put(benchmarkName, noResultResult);
  }

  private static String getBenchmarkName(final TestIdentifier testIdentifier) {
    final Optional<TestSource> testSource = testIdentifier.getSource();
    if (!testSource.isPresent())
      return null;
    final TestSource source = testSource.get();
    if (!(source instanceof MethodSource))
      return null;

    final MethodSource methodTestSource = (MethodSource) source;
    final String className = methodTestSource.getJavaClass().getSimpleName().replace('_', '.').replace("Test", "");
    final String methodName;
    if (methodTestSource.getMethodParameterTypes().isEmpty()) {
      methodName = methodTestSource.getMethodName();
    } else {
      final Matcher matcher = PARAMTER_FORMAT.matcher(testIdentifier.getDisplayName());
      if (!matcher.matches())
        return null;
      methodName = matcher.group(1);
    }

    return Reports.createBenchmarkName(className, methodName);
  }
}
