package uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder;

import com.github.javaparser.ast.type.Type;

import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.IExpression;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.IStatement;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.LeftHandSideExpression;

public final class JavaLanguageKeys {
  
  private JavaLanguageKeys() {
  }

  public static JavaLanguageKey statement(final Type type) {
    return new JavaLanguageKey(IStatement.class, type, false);
  }

  public static JavaLanguageKey lhs(final Type type) {
    return new JavaLanguageKey(LeftHandSideExpression.class, type, true);
  }

  public static JavaLanguageKey expression(final Type type) {
    return new JavaLanguageKey(IExpression.class, type, false);
  }

  public static JavaLanguageKey nonnull(final Type type) {
    return new JavaLanguageKey(IExpression.class, type, true);
  }

}
