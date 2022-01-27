package uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.javadoc;

import java.util.function.Predicate;

import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;

/** Used to identify a method by simple name. */
class MatchesMethodName implements Predicate<ResolvedMethodDeclaration> {

  /** Simple method name. */
  private final String methodName;

  /** @param methodName {@link #methodName} */
  MatchesMethodName(final String methodName) {
    this.methodName = methodName;
  }

  @Override
  public boolean test(final ResolvedMethodDeclaration t) {
    return methodName.equals(t.getName());
  }
}
