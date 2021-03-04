package uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression;

import java.lang.reflect.Field;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.type.Type;

import uk.ac.ox.cs.refactoring.synthesis.candidate.api.ExecutionContext;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.LeftHandSideExpression;

/**
 * Models a Java field access expression.
 */
public class FieldAccess implements LeftHandSideExpression {

  /**
   * 
   */
  private final String fieldName;

  /**
   * 
   */
  private final String fullyQualifiedClassName;

  /**
   * 
   */
  private final Type type;

  /**
   * @param fieldName               {@link #fieldName}
   * @param fullyQualifiedClassName {@link #fullyQualifiedClassName}
   * @param type                    {@link #type}
   */
  public FieldAccess(final String fieldName, final String fullyQualifiedClassName, final Type type) {
    this.fieldName = fieldName;
    this.fullyQualifiedClassName = fullyQualifiedClassName;
    this.type = type;
  }

  /**
   * 
   * @param context
   * @return
   * @throws ClassNotFoundException
   * @throws NoSuchFieldException
   */
  private Field getField(final ExecutionContext context) throws ClassNotFoundException, NoSuchFieldException {
    final Class<?> cls = context.ClassLoader.loadClass(fullyQualifiedClassName);
    final Field field = cls.getDeclaredField(fieldName);
    field.setAccessible(true);
    return field;
  }

  @Override
  public Type getType() {
    return type;
  }

  @Override
  public Object evaluate(final ExecutionContext context)
      throws ClassNotFoundException, IllegalAccessException, NoSuchFieldException {
    return getField(context).get(null);
  }

  @Override
  public Expression toNode() {
    return new FieldAccessExpr(new NameExpr(fullyQualifiedClassName), fieldName);
  }

  @Override
  public void set(final ExecutionContext context, final Object value)
      throws ClassNotFoundException, IllegalAccessException, NoSuchFieldException {
    getField(context).set(null, value);
  }
}
