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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang3.math.NumberUtils;

import uk.ac.ox.cs.refactoring.synthesis.statistics.Report;
import uk.ac.ox.cs.refactoring.synthesis.statistics.Run;

public class TableGenerator {

  public static void main(final String[] args) throws IOException {
    final String[] files = Arrays.stream(args).map(Paths::get).filter(Files::exists).map(Path::toString)
        .toArray(String[]::new);
    final int numberOfConfigurations = files.length;
    final List<List<String>> cells = getCells(files);
    try (final OutputStream os = Files.newOutputStream(Paths.get("table.tex"));
        final OutputStream buffer = new BufferedOutputStream(os);
        final PrintWriter writer = new PrintWriter(buffer)) {

      writer.println("\\documentclass{article}");
      writer.println("\\usepackage{wasysym}");
      writer.println("\\usepackage{pifont}");
      writer.println("\\newcommand{\\xmark}{\\ding{55}}");
      writer.println("\\begin{document}");

      writer.print("\\begin{tabular}{ l ");
      for (int i = 0; i < numberOfConfigurations; ++i)
        writer.print("r ");
      writer.println('}');

      for (int rowIndex = 0; rowIndex < cells.size(); ++rowIndex) {
        final List<String> row = cells.get(rowIndex);
        writer.print(String.join(" & ", row));
        writer.println(" \\\\");

        if (rowIndex == 0)
          writer.println("\\hline");
        if (rowIndex == cells.size() - 2) {
          writer.println("\\hline");
          writer.println("\\hline");
        }
      }
      writer.println("\\end{tabular}");
      writer.println("\\end{document}");
    }
  }

  private static List<List<String>> getCells(final String[] jsonFiles) throws IOException {
    if (jsonFiles.length == 0)
      return Collections.emptyList();

    final ObjectMapper objectMapper = new ObjectMapper();
    final List<List<String>> cells = new ArrayList<>();
    final int numberOfConfigurations = jsonFiles.length;
    final int numberOfColumns = numberOfConfigurations + 1;
    int numberOfRows = -1;
    for (int column = 0; column < numberOfConfigurations; ++column) {
      final Path jsonFile = Paths.get(jsonFiles[column]);
      final Report report;
      try (final InputStream is = Files.newInputStream(jsonFile)) {
        report = objectMapper.readValue(is, Report.class);
      }

      final int numberOfBenchmarks = report.Benchmarks.size();
      numberOfRows = numberOfBenchmarks + 2;
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
          row.set(columnIndex, "\\multicolumn{1}{c}{\\lightning}");
          continue;
        }

        final boolean hasNoSolution = runs.stream().anyMatch(run -> run.Solution == null);
        if (hasNoSolution) {
          row.set(columnIndex, "\\multicolumn{1}{c}{\\xmark}");
          continue;
        }

        final double averageRuntimeInMilliseconds = runs.stream()
            .collect(Collectors.averagingLong(run -> run.RuntimeInMilliseconds));
        row.set(columnIndex, Long.toString(Math.round(averageRuntimeInMilliseconds)));
      }
    }

    cells.get(numberOfRows - 1).set(0, "Average");
    for (int column = 1; column < numberOfConfigurations + 1; ++column) {
      final int columnIndex = column;
      final long average = Math.round(cells.stream().skip(1).map(row -> row.get(columnIndex))
          .filter(NumberUtils::isParsable).collect(Collectors.averagingLong(Long::parseLong)));
      cells.get(numberOfRows - 1).set(column, Long.toString(average));
    }

    return cells;
  }
}
