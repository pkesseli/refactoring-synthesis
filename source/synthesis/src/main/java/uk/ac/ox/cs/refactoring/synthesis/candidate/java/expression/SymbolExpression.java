package uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression;

import java.util.function.Supplier;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.type.Type;

import uk.ac.ox.cs.refactoring.synthesis.candidate.api.ExecutionContext;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.IExpression;

/**
 * Intended to be used to refer to local variable symbols.
 */
public class SymbolExpression implements IExpression {

  /**
   * Name of the symbol to which we are referring.
   */
  private final String name;

  /**
   * Type of the symbol to which we are referring.
   */
  private final Type type;

  /**
   * Value of the symbol, expected to be evaluated lazily.
   */
  private final Supplier<?> value;

  /**
   * @param name  {@link #name}
   * @param type  {@link #type}
   * @param value {@link #expression}
   */
  public SymbolExpression(final String name, final Type type, final Supplier<?> value) {
    this.name = name;
    this.type = type;
    this.value = value;
  }

  @Override
  public Expression toNode() {
    return new NameExpr(new SimpleName(name));
  }

  @Override
  public Type getType() {
    return type;
  }

  @Override
  public Object evaluate(final ExecutionContext context) {
    return value.get();
  }

}
