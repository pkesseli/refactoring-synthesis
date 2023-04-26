package uk.ac.ox.cs.refactoring.synthesis.statistics;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import uk.ac.ox.cs.refactoring.synthesis.cegis.CegisLoopListener;

public final class Reports {

  public static final Report DEFAULT_REPORT = new Report();

  public static String createBenchmarkName(final String className, final String methodName) {
    return className + '#' + methodName;
  }

  public static <Candidate> CegisLoopListener<Candidate> createReportListener(final String benchmarkName,
      final boolean foundCodeHints) {
    return new ReportCegisLoopListener<>(benchmarkName, DEFAULT_REPORT, foundCodeHints);
  }

  public static Path getSurefireReportsDirectory() throws URISyntaxException {
    final URL location = Reports.class.getProtectionDomain().getCodeSource().getLocation();
    return Paths.get(location.toURI()).getParent().resolve("surefire-reports");
  }
}
