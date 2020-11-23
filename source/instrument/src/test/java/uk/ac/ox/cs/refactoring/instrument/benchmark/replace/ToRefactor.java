package uk.ac.ox.cs.refactoring.instrument.benchmark.replace;

public class ToRefactor {
  public static void foo() {
    Original original = new Original();
    @SuppressWarnings("deprecation")
    int output = original.foo(10, "20");
    System.out.println(output);
  }
}
