package uk.ac.ox.cs.refactoring.instrument.benchmark.javafeature;

public class Locals {
  public int foo() {
    int x = 7;
    int y = 8;
    int z = x + y;
    return z;
  }
}
