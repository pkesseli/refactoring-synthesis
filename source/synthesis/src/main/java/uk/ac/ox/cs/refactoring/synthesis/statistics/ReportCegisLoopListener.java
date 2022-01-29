package uk.ac.ox.cs.refactoring.synthesis.statistics;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

import uk.ac.ox.cs.refactoring.classloader.JavaLanguage;
import uk.ac.ox.cs.refactoring.synthesis.cegis.CegisLoopListener;
import uk.ac.ox.cs.refactoring.synthesis.counterexample.Counterexample;
import uk.ac.ox.cs.refactoring.synthesis.invocation.ExecutionResult;

class ReportCegisLoopListener<Candidate> implements CegisLoopListener<Candidate> {

  private final String benchmarkName;

  private final Report report;

  private final Run run = new Run();

  private Instant start;

  ReportCegisLoopListener(final String benchmarkName, final Report report) {
    this.benchmarkName = benchmarkName.replace(JavaLanguage.INNER_CLASS_SEPARATOR, JavaLanguage.PACKAGE_SEPARATOR);
    this.report = report;
  }

  @Override
  public void initial(final Candidate candidate) {
    start = Instant.now();
  }

  @Override
  public void spurious(final Candidate candidate) {
    ++run.Candidates.Spurious;
  }

  @Override
  public void genuine(final Candidate candidate) {
    ++run.Candidates.Genuine;
    ++run.Rounds;
  }

  @Override
  public void spurious(final Counterexample counterexample) {
    ++run.Counterexamples.Spurious;
  }

  @Override
  public void genuine(final Counterexample counterexample, final ExecutionResult expected,
      final ExecutionResult actual) {
    ++run.Counterexamples.Genuine;
  }

  @Override
  public void verified(final Candidate candidate) {
    run.Solution = candidate.toString();
  }

  @Override
  public void close() {
    final Instant end = Instant.now();
    run.RuntimeInMilliseconds = Duration.between(start, end).toMillis();
    report.Benchmarks.computeIfAbsent(benchmarkName, _1 -> new ArrayList<>()).add(run);

    report.TotalRuntimeInMilliseconds += run.RuntimeInMilliseconds;
    report.TotalCandidates.Genuine += run.Candidates.Genuine;
    report.TotalCandidates.Spurious += run.Candidates.Spurious;
    report.TotalCounterexamples.Genuine += run.Counterexamples.Genuine;
    report.TotalCounterexamples.Spurious += run.Counterexamples.Spurious;
  }
}
