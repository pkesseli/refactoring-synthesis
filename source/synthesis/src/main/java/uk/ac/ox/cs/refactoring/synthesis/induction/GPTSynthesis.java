package uk.ac.ox.cs.refactoring.synthesis.induction;


import java.io.IOException;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;

import com.github.javaparser.ast.type.Type;
import com.github.javaparser.resolution.SymbolResolver;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;

import uk.ac.ox.cs.refactoring.classloader.ClassLoaders;
import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.ComponentDirectory;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.SnippetCandidate;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.methods.MethodIdentifier;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.parser.ParserContext;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.javadoc.SourceCodeConvertor;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.javadoc.SourceFinder;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.statement.ExpressionStatement;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.type.TypeFactory;

public class GPTSynthesis {

  public final ClassLoader classLoader;

  public final ParserContext parserContext;

  public final ComponentDirectory components;

  /** Method to be replaced. */
  private final MethodIdentifier methodToRefactor;

  private final SourceFinder sourceFinder;

  public GPTSynthesis(final MethodIdentifier methodToRefactor, final ClassLoader classLoader, final ParserContext parserContext,
      final ComponentDirectory components) {
    this.methodToRefactor = methodToRefactor;
    this.classLoader = classLoader;
    this.parserContext = parserContext;
    this.components = components;
    this.sourceFinder = new SourceFinder(parserContext, classLoader, methodToRefactor);
  }

  public SnippetCandidate synthesise(final String code) {

    final String className = methodToRefactor.FullyQualifiedClassName;
    final SymbolResolver symbolResolver = parserContext.SymbolResolver;
    final TypeSolver typeSolver = parserContext.TypeSolver;
    final JavaParser javaParser = parserContext.JavaParser;

    final ParseResult<CompilationUnit> parseResult;
    try {
      parseResult = sourceFinder.findSource(javaParser, className);
    } catch (final IOException e) {
      // TODO log info
      return null;
    }
    final MethodDeclaration method = sourceFinder.findMethod(symbolResolver, typeSolver, parseResult);
    final Type defaultType;
    try {
      defaultType = TypeFactory.createClassType(ClassLoaders.loadClass(classLoader, className));
    } catch (final ClassNotFoundException e) {
      // TODO log info
      return null;
    }

    var block = parserContext.JavaParser.parseBlock(code);
    final var candidate = new SnippetCandidate();


    for (final var suggestedStatement: block.getResult().get().getStatements()) {

      // TODO maybe re-parse within the method context?
      final var statement = sourceFinder.parseInMethodContext(symbolResolver, typeSolver, javaParser, defaultType, parseResult, method, suggestedStatement);
      // final var statement = suggestedStatement;
      final var expression = statement.asExpressionStmt().getExpression();

      System.out.println("parsed expression: " + expression.toString());
      final var convertor = new SourceCodeConvertor(classLoader, parserContext.JavaParser, parserContext.TypeSolver, components.InvolvedClasses);
      candidate.Block.Statements.add(new ExpressionStatement(convertor.convertExpression(expression)));

    }

    System.out.println(candidate.Block.toNode().toString());
    System.out.println("done");
    return candidate;
  }
}
