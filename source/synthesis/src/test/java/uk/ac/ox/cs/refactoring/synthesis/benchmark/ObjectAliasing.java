package uk.ac.ox.cs.refactoring.synthesis.benchmark;

import java.util.function.BiConsumer;

public class ObjectAliasing implements BiConsumer<Object, Object> {
  static Object first;
  static Object second;

  @Override
  public void accept(Object t, Object u) {
    first = t;
    second = u;
  }
}
