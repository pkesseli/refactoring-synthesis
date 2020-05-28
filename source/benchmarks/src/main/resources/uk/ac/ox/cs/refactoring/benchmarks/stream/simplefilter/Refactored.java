package uk.ac.ox.cs.refactoring.benchmarks.stream.simplefilter;

import java.util.Collection;
import java.util.stream.Collectors;

public class Refactored {

  public static Collection<String> filter(final Collection<String> collection) {
    final Collection<String> filtered = collection.stream()
        .filter(item -> !item.isEmpty()).collect(Collectors.toList());

    return filtered;
  }
}
