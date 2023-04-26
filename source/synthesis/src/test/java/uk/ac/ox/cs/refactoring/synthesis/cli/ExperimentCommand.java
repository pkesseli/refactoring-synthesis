package uk.ac.ox.cs.refactoring.synthesis.cli;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import picocli.CommandLine.Command;
import picocli.CommandLine.ExitCode;
import uk.ac.ox.cs.refactoring.synthesis.statistics.FuzzedInputs;
import uk.ac.ox.cs.refactoring.synthesis.statistics.Report;
import uk.ac.ox.cs.refactoring.synthesis.statistics.Reports;

@Command(name = "experiment")
class ExperimentCommand implements Callable<Integer> {

  @Override
  public Integer call() throws Exception {
    final Path surefireReportsDirectory = Reports.getSurefireReportsDirectory();
    final Report summary = new Report();
    final ObjectMapper objectMapper = new ObjectMapper();
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(surefireReportsDirectory, "*.json")) {
      for (final Path entry : stream) {
        final Report report;
        try (final InputStream is = Files.newInputStream(entry)) {
          report = objectMapper.readValue(is, Report.class);
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
