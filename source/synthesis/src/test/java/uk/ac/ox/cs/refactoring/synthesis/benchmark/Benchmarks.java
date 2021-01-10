package uk.ac.ox.cs.refactoring.synthesis.benchmark;

import uk.ac.ox.cs.refactoring.classloader.JavaLanguage;

public final class Benchmarks {
  private Benchmarks() {
  }

  public static final String EMPTY = getFullyQualifiedBenchmarkName("Empty");

  public static final String INTEGER_ALIASING = getFullyQualifiedBenchmarkName("IntegerAliasing");

  public static final String INTEGER_WRAPPER = getFullyQualifiedBenchmarkName("IntegerWrapper");

  public static final String OBJECT_ALIASING = getFullyQualifiedBenchmarkName("ObjectAliasing");

  public static final String STATIC_INT_CONSUMER = getFullyQualifiedBenchmarkName("StaticIntConsumer");

  public static final String STATIC_INTEGER_CONSUMER = getFullyQualifiedBenchmarkName("StaticIntegerConsumer");

  public static final String SUM = getFullyQualifiedBenchmarkName("Sum");

  private static String getFullyQualifiedBenchmarkName(final String name) {
    return Benchmarks.class.getPackageName() + JavaLanguage.PACKAGE_SEPARATOR + name;
  }
}
