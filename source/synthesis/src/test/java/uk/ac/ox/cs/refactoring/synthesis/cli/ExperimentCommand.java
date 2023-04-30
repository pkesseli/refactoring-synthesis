package uk.ac.ox.cs.refactoring.synthesis.cli;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import picocli.CommandLine.Command;
import picocli.CommandLine.ExitCode;
import uk.ac.ox.cs.refactoring.synthesis.statistics.FuzzedInputs;
import uk.ac.ox.cs.refactoring.synthesis.statistics.Report;
import uk.ac.ox.cs.refactoring.synthesis.statistics.Reports;
import uk.ac.ox.cs.refactoring.synthesis.statistics.Run;

@Command(name = "experiment")
class ExperimentCommand implements Callable<Integer> {

  @Override
  public Integer call() throws Exception {
    final Path surefireReportsDirectory = Reports.getSurefireReportsDirectory();
    final Report summary = new Report();
    final ObjectMapper objectMapper = new ObjectMapper();
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(surefireReportsDirectory, "zest-*.log")) {
      for (final Path entry : stream) {
        final String logFileName = entry.getFileName().toString();
        if (logFileName.contains("GPT"))
          continue;

        final String jsonFileName = logFileName.replace("zest-javadoc-", "").replace("zest-", "").replace('_', '.')
            .replace("Test#", "#").replaceAll(".log$", ".json");
        final Path json = entry.getParent().resolve(jsonFileName);
        final Report report;
        if (Files.exists(json))
          try (final InputStream is = Files.newInputStream(json)) {
            report = objectMapper.readValue(is, Report.class);
          }
        else {
          final String benchmarkName = jsonFileName.replace(".json", "");
          System.err.println(benchmarkName);
          final List<Run> runs = new ArrayList<>();
          runs.add(new Run());
          report = new Report();
          report.Benchmarks.put(benchmarkName, runs);
        }
        merge(summary.TotalCandidates, report.TotalCandidates);
        merge(summary.TotalCounterexamples, report.TotalCounterexamples);
        summary.TotalRuntimeInMilliseconds += report.TotalRuntimeInMilliseconds;
        summary.Benchmarks.putAll(report.Benchmarks);
      }
    }

    final ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();
    try (final OutputStream os = Files.newOutputStream(surefireReportsDirectory.resolve("summary.json"))) {
      writer.writeValue(os, summary);
    }
    return ExitCode.OK;
  }

  private static void merge(final FuzzedInputs lhs, final FuzzedInputs rhs) {
    lhs.Genuine += rhs.Genuine;
    lhs.Spurious += rhs.Spurious;
  }
}
