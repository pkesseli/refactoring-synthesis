package uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.BinaryExpr.Operator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.Type;

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

  /**
   * Base for binary double expressions.
   */
  private static abstract class BinaryExpression implements IExpression {

    /**
     * Left-hand-side.
     */
    private final IExpression lhs;

    /**
     * Double {@link Operator}.
     */
    private final Operator operator;

    /**
     * Right-hand-side.
     */
    private final IExpression rhs;

    /**
     * @param lhs      {@link #lhs}
     * @param operator {@link #operator}
     * @param rhs      {@link #rhs}
     */
    public BinaryExpression(final IExpression lhs, final Operator operator, final IExpression rhs) {
      this.lhs = lhs;
      this.operator = operator;
      this.rhs = rhs;
    }

    /**
     * @param context {@link IExpression#evaluate(ExecutionContext)}
     * @return Left-hand-side double value.
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     */
    public double getLhs(final ExecutionContext context) throws ClassNotFoundException, IllegalAccessException,
        InvocationTargetException, NoSuchFieldException, NoSuchMethodException {
      return (double) lhs.evaluate(context);
    }

    /**
     * @param context {@link IExpression#evaluate(ExecutionContext)}
     * @return Right-hand-side double value.
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     */
    public double getRhs(final ExecutionContext context) throws ClassNotFoundException, IllegalAccessException,
        InvocationTargetException, NoSuchFieldException, NoSuchMethodException {
      return (double) rhs.evaluate(context);
    }

    @Override
    public Type getType() {
      return PrimitiveType.doubleType();
    }

    @Override
    public Expression toNode() {
      return new BinaryExpr(lhs.toNode(), rhs.toNode(), operator);
    }
  }

  static class Plus extends BinaryExpression {
    public Plus(final IExpression lhs, final IExpression rhs) {
      super(lhs, Operator.PLUS, rhs);
    }

    @Override
    public java.lang.Double evaluate(final ExecutionContext context) throws ClassNotFoundException,
        IllegalAccessException, InvocationTargetException, NoSuchFieldException, NoSuchMethodException {
      return getLhs(context) + getRhs(context);
    }
  }

  static class Minus extends BinaryExpression {
    public Minus(final IExpression lhs, final IExpression rhs) {
      super(lhs, Operator.MINUS, rhs);
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
