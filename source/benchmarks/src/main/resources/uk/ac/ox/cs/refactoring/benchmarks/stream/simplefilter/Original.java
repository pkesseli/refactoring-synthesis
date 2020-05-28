package uk.ac.ox.cs.refactoring.benchmarks.stream.simplefilter;

import java.util.ArrayList;
import java.util.Collection;

public class Original {

  public static Collection<String> filter(final Collection<String> collection) {
    final Collection<String> filtered = new ArrayList<>();
    for(final String item : collection)
      if (!item.isEmpty())
        filtered.add(item);

    return filtered;
  }
}
