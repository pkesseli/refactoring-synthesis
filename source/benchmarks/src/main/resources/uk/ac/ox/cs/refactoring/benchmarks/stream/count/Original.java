package uk.ac.ox.cs.refactoring.benchmarks.stream.simplefilter;

import java.util.ArrayList;
import java.util.Collection;

public class Original {

  public static int count(final Collection<String> collection) {
    int sum = 0;
    for(final String item : collection)
      ++sum;

    return sum;
  }
}
