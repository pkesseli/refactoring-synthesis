package uk.ac.ox.cs.refactoring.classloader;

/**
 * Common constants relevant for Java language analysis.
 */
public final class JavaLanguage {
  private JavaLanguage() {
  }

  /**
   * Separator character for packages and classes in Java.
   */
  public static final char PACKAGE_SEPARATOR = '.';

  /**
   * Separator character for resource path components in Java.
   */
  public static final char RESOURCE_SEPARATOR = '/';
}
