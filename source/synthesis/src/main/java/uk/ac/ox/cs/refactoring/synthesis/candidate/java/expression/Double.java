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
 * Contains double type expression for use in a Java candidate builder.
 */
public final class Double {
  private Double() {
  }

  static class Plus extends BinaryExpression<java.lang.Double> {
    public Plus(final IExpression lhs, final IExpression rhs) {
      super(lhs, Operator.PLUS, rhs, PrimitiveType.doubleType());
    }

    @Override
    public java.lang.Double evaluate(final ExecutionContext context) throws ClassNotFoundException,
        IllegalAccessException, InvocationTargetException, NoSuchFieldException, NoSuchMethodException {
      return getLhs(context) + getRhs(context);
    }
  }

  static class Minus extends BinaryExpression<java.lang.Double> {
    public Minus(final IExpression lhs, final IExpression rhs) {
      super(lhs, Operator.MINUS, rhs, PrimitiveType.doubleType());
    }

    @Override
    public java.lang.Double evaluate(final ExecutionContext context) throws ClassNotFoundException,
        IllegalAccessException, InvocationTargetException, NoSuchFieldException, NoSuchMethodException {
      return getLhs(context) - getRhs(context);
    }
  }

  /**
   * Helper to register double expressions in Java candidate builders.
   * 
   * @param components Directory in which to register all double expressions.
   */
  public static void register(final ComponentDirectory components) {
    final JavaLanguageKey doubleExpression = new JavaLanguageKey(IExpression.class, PrimitiveType.doubleType());
    final List<JavaLanguageKey> parameterKeys = Arrays.asList(doubleExpression, doubleExpression);
    final Iterable<Class<?>> classes = Classes.getNonAbstractMemberClasses(Double.class)::iterator;
    for (final Class<?> cls : classes) {
      components.put(doubleExpression, new ConstructorComponent<>(parameterKeys, cls));
    }
  }
}
