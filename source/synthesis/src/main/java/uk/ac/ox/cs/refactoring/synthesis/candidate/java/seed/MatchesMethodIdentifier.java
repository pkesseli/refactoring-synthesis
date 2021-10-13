package uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed;

import java.util.function.Predicate;

import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceTypeImpl;

import uk.ac.ox.cs.refactoring.synthesis.candidate.java.methods.MethodIdentifier;

class MatchesMethodIdentifier implements Predicate<TypeDeclaration<?>> {

  private final TypeSolver typeSolver;

  private final MethodIdentifier methodIdentifier;

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
