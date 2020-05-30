package uk.ac.ox.cs.refactoring.benchmarks.stream.simplefilter;

import java.util.Collection;
import java.util.stream.Collectors;

public class Refactored {

  public static String method(final Collection<String> collection) {
    final String result = collection.stream().reduce(String::concat).orElse("");

    return result;
  }
}
