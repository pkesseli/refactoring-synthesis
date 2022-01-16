package uk.ac.ox.cs.refactoring.synthesis.statistics;

import uk.ac.ox.cs.refactoring.synthesis.cegis.CegisLoopListener;

public final class Reports {

  public static final Report DEFAULT_REPORT = new Report();

  public static String createBenchmarkName(final String className, final String methodName) {
    return className + '#' + methodName;
  }

  public static <Candidate> CegisLoopListener<Candidate> createReportListener(final String className,
      final String methodName) {
    return new ReportCegisLoopListener<>(createBenchmarkName(className, methodName), DEFAULT_REPORT);
  }
}
