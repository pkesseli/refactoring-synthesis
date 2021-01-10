package uk.ac.ox.cs.refactoring.synthesis.benchmark;

import java.util.function.BiConsumer;

public class IntegerAliasing implements BiConsumer<Integer, Integer> {
  static Integer first;
  static Integer second;

  @Override
  public void accept(Integer t, Integer u) {
    first = t;
    second = u;
  }
}
