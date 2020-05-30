package uk.ac.ox.cs.refactoring.benchmarks.stream.simplefilter;

import java.util.ArrayList;
import java.util.Collection;

public class Original {

  public static String method(final Collection<String> collection) {
    String result = "";
    for(final String item : collection)
      result = result.concat(item);

    return result;
  }
}
