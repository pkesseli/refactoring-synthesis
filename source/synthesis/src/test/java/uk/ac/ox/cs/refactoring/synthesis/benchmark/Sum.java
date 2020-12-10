package uk.ac.ox.cs.refactoring.synthesis.benchmark;

public class Sum {
  private IntegerWrapper base;

  public int max(IntegerWrapper lhs, IntegerWrapper rhs) {
    return base.get() + lhs.get() + rhs.get();
  }
}
