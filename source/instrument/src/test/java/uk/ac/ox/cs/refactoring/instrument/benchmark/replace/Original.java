package uk.ac.ox.cs.refactoring.instrument.benchmark.replace;

public class Original {
  @Deprecated
  public int foo(long x, String z) {
    final int i = Integer.valueOf(z);
    final int j = (int) x;
    return i + j;
  }
}
