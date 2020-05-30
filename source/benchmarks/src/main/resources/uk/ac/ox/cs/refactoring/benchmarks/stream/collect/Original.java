package uk.ac.ox.cs.refactoring.benchmarks.stream.simplefilter;

import java.util.ArrayList;
import java.util.Collection;

public class Original {

  public static Collection<String> method(final Collection<String> collection) {
    final Collection<String> collected = new ArrayList<>();
    for(final String item : collection)
      collected.add(item);

    return collected;
  }
}
