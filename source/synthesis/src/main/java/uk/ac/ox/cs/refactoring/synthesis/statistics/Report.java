package uk.ac.ox.cs.refactoring.synthesis.statistics;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Report {

  public long TotalRuntimeInMilliseconds;

  public FuzzedInputs TotalCounterexamples = new FuzzedInputs();

  public FuzzedInputs TotalCandidates = new FuzzedInputs();

  public Map<String, List<Run>> Benchmarks = new TreeMap<>();
}
