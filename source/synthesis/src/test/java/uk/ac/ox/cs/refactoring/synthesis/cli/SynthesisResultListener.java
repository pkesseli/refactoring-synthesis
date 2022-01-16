package uk.ac.ox.cs.refactoring.synthesis.cli;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.TestSource;
import org.junit.platform.engine.support.descriptor.MethodSource;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.opentest4j.AssertionFailedError;

import uk.ac.ox.cs.refactoring.synthesis.statistics.Report;
import uk.ac.ox.cs.refactoring.synthesis.statistics.Reports;
import uk.ac.ox.cs.refactoring.synthesis.statistics.Run;

class SynthesisResultListener implements TestExecutionListener {

  private static final Pattern PARAMTER_FORMAT = Pattern.compile("^\\[\\d+\\]\\s+([^,]+),\\s+\\d+$");

  private final Report report;

  SynthesisResultListener(final Report report) {
    this.report = report;
  }

  private Run getLastRun(final TestIdentifier testIdentifier) {
    final String benchmarkName = getBenchmarkName(testIdentifier);
    if (benchmarkName == null)
      return null;

    final List<Run> runs = report.Benchmarks.get(benchmarkName);
    return runs.get(runs.size() - 1);
  }

  @Override
  public void executionFinished(final TestIdentifier testIdentifier, final TestExecutionResult testExecutionResult) {
    final Run run = getLastRun(testIdentifier);
    if (run == null)
      return;

    switch (testExecutionResult.getStatus()) {
      case FAILED:
        if (testExecutionResult.getThrowable().map(SynthesisResultListener::isAssertionViolation).orElse(false))
          run.Unsound = true;
      default:
    }
  }

  private static boolean isAssertionViolation(final Throwable throwable) {
    return throwable instanceof AssertionFailedError || throwable instanceof AssertionError;
  }

  @Override
  public void executionSkipped(final TestIdentifier testIdentifier, final String reason) {
    final String benchmarkName = getBenchmarkName(testIdentifier);
    if (benchmarkName == null)
      return;

    final List<Run> runs = report.Benchmarks.computeIfAbsent(benchmarkName, _1 -> new ArrayList<>());
    runs.add(new Run());
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
