package uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder;

import java.util.Objects;

import com.github.javaparser.ast.type.Type;

/**
 * Key identifying a category of Java expressions when used in a candidate
 * builder.
 */
public class JavaLanguageKey {
  /**
   * Kind of the language element (e.g. expression or statement).
   */
  private Class<?> kind;

  /**
   * Type of the Java language element if applicable.
   */
  private Type type;

  /**
   * @param kind {@link #kind}
   * @param type {@link #type}
   */
  public JavaLanguageKey(final Class<?> kind, final Type type) {
    this.kind = kind;
    this.type = type;
  }

  @Override
  public int hashCode() {
    return Objects.hash(kind, type);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    JavaLanguageKey other = (JavaLanguageKey) obj;
    return Objects.equals(kind, other.kind) && Objects.equals(type, other.type);
  }
}
