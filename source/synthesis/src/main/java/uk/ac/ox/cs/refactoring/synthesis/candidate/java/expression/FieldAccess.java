package uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression;

import java.lang.reflect.Field;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.type.Type;

import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.LeftHandSideExpression;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.type.TypeFactory;

/**
 * 
 */
public class FieldAccess implements LeftHandSideExpression {

  /**
   * 
   */
  private final Field field;

  /**
   * 
   * @param field
   */
  public FieldAccess(final Field field) {
    this.field = field;
  }

  @Override
  public Type getType() {
    return TypeFactory.create(field.getType());
  }

  @Override
  public Object evaluate() {
    try {
      return field.get(null);
    } catch (final IllegalArgumentException | IllegalAccessException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public Expression toNode() {
    final String className = field.getDeclaringClass().getName();
    return new FieldAccessExpr(new NameExpr(className), field.getName());
  }

  @Override
  public void set(final Object value) {
    try {
      field.set(null, value);
    } catch (final IllegalArgumentException | IllegalAccessException e) {
      throw new IllegalStateException(e);
    }
  }
}
