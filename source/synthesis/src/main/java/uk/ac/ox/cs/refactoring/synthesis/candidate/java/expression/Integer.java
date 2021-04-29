package uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import com.github.javaparser.ast.expr.BinaryExpr.Operator;
import com.github.javaparser.ast.type.PrimitiveType;

import uk.ac.ox.cs.refactoring.synthesis.candidate.api.ExecutionContext;
import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.ComponentDirectory;
import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.ConstructorComponent;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.IExpression;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaLanguageKey;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.reflection.Classes;

/**
 * Contains int type expression for use in a Java candidate builder.
 */
public final class Integer {
  private Integer() {
  }

  public static class Plus extends BinaryExpression<java.lang.Integer> {
    public Plus(final IExpression lhs, final IExpression rhs) {
      super(lhs, Operator.PLUS, rhs, PrimitiveType.intType());
    }

    @Override
    public java.lang.Integer evaluate(final ExecutionContext context) throws ClassNotFoundException,
        IllegalAccessException, InvocationTargetException, NoSuchFieldException, NoSuchMethodException {
      return getLhs(context) + getRhs(context);
    }
  }

  static class Minus extends BinaryExpression<java.lang.Integer> {
    public Minus(final IExpression lhs, final IExpression rhs) {
      super(lhs, Operator.MINUS, rhs, PrimitiveType.intType());
    }

    @Override
    public java.lang.Integer evaluate(final ExecutionContext context) throws ClassNotFoundException,
        IllegalAccessException, InvocationTargetException, NoSuchFieldException, NoSuchMethodException {
      return getLhs(context) - getRhs(context);
    }
  }

  /**
   * Helper to register int expressions in Java candidate builders.
   * 
   * @param components Directory in which to register all int expressions.
   */
  public static void register(final ComponentDirectory components) {
    final JavaLanguageKey intExpression = new JavaLanguageKey(IExpression.class, PrimitiveType.intType());
    final List<JavaLanguageKey> parameterKeys = Arrays.asList(intExpression, intExpression);
    final Iterable<Class<?>> classes = Classes.getNonAbstractMemberClasses(Integer.class)::iterator;
    for (final Class<?> cls : classes) {
      components.put(intExpression, new ConstructorComponent<>(parameterKeys, cls));
    }
  }
}
