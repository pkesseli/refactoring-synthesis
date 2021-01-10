package uk.ac.ox.cs.refactoring.synthesis.benchmark;

import java.util.function.IntConsumer;

public class StaticIntConsumer implements IntConsumer {
  static int value;

  @Override
  public void accept(int t) {
    value = t;
  }
}
