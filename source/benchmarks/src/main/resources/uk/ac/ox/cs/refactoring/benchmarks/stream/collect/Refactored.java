package uk.ac.ox.cs.refactoring.benchmarks.stream.simplefilter;

import java.util.Collection;
import java.util.stream.Collectors;

public class Refactored {

  public static Collection<String> method(final Collection<String> collection) {
    final Collection<String> collected = collection.stream()
        .collect(Collectors.toList());

    return collected;
  }
}
