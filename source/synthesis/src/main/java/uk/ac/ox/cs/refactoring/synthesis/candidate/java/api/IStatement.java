package uk.ac.ox.cs.refactoring.synthesis.candidate.java.api;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import com.github.javaparser.ast.stmt.Statement;

import uk.ac.ox.cs.refactoring.synthesis.candidate.api.ExecutionContext;

/**
 * Models a Java language statement.
 */
public interface IStatement extends INodeConvertible<Statement> {
  /**
   * Invoke {@code this} statement in the given context.
   * 
   * @param context Context in which to perform the execution.
   * 
   * @return Return expression, if applicable. {@code null} otherwise.
   * 
   * @throws ClassNotFoundException    if class whose method to execute could not
   *                                   be accessed.
   * @throws IllegalAccessException    if reflecitve access was denied.
   * @throws InstantiationException    if a {@code this} instance could not be
   *                                   created.
   * @throws InvocationTargetException if a reflective execution throws an error.
   * @throws NoSuchFieldException      if a field could not be accessed
   *                                   reflectively.
   * @throws NoSuchMethodException     if a method to execute could not be
   *                                   accessed reflectively.
   */
  Object execute(ExecutionContext context) throws ClassNotFoundException, IllegalAccessException,
      InstantiationException, InvocationTargetException, NoSuchFieldException, NoSuchMethodException;

  /**
   * If present, provides an expression using which the value of {@code this}
   * statement can be reused. The typical example is a statement declaring a
   * temporary variable to which we provide a symbol expression.
   * 
   * @return {@link Optional} symbol expression referring to a designated
   *         statement value.
   */
  Optional<IExpression> getSymbolExpression();
}
