package uk.ac.ox.cs.refactoring.synthesis.candidate.java.api;

import com.github.javaparser.ast.stmt.Statement;

/**
 * Models a Java language statement.
 */
public interface IStatement extends INodeConvertible<Statement>, Runnable {
}
