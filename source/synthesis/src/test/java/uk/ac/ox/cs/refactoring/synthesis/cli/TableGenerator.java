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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang3.math.NumberUtils;

import uk.ac.ox.cs.refactoring.synthesis.statistics.Report;
import uk.ac.ox.cs.refactoring.synthesis.statistics.Run;

public class TableGenerator {

  private static final int NUMBER_OF_SUMMARY_COLUMNS = 4;

  public static void main(final String[] args) throws IOException {
    final String[] files = Arrays.stream(args).map(Paths::get).filter(Files::exists).map(Path::toString)
        .toArray(String[]::new);
    final int numberOfConfigurations = files.length;
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

  private static List<List<String>> getCells(final String[] jsonFiles) throws IOException {
    if (jsonFiles.length == 0)
      return Collections.emptyList();

    final ObjectMapper objectMapper = new ObjectMapper();
    final List<List<String>> cells = new ArrayList<>();
    final int numberOfConfigurations = jsonFiles.length;
    final int numberOfColumns = numberOfConfigurations + 1;
    int numberOfRows = -1;
    final Map<String, Report> reports = new HashMap<>();
    for (int column = 0; column < numberOfConfigurations; ++column) {
      final Path jsonFile = Paths.get(jsonFiles[column]);
      final Report report;
      try (final InputStream is = Files.newInputStream(jsonFile)) {
        report = objectMapper.readValue(is, Report.class);
      }
      reports.put(jsonFiles[column], report);

      final int numberOfBenchmarks = report.Benchmarks.size();
      numberOfRows = numberOfBenchmarks + NUMBER_OF_SUMMARY_COLUMNS + 1;
      if (cells.isEmpty()) {
        for (int i = 0; i < numberOfRows; ++i)
          cells.add(new ArrayList<>(Collections.nCopies(numberOfColumns, null)));

        cells.get(0).set(0, "Benchmark");
      }

      final int columnIndex = column + 1;
      cells.get(0).set(columnIndex,
          jsonFile.getFileName().toString().replace(".json", "").replace("javadoc", "jd").replace("random", "rand"));
      int rowIndex = 1;
      for (final Map.Entry<String, List<Run>> benchmark : report.Benchmarks.entrySet()) {
        final List<String> row = cells.get(rowIndex++);

        if (row.get(0) == null) {
          final String name = benchmark.getKey();
          final String shortName = name.substring(name.indexOf('#') + 1);
          final Pattern pattern = Pattern.compile(shortName + "\\d*");
          final long count = cells.stream().map(r -> r.get(0)).filter(Objects::nonNull).map(pattern::matcher)
              .filter(Matcher::matches).count();
          final String label = count <= 0 ? shortName : shortName + Long.toString(count + 1);
          row.set(0, label);
        }

        final List<Run> runs = benchmark.getValue();
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

    cells.get(averageRow).set(0, "Average");
    cells.get(soundRow).set(0, "Sound");
    cells.get(unsoundRow).set(0, "Unsound");
    cells.get(missedRow).set(0, "Missed");

    for (int column = 1; column < numberOfConfigurations + 1; ++column) {
      final long average = Math.round(getRow(cells, column)
          .filter(NumberUtils::isParsable).collect(Collectors.averagingLong(Long::parseLong)));
      final long sound = getRow(cells, column).filter(NumberUtils::isParsable).count();
      final long unsound = getRow(cells, column).filter(UNSOUND_MARKER::equals).count();
      final long missed = getRow(cells, column).filter(MISSED_MARKER::equals).count();

      cells.get(averageRow).set(column, Long.toString(average));
      cells.get(soundRow).set(column, Long.toString(sound));
      cells.get(unsoundRow).set(column, Long.toString(unsound));
      cells.get(missedRow).set(column, Long.toString(missed));
    }

    return cells;
  }

  private static Stream<String> getRow(final List<List<String>> cells, final int column) {
    return cells.stream().skip(1).map(row -> row.get(column));
  }
}
