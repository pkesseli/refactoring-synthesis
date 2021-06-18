package uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder;

import com.github.javaparser.ast.type.Type;

import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.Component;
import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.ComponentDirectory;
import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.NullaryComponent;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.Literal;

/**
 * 
 */
public class JavaComponents {

  /**
   * 
   */
  private final ComponentDirectory components;

  /**
   * 
   * @param components
   */
  public JavaComponents(final ComponentDirectory components) {
    this.components = components;
  }

  /**
   * 
   * @param type
   * @param component
   */
  public void statement(final Type type, final Component<JavaLanguageKey, ?> component) {
    components.put(JavaLanguageKeys.statement(type), component);
  }

  /**
   * 
   * @param type
   * @param component
   */
  public void lhs(final Type type, final Component<JavaLanguageKey, ?> component) {
    components.put(JavaLanguageKeys.lhs(type), component);
    nonnull(type, component);
  }

  /**
   * 
   * @param type
   * @param component
   */
  public void expr(final Type type, final Component<JavaLanguageKey, ?> component) {
    components.put(JavaLanguageKeys.expression(type), component);
  }

  /**
   * 
   * @param type
   * @param component
   */
  public void nonnull(final Type type, final Component<JavaLanguageKey, ?> component) {
    expr(type, component);
    components.put(JavaLanguageKeys.nonnull(type), component);
  }

  /**
   * 
   * @param type
   * @param value
   */
  public void literal(final Type type, final Object value) {
    nonnull(type, new NullaryComponent<>(new Literal(value, type)));
  }

  /**
   * 
   * @param type
   */
  public void nullLiteral(final Type type) {
    expr(type, new NullaryComponent<>(new Literal(null, type)));
  }
}
