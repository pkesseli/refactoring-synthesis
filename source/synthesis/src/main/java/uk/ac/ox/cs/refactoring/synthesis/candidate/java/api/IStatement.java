package uk.ac.ox.cs.refactoring.synthesis.candidate.java.api;

import java.lang.reflect.InvocationTargetException;

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
   * @throws ClassNotFoundException
   * @throws IllegalAccessException
   * @throws NoSuchFieldException
   */
  Object execute(ExecutionContext context) throws ClassNotFoundException, IllegalAccessException,
      InvocationTargetException, NoSuchFieldException, NoSuchMethodException;
}
