package uk.ac.ox.cs.refactoring.synthesis.statistics;

import java.time.Duration;
import java.time.Instant;

import uk.ac.ox.cs.refactoring.synthesis.cegis.CegisLoopListener;
import uk.ac.ox.cs.refactoring.synthesis.counterexample.Counterexample;
import uk.ac.ox.cs.refactoring.synthesis.invocation.ExecutionResult;

class ReportCegisLoopListener<Candidate> implements CegisLoopListener<Candidate> {

  private final String benchmarkName;

  private final Report report;

  private Instant start;

  private Instant end;

  private int spuriousCounterexamples;

  private int genuineCounterexamples;

  private int verificationPhases = 1;

  private int spuriousCandidates;

  private int genuineCandidates;

  ReportCegisLoopListener(final String benchmarkName, final Report report) {
    this.benchmarkName = benchmarkName;
    this.report = report;
  }

  @Override
  public void initial(final Candidate candidate) {
    start = Instant.now();
  }

  @Override
  public void spurious(final Candidate candidate) {
    ++spuriousCandidates;
  }

  @Override
  public void genuine(final Candidate candidate) {
    ++genuineCandidates;
    ++verificationPhases;
  }

  @Override
  public void spurious(final Counterexample counterexample) {
    ++spuriousCounterexamples;
  }

  @Override
  public void genuine(final Counterexample counterexample, final ExecutionResult expected,
      final ExecutionResult actual) {
    ++genuineCounterexamples;
  }

  @Override
  public void verified(final Candidate candidate) {
    end = Instant.now();
    saveToReport();
  }

  private void saveToReport() {
    final Benchmark benchmark = new Benchmark();
    benchmark.RuntimeInMilliseconds = Duration.between(start, end).toMillis();
    benchmark.Rounds = verificationPhases;
    benchmark.Candidates.Genuine = genuineCandidates;
    benchmark.Candidates.Spurious = spuriousCandidates;
    benchmark.Counterexamples.Genuine = genuineCounterexamples;
    benchmark.Counterexamples.Spurious = spuriousCounterexamples;
    report.Benchmarks.put(benchmarkName, benchmark);

    report.TotalRuntimeInMilliseconds += benchmark.RuntimeInMilliseconds;
    report.TotalCandidates.Genuine += genuineCandidates;
    report.TotalCandidates.Spurious += spuriousCandidates;
    report.TotalCounterexamples.Genuine += genuineCounterexamples;
    report.TotalCounterexamples.Spurious += spuriousCounterexamples;
  }
}
