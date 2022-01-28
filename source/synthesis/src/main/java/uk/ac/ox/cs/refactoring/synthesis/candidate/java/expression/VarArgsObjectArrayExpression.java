package uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.type.ArrayType;
import com.github.javaparser.ast.type.Type;

import uk.ac.ox.cs.refactoring.synthesis.candidate.api.ExecutionContext;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.IExpression;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.type.TypeFactory;

/**
 * Evaluates to the correct arguments to pass to a variadic method when
 * evaluating, while at the same visualising the arguments without a wrapping
 * array when printing.
 */
public class VarArgsObjectArrayExpression implements IExpression {

  /** Component type of the implicit varargs array. */
  private final Class<?> componentType;

  /** Elements of the varargs array. */
  final List<IExpression> arguments;

  /**
   * @param componentType {@link #componentType}
   * @param arguments     {@link #arguments}
   */
  public VarArgsObjectArrayExpression(final Class<?> componentType, final List<IExpression> arguments) {
    this.componentType = componentType;
    this.arguments = arguments;
  }

  @Override
  public Expression toNode() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Type getType() {
    return new ArrayType(TypeFactory.createClassType(Object.class));
  }

  @Override
  public Object evaluate(final ExecutionContext context) throws ClassNotFoundException, IllegalAccessException,
      InstantiationException, InvocationTargetException, NoSuchFieldException, NoSuchMethodException {
    final int size = arguments.size();
    final Object result = Array.newInstance(componentType, size);
    for (int i = 0; i < size; ++i) {
      Array.set(result, i, arguments.get(i).evaluate(context));
    }
    return result;
  }
}
