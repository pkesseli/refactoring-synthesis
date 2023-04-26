package uk.ac.ox.cs.refactoring.synthesis.candidate.java.parser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;

public class ParserContext {

  public final CombinedTypeSolver TypeSolver;

  public final JavaSymbolSolver SymbolResolver;

  public final JavaParser JavaParser;

  public ParserContext(final CombinedTypeSolver typesolver, final JavaSymbolSolver symbolSolver,
      final JavaParser javaParser) {
    TypeSolver = typesolver;
    SymbolResolver = symbolSolver;
    JavaParser = javaParser;
  }
}
