package uk.ac.ox.cs.refactoring.benchmarks.stream.simplefilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class Original {

  public static Collection<String> method(final Collection<String> collection) {
    return collection.stream().map(String::trim).map(String::length)
        .collect(Collectors.toList());
  }
}
