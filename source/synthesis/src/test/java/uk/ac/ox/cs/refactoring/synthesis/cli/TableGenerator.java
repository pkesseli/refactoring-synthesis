package uk.ac.ox.cs.refactoring.synthesis.cli;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.math.NumberUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import uk.ac.ox.cs.refactoring.synthesis.statistics.Report;
import uk.ac.ox.cs.refactoring.synthesis.statistics.Run;

public class TableGenerator {

  private static final int NUMBER_OF_SUMMARY_COLUMNS = 10;

  public static void main(final String[] args) throws IOException {
    final Path[] files = Parameters.getJsonFiles(args);
    final int numberOfConfigurations = files.length + 1;
    final List<List<String>> cells = getCells(files);
    final int numberOfBenchmarks = cells.size() - NUMBER_OF_SUMMARY_COLUMNS - 1;
    try (final OutputStream os = Files.newOutputStream(Paths.get("table.tex"));
        final OutputStream buffer = new BufferedOutputStream(os);
        final PrintWriter writer = new PrintWriter(buffer)) {

      writer.println("\\documentclass{article}");
      writer.println("\\usepackage{longtable}");
      writer.println("\\usepackage{pifont}");
      writer.println("\\usepackage{wasysym}");
      writer.println("\\newcommand{\\xmark}{\\ding{55}}");
      writer.println("\\begin{document}");

      writer.print("Number of benchmarks: ");
      writer.println(numberOfBenchmarks);

      writer.print("\\begin{longtable}{ l ");
      for (int i = 0; i < numberOfConfigurations; ++i)
        writer.print("r ");
      writer.println('}');

      for (int rowIndex = 0; rowIndex < cells.size(); ++rowIndex) {
        final List<String> row = cells.get(rowIndex);
        writer.print(String.join(" & ", row));
        writer.println(" \\\\");

        if (rowIndex == 0)
          writer.println("\\hline");
        if (rowIndex == cells.size() - NUMBER_OF_SUMMARY_COLUMNS - 1) {
          writer.println("\\hline");
          writer.println("\\hline");
        }
      }
      writer.println("\\end{longtable}");
      writer.println("\\end{document}");
    }
  }

  private static final String UNSOUND_MARKER = "\\multicolumn{1}{c}{\\lightning}";

  private static final String MISSED_MARKER = "\\multicolumn{1}{c}{\\xmark}";

