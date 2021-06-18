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
  private final Class<?> kind;

  /**
   * Type of the Java language element if applicable.
   */
  private final Type type;

  /**
   * Indicate that the component type should be {@code @NonNull}.
   */
  private final boolean nonnull;

  /**
   * @param kind    {@link #kind}
   * @param type    {@link #type}
   * @param nonnull {@link #nonnull}
   */
  JavaLanguageKey(final Class<?> kind, final Type type, boolean nonnull) {
    this.kind = kind;
    this.type = type;
    this.nonnull = nonnull;
  }

  @Override
  public int hashCode() {
    return Objects.hash(kind, type, nonnull);
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
    return Objects.equals(kind, other.kind) && Objects.equals(type, other.type) && nonnull == other.nonnull;
  }
}
