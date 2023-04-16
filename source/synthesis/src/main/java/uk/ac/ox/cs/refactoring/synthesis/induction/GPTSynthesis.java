package uk.ac.ox.cs.refactoring.synthesis.induction;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.resolution.SymbolResolver;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.pholser.junit.quickcheck.internal.generator.GeneratorRepository;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import uk.ac.ox.cs.refactoring.classloader.ClassLoaders;
import uk.ac.ox.cs.refactoring.synthesis.candidate.api.CandidateExecutor;
import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.ComponentDirectory;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.IExpression;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.SnippetCandidate;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.parser.ParserContext;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.api.GeneratorConfiguration;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.javadoc.IRGenerator;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.javadoc.ParameterMapping;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.javadoc.SourceFinder;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.statement.ExpressionStatement;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.type.TypeFactory;
import uk.ac.ox.cs.refactoring.synthesis.cegis.CegisLoopListener;
import uk.ac.ox.cs.refactoring.synthesis.cegis.GPTHints;
import uk.ac.ox.cs.refactoring.synthesis.counterexample.Counterexample;
import uk.ac.ox.cs.refactoring.synthesis.invocation.ExecutionResult;

public class GPTSynthesis<Candidate> extends FuzzingSynthesis<Candidate> {
  public final GPTHints hints;

  public SourceFinder sourceFinder;

  public GPTSynthesis(final GeneratorConfiguration generatorConfiguration,
      final GeneratorRepository generatorRepository, final SourceOfRandomness sourceOfRandomness,
      final Class<Candidate> candidateType, final Method frameworkMethodPlaceholder,
      final CandidateExecutor<Candidate> executor, final CegisLoopListener<Candidate> listener,
      final GPTHints hints) {
    super(generatorConfiguration, generatorRepository, sourceOfRandomness, candidateType, frameworkMethodPlaceholder, executor, listener);
    this.hints = hints;
    this.sourceFinder = new SourceFinder(generatorConfiguration.parserContext, generatorConfiguration.classLoader, generatorConfiguration.methodToRefactor);
  }

  @Override
  public Candidate getDefault() {

    final ClassLoader classLoader = generatorConfiguration.classLoader;
    final ParserContext parserContext = generatorConfiguration.parserContext;
    final ComponentDirectory components = generatorConfiguration.Components;

    final String className = generatorConfiguration.methodToRefactor.FullyQualifiedClassName;
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

    var block = generatorConfiguration.parserContext.JavaParser.parseBlock(hints.after);
    final var candidate = new SnippetCandidate();
    final Map<String, IExpression> environment = new HashMap<>();


    if (hints.before != null) {
      var original = generatorConfiguration.parserContext.JavaParser.parseBlock(hints.before);
      final var remapper = new ParameterMapping(generatorConfiguration.methodToRefactor, javaParser);

      for (final var originalStatement: original.getResult().get().getStatements()) {

        final var statement = sourceFinder.parseInMethodContext(symbolResolver, typeSolver, javaParser, defaultType, parseResult,
            method, originalStatement);
        final var expression = statement.asExpressionStmt().getExpression();

        remapper.checkExpression(expression);
      }



      environment.putAll(remapper.arguments);
    }

    final var convertor = new IRGenerator(classLoader, parserContext.JavaParser, parserContext.TypeSolver, components.InvolvedClasses,
        environment, candidate);


    for (final var suggestedStatement: block.getResult().get().getStatements()) {

      final var statement = sourceFinder.parseInMethodContext(symbolResolver, typeSolver, javaParser, defaultType, parseResult,
          method, suggestedStatement);
      final var expression = statement.asExpressionStmt().getExpression();

      System.out.println("parsed expression: " + expression.toString());
      final var instructionExpression = convertor.convertExpression(expression);
      if (instructionExpression == null) {
        continue;
      }
      candidate.Block.Statements.add(new ExpressionStatement(convertor.convertExpression(expression)));

    }

    System.out.println(candidate.Block.toNode().toString());
    System.out.println("done");

    // Hmmmm
    Candidate res = (Candidate) candidate;

    listener.initial(res);
    
    return res;
  }

  @Override
  public Candidate synthesise(final Map<Counterexample, ExecutionResult> counterexamples)
      throws ClassNotFoundException, IOException, IllegalAccessException, NoSuchElementException, NoSuchFieldException {
    throw new NoSuchElementException();
  }
}