  private static List<List<String>> getCells(final Path[] jsonFiles) throws IOException {
    if (jsonFiles.length == 0)
      return Collections.emptyList();

    final List<List<String>> cells = new ArrayList<>();
    final int numberOfConfigurations = jsonFiles.length + 1;
    final int numberOfColumns = numberOfConfigurations + 1;
    int numberOfRows = -1;
    final List<Report> reports = getReports(jsonFiles);
    final Map<String, Boolean> benchmarkNameToFoundCodeHints = new HashMap<>();
    for (int column = 0; column < numberOfConfigurations; ++column) {
      final Report report;
      final String configName;
      if (column < jsonFiles.length) {
        report = reports.get(column);
        final Path jsonFile = jsonFiles[column];
        configName = jsonFile.getFileName().toString().replace(".json", "").replace("javadoc", "jd").replace("random",
            "rand");
      } else {
        report = getHypotheticalFallbackReport(reports);
        configName = "JQF-full";
      }

      final int numberOfBenchmarks = report.Benchmarks.size();
      numberOfRows = numberOfBenchmarks + NUMBER_OF_SUMMARY_COLUMNS + 1;
      if (cells.isEmpty()) {
        for (int i = 0; i < numberOfRows; ++i)
          cells.add(new ArrayList<>(Collections.nCopies(numberOfColumns, null)));

        cells.get(0).set(0, "Benchmark");
      }

      final int columnIndex = column + 1;
      cells.get(0).set(columnIndex, configName);
      int rowIndex = 1;
      for (final Map.Entry<String, List<Run>> benchmark : report.Benchmarks.entrySet()) {
        final List<String> row = cells.get(rowIndex++);
        final List<Run> runs = benchmark.getValue();

        final String name = benchmark.getKey();
        final String shortName = name.substring(name.indexOf('#') + 1);
        final Pattern pattern = Pattern.compile(shortName + "\\d*");
        final long count = cells.stream().map(r -> r.get(0)).filter(Objects::nonNull).map(pattern::matcher)
            .filter(Matcher::matches).count();
        final String label = count <= 0 ? shortName : shortName + Long.toString(count + 1);
        final boolean hasCodeHints = runs.stream().anyMatch(run -> run.FoundCodeHints);
        final String annotatedLabel = hasCodeHints ? label + '*' : label;
        if (hasCodeHints)
          row.set(0, label + '*');
        else if (row.get(0) == null)
          row.set(0, label);
        benchmarkNameToFoundCodeHints.merge(annotatedLabel, hasCodeHints, (lhs, rhs) -> lhs || rhs);

        final boolean isUnsound = runs.stream().anyMatch(run -> run.Unsound);
        if (isUnsound) {
          row.set(columnIndex, UNSOUND_MARKER);
          continue;
        }

        final boolean hasNoSolution = runs.stream().anyMatch(run -> run.Solution == null);
        if (hasNoSolution) {
          row.set(columnIndex, MISSED_MARKER);
          continue;
        }

        final double averageRuntimeInMilliseconds = runs.stream()
            .collect(Collectors.averagingLong(run -> run.RuntimeInMilliseconds));
        row.set(columnIndex, Long.toString(Math.round(averageRuntimeInMilliseconds)));
      }
    }

    final int averageRow = numberOfRows - NUMBER_OF_SUMMARY_COLUMNS;
    final int soundRow = averageRow + 1;
    final int unsoundRow = soundRow + 1;
    final int missedRow = unsoundRow + 1;
    final int averageRowChOnly = missedRow + 1;
    final int soundRowChOnly = averageRowChOnly + 1;
    final int unsoundRowChOnly = soundRowChOnly + 1;
    final int missedRowChOnly = unsoundRowChOnly + 1;
    final int successRateRow = missedRowChOnly + 1;
    final int successRateRowChOnly = successRateRow + 1;

    cells.get(averageRow).set(0, "Average");
    cells.get(soundRow).set(0, "Sound");
    cells.get(unsoundRow).set(0, "Unsound");
    cells.get(missedRow).set(0, "Missed");
    cells.get(averageRowChOnly).set(0, "Average CH");
    cells.get(soundRowChOnly).set(0, "Sound CH");
    cells.get(unsoundRowChOnly).set(0, "Unsound CH");
    cells.get(missedRowChOnly).set(0, "Missed CH");
    cells.get(successRateRow).set(0, "Success Rate");
    cells.get(successRateRowChOnly).set(0, "Success Rate CH");

    for (int column = 1; column < numberOfConfigurations + 1; ++column) {
      final long average = Math.round(getRow(cells, column)
          .filter(NumberUtils::isParsable).collect(Collectors.averagingLong(Long::parseLong)));
      final long sound = getRow(cells, column).filter(NumberUtils::isParsable).count();
      final long unsound = getRow(cells, column).filter(UNSOUND_MARKER::equals).count();
      final long missed = getRow(cells, column).filter(MISSED_MARKER::equals).count();
      final long averageChOnly = Math.round(getChOnlyRow(benchmarkNameToFoundCodeHints, cells, column)
          .filter(NumberUtils::isParsable).collect(Collectors.averagingLong(Long::parseLong)));
      final long soundChOnly = getChOnlyRow(benchmarkNameToFoundCodeHints, cells, column)
          .filter(NumberUtils::isParsable).count();
      final long unsoundChOnly = getChOnlyRow(benchmarkNameToFoundCodeHints, cells, column)
          .filter(UNSOUND_MARKER::equals).count();
      final long missedChOnly = getChOnlyRow(benchmarkNameToFoundCodeHints, cells, column).filter(MISSED_MARKER::equals)
          .count();
      final double successRate = Math.round((double) sound / (sound + unsound + missed) * 100.0) / 100.0;
      final double successRateChOnly = Math
          .round((double) soundChOnly / (soundChOnly + unsoundChOnly + missedChOnly) * 100.0) / 100.0;

      cells.get(averageRow).set(column, Long.toString(average));
      cells.get(soundRow).set(column, Long.toString(sound));
      cells.get(unsoundRow).set(column, Long.toString(unsound));
      cells.get(missedRow).set(column, Long.toString(missed));
      cells.get(averageRowChOnly).set(column, Long.toString(averageChOnly));
      cells.get(soundRowChOnly).set(column, Long.toString(soundChOnly));
      cells.get(unsoundRowChOnly).set(column, Long.toString(unsoundChOnly));
      cells.get(missedRowChOnly).set(column, Long.toString(missedChOnly));
      cells.get(successRateRow).set(column, Double.toString(successRate));
      cells.get(successRateRowChOnly).set(column, Double.toString(successRateChOnly));
    }

    return cells;
  }

