package uk.ac.ox.cs.refactoring.synthesis.benchmark;

import java.util.function.Supplier;

public class IntegerWrapper implements Supplier<Integer> {
  private Integer value;

  @Override
  public Integer get() {
    return value;
  }
}
