package uk.ac.ox.cs.refactoring.synthesis.benchmark;

import java.util.function.Consumer;

public class StaticIntegerConsumer implements Consumer<Integer> {
  static Integer value;

  @Override
  public void accept(Integer t) {
    value = t;
  }
}