  private static Stream<String> getRow(final List<List<String>> cells, final int column) {
    return cells.stream().skip(1).map(row -> row.get(column));
  }

  private static Stream<String> getChOnlyRow(final Map<String, Boolean> benchmarkNameToFoundCodeHints,
      final List<List<String>> cells, final int column) {
    return cells.stream().skip(1).filter(row -> Boolean.TRUE.equals(benchmarkNameToFoundCodeHints.get(row.get(0))))
        .map(row -> row.get(column));
  }

  private static List<Report> getReports(final Path[] jsonFiles) throws IOException {
    final List<Report> reports = new ArrayList<>();
    final Set<String> benchmarks = new HashSet<>();
    final ObjectMapper objectMapper = new ObjectMapper();
    for (final Path jsonFile : jsonFiles) {
      final Report report;
      try (final InputStream is = Files.newInputStream(jsonFile)) {
        report = objectMapper.readValue(is, Report.class);
      }
      reports.add(report);

      for (final Map.Entry<String, List<Run>> benchmark : report.Benchmarks.entrySet())
        benchmarks.add(benchmark.getKey());
    }

    final List<Run> missingPlaceholder = new ArrayList<>();
    final Run placeholderRun = new Run();
    placeholderRun.Rounds = -1;
    missingPlaceholder.add(placeholderRun);
    for (final Report report : reports) {
      final Set<String> missingBenchmarks = new HashSet<>(benchmarks);
      missingBenchmarks.removeAll(report.Benchmarks.keySet());
      for (final String missingBenchmark : missingBenchmarks)
        report.Benchmarks.put(missingBenchmark, missingPlaceholder);
    }
    return reports;
  }

  private static Report getHypotheticalFallbackReport(final List<Report> reports) {
    final Report result = new Report();
    for (final Report report : reports) {
      for (final Map.Entry<String, List<Run>> runEntry : report.Benchmarks.entrySet()) {
        final String benchmarkName = runEntry.getKey();
        final List<Run> runs = runEntry.getValue();
        if (runs.size() != 1)
          throw new IllegalStateException("Expected single run.");

        final Run run = runs.get(0);
        List<Run> summaryRuns = result.Benchmarks.get(benchmarkName);
        if (summaryRuns == null) {
          summaryRuns = new ArrayList<>();
          summaryRuns.add(run);
          result.Benchmarks.put(benchmarkName, summaryRuns);
        } else {
          if (run.Solution != null) {
            summaryRuns.clear();
            summaryRuns.add(run);
          }
        }
      }
    }
    return result;
  }
}
