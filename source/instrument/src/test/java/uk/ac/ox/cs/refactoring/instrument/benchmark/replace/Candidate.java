package uk.ac.ox.cs.refactoring.instrument.benchmark.replace;

public final class Candidate {
  private Candidate() {
  }

  public static int foo(final Original original, final long x, final String z) {
    throw new UnsupportedOperationException();
  }
}
