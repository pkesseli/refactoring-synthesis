package uk.ac.ox.cs.refactoring.synthesis.candidate.java.parser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ParserConfiguration.LanguageLevel;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ClassLoaderTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

public final class ParserFactory {

  private ParserFactory() {
  }

  public static ParserContext create(final ClassLoader classLoader) {
    final ParserConfiguration parserConfiguration = new ParserConfiguration();
    parserConfiguration.setLanguageLevel(LanguageLevel.JAVA_15);
    final CombinedTypeSolver typeSolver = new CombinedTypeSolver(new ReflectionTypeSolver(),
        new ClassLoaderTypeSolver(classLoader));
    typeSolver.setExceptionHandler(SecurityException.class::isInstance);
    final JavaSymbolSolver symbolResolver = new JavaSymbolSolver(typeSolver);
    parserConfiguration.setSymbolResolver(symbolResolver);
    final JavaParser javaParser = new JavaParser(parserConfiguration);
    return new ParserContext(typeSolver, symbolResolver, javaParser);
  }
}
