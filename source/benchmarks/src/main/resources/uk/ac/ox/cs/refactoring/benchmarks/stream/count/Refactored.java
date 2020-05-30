package uk.ac.ox.cs.refactoring.benchmarks.stream.simplefilter;

import java.util.Collection;
import java.util.stream.Collectors;

public class Refactored {

  public static int method(final Collection<String> collection) {
    final int sum = collection.stream().count();

    return sum;
  }
}
