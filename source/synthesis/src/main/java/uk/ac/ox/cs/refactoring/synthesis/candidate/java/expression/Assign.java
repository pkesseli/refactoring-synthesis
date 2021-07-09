package uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.AssignExpr.Operator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.type.Type;

import uk.ac.ox.cs.refactoring.synthesis.candidate.api.ExecutionContext;
import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.ConstructorComponent;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.IExpression;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.LeftHandSideExpression;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaComponents;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaLanguageKey;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaLanguageKeys;

/**
 * Contains Java assignment expressions for use in builders.
 */
public final class Assign {

  /**
   * Java value assignment expression.
   */
  public static class Value implements IExpression {

    /**
     * Left-hand-side of assignment.
     */
    private final LeftHandSideExpression lhs;

    /**
     * Right-hand-side of assignment.
     */
    private final IExpression rhs;

    /**
     * @param lhs {@link #lhs}
     * @param rhs {@link #rhs}
     */
    public Value(final LeftHandSideExpression lhs, final IExpression rhs) {
      this.lhs = lhs;
      this.rhs = rhs;
    }

    @Override
    public Expression toNode() {
      return new AssignExpr(lhs.toNode(), rhs.toNode(), Operator.ASSIGN);
    }

    @Override
    public Type getType() {
      return lhs.getType();
    }

    @Override
    public Object evaluate(final ExecutionContext context) throws ClassNotFoundException, IllegalAccessException,
        InstantiationException, InvocationTargetException, NoSuchFieldException, NoSuchMethodException {
      lhs.set(context, rhs.evaluate(context));
      return lhs.evaluate(context);
    }
  }

  /**
   * Helper to register assignment expressions in candidate builders.
   * 
   * @param components Directory in which to register.
   * @param type       {@link Type} of assignments to register.
   */
  public static void register(final JavaComponents components, final Type type) {
    final JavaLanguageKey lhsKey = JavaLanguageKeys.lhs(type);
    final JavaLanguageKey rhsKey = JavaLanguageKeys.expression(type);
    final List<JavaLanguageKey> parameterKeys = Arrays.asList(lhsKey, rhsKey);
    components.nonnull(type, new ConstructorComponent<>(parameterKeys, Value.class));
  }
}
