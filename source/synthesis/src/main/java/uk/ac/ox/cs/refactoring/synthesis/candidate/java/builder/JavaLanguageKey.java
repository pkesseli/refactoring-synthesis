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
  public final Class<?> Kind;

  /**
   * Type of the Java language element if applicable.
   */
  public final Type Type;

  /**
   * Indicate that the component type should be {@code @NonNull}.
   */
  public final boolean Nonnull;

  /**
   * @param kind    {@link #Kind}
   * @param type    {@link #Type}
   * @param nonnull {@link #Nonnull}
   */
  JavaLanguageKey(final Class<?> kind, final Type type, boolean nonnull) {
    this.Kind = kind;
    this.Type = type;
    this.Nonnull = nonnull;
  }

  @Override
  public int hashCode() {
    return Objects.hash(Kind, Type, Nonnull);
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
    return Objects.equals(Kind, other.Kind) && Objects.equals(Type, other.Type) && Nonnull == other.Nonnull;
  }
}
