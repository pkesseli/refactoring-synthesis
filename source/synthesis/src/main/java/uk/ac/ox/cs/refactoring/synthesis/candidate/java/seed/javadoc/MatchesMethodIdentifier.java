package uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.javadoc;

import java.util.function.Predicate;

import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceTypeImpl;

import uk.ac.ox.cs.refactoring.synthesis.candidate.java.methods.MethodIdentifier;

/**
 * Checks whether a type declared by a given {@link TypeDeclaration} matches the
 * fully qualified class name in a {@link MethodIdentifier}.
 */
class MatchesMethodIdentifier implements Predicate<TypeDeclaration<?>> {

  /**
   * Used to resolve the simple name in a {@link TypeDeclaration} to a fully
   * qualified one.
   */
  private final TypeSolver typeSolver;

  /**
   * {@link MethodIdentifier} for which to find a matching class.
   */
  private final MethodIdentifier methodIdentifier;

  /**
   * @param typeSolver       {@link #typeSolver}
   * @param methodIdentifier {@link #methodIdentifier}
   */
  MatchesMethodIdentifier(final TypeSolver typeSolver, final MethodIdentifier methodIdentifier) {
    this.typeSolver = typeSolver;
    this.methodIdentifier = methodIdentifier;
  }

  @Override
  public boolean test(final TypeDeclaration<?> type) {
    final ResolvedReferenceType resolvedType = ReferenceTypeImpl.undeterminedParameters(type.resolve(), typeSolver);
    return resolvedType.getQualifiedName().equals(methodIdentifier.FullyQualifiedClassName);
  }

}