package uk.ac.ox.cs.refactoring.synthesis.cli;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import uk.ac.ox.cs.refactoring.synthesis.statistics.Report;
import uk.ac.ox.cs.refactoring.synthesis.statistics.Run;

public class JsonMerger {
  public static void main(final String[] args) throws IOException {
    final Map<Path, Path> jsonPairs = new HashMap<>();
    jsonPairs.put(Paths.get("experiments/15.03.2022-merged-3/random.json"),
        Paths.get("experiments/17.03.2022-code-hints-info/random-javadoc.json"));
    jsonPairs.put(Paths.get("experiments/15.03.2022-merged-3/random-javadoc.json"),
        Paths.get("experiments/17.03.2022-code-hints-info/random-javadoc.json"));
    jsonPairs.put(Paths.get("experiments/15.03.2022-merged-3/zest.json"),
        Paths.get("experiments/17.03.2022-code-hints-info/zest-javadoc.json"));
    jsonPairs.put(Paths.get("experiments/15.03.2022-merged-3/zest-javadoc.json"),
        Paths.get("experiments/17.03.2022-code-hints-info/zest-javadoc.json"));

    final Map<String, String> nameMismatches = new HashMap<>();
    nameMismatches.put("java.awt.Component#enableBoolean", "java.awt.Component#enable");
    nameMismatches.put("java.awt.Component#resizeDimension", "java.awt.Component#resize");
    nameMismatches.put("java.awt.Component#showBoolean", "java.awt.Component#show");
    nameMismatches.put("java.awt.Frame#setCursor", "java.awt.Frame#setCursorType");
    nameMismatches.put("java.awt.MenuItem#enableBoolean", "java.awt.MenuItem#enable");

    final ObjectMapper objectMapper = new ObjectMapper();
    final ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
    for (final Map.Entry<Path, Path> jsonPair : jsonPairs.entrySet()) {
      final Report codeHintsReport;
      try (final InputStream is = Files.newInputStream(jsonPair.getValue())) {
        codeHintsReport = objectMapper.readValue(is, Report.class);
      }
      final Report dataReport;
      try (final InputStream is = Files.newInputStream(jsonPair.getKey())) {
        dataReport = objectMapper.readValue(is, Report.class);
      }

      codeHintsReport.TotalCandidates = dataReport.TotalCandidates;
      codeHintsReport.TotalCounterexamples = dataReport.TotalCounterexamples;
      codeHintsReport.TotalRuntimeInMilliseconds = dataReport.TotalRuntimeInMilliseconds;
      for (final Map.Entry<String, List<Run>> runs : codeHintsReport.Benchmarks.entrySet()) {
        List<Run> dataRuns = dataReport.Benchmarks.get(runs.getKey());
        int dataIndex = 0;
        if (dataRuns == null) {
          final String oldName = nameMismatches.get(runs.getKey());
          dataRuns = dataReport.Benchmarks.get(oldName);
          if (!"java.awt.Frame#setCursorType".equals(oldName))
            dataIndex = 1;
        }

        final List<Run> codeHintsRuns = runs.getValue();
        final Run codeHintsRun = codeHintsRuns.get(0);
        final boolean hasCodeHints = codeHintsRun.FoundCodeHints;
        codeHintsRuns.clear();
        final Run dataRun = dataRuns.get(dataIndex);
        dataRun.FoundCodeHints = hasCodeHints;
        codeHintsRuns.add(dataRun);
      }

      try (final OutputStream os = Files.newOutputStream(jsonPair.getKey().getFileName())) {
        objectWriter.writeValue(os, codeHintsReport);
      }
    }
  }
}
