package uk.ac.ox.cs.refactoring.synthesis.induction;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Map.Entry;

import org.apache.commons.lang3.NotImplementedException;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;
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
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.parser.ParserFactory;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.api.GeneratorConfiguration;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.javadoc.ExpressionCompiler;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.javadoc.ResolveArgument;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.javadoc.SourceFinder;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.statement.ExpressionStatement;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.type.TypeFactory;
import uk.ac.ox.cs.refactoring.synthesis.cegis.CegisLoopListener;
import uk.ac.ox.cs.refactoring.synthesis.cegis.GPTHints;
import uk.ac.ox.cs.refactoring.synthesis.counterexample.Counterexample;
import uk.ac.ox.cs.refactoring.synthesis.invocation.ExecutionResult;
import uk.ac.ox.cs.refactoring.synthesis.neural.CodeEngine;
import uk.ac.ox.cs.refactoring.synthesis.neural.Prompt;
import uk.ac.ox.cs.refactoring.synthesis.neural.TextTagger;

public class NeuralSynthesis<Candidate> extends FuzzingSynthesis<Candidate> {
  public GPTHints hints;
  private CodeEngine codeEngine = new CodeEngine();

  public SourceFinder sourceFinder;

  private final ClassLoader classLoader;
  private final ParserContext parserContext;

  public NeuralSynthesis(final GeneratorConfiguration generatorConfiguration,
      final GeneratorRepository generatorRepository, final SourceOfRandomness sourceOfRandomness,
      final Class<Candidate> candidateType, final Method frameworkMethodPlaceholder,
      final CandidateExecutor<Candidate> executor, final CegisLoopListener<Candidate> listener,
      final GPTHints hints) {
    super(generatorConfiguration, generatorRepository, sourceOfRandomness, candidateType, frameworkMethodPlaceholder,
        executor, listener);
    this.hints = hints;
    final ClassLoader classLoader = ClassLoaders.createIsolated();
    final ParserContext parserContext = ParserFactory.create(classLoader);
    this.classLoader = classLoader;
    this.parserContext = parserContext;
    this.sourceFinder = new SourceFinder(parserContext, classLoader, hints.methodToRefactor);
  }

  @Override
  public Candidate getDefault() {
    return extractCandidate();
  }

  @Override
  public Candidate synthesise(final Map<Counterexample, ExecutionResult> counterexamples)
      throws ClassNotFoundException, IOException, IllegalAccessException, NoSuchElementException, NoSuchFieldException {

    List<String> candidates = codeEngine.generateCodeN(inductionPrompt(counterexamples), 3);

    String original = hints.after;
    for (int i = 0; i < candidates.size(); i++) {
      try {
        hints.after = candidates.get(i);
        return extractCandidate();
      } catch (Exception e) {
        hints.after = original;
      }
    }
    throw new NoSuchElementException("Failed to find a compilable candidate");
  }

  public Prompt basePrompt() {
    throw new NotImplementedException();
  }


  public String textualCounterExamples(final Map<Counterexample, ExecutionResult> counterexamples) {
    throw new NotImplementedException();
  }

  public Prompt inductionPrompt(final Map<Counterexample, ExecutionResult> counterexamples) {
    String ce = textualCounterExamples(counterexamples);
    String context = "I have a code snippet below that contains a call to a deprecated method.\n" +
      TextTagger.tag("code", hints.before) +
      "Below is a plausible refactoring that potentially removes the deprecated method.\n" +
      TextTagger.tag("code", hints.after) +
      "However, it is not semantically equivalent to the original code snippet." +
      "A set of counter examples is given below\n" + ce;
    Prompt prompt = new Prompt(context, "Give me a correct refactoring.");
    prompt.constraints.add("Your code must only contain a sequence of Java statements.");

    return prompt;
  }

  private Candidate extractCandidate() {
    final ComponentDirectory components = generatorConfiguration.Components;

    final String className = hints.methodToRefactor.FullyQualifiedClassName;
    final SymbolResolver symbolResolver = parserContext.SymbolResolver;
    final TypeSolver typeSolver = parserContext.TypeSolver;
    final JavaParser javaParser = parserContext.JavaParser;

    final ParseResult<CompilationUnit> parseResult;
    final MethodDeclaration method;
    final Type defaultType;
    try {
      parseResult = sourceFinder.findSource(javaParser, className);
      method = sourceFinder.findMethod(symbolResolver, typeSolver, parseResult);
      defaultType = TypeFactory.createClassType(ClassLoaders.loadClass(classLoader, className));
    } catch (Exception e) {
      listener.initial(null);
      throw new NoSuchElementException(e.getMessage());
    }

    var before = parseHints(hints.before);
    if (before instanceof BlockStmt) {
      try {
        final Map<String, IExpression> environment = new HashMap<>();
        final var mapping = new ResolveArgument(hints.methodToRefactor, javaParser);
        for (final var originalStatement : ((BlockStmt) before).getStatements()) {
          final var statement = sourceFinder.parseInMethodContext(symbolResolver, typeSolver, javaParser, defaultType,
              parseResult,
              method, originalStatement);
          final var expression = statement.asExpressionStmt().getExpression();

          mapping.checkExpression(expression);
        }
        environment.putAll(mapping.arguments);

        var after = (BlockStmt) parseHints(hints.after);
        var arguments = resolveArguments(environment);
        for (final var argument : arguments) {
          after.addStatement(0, argument);
        }

        final var candidate = new SnippetCandidate();
        final var compiler = new ExpressionCompiler(classLoader, parserContext.JavaParser, parserContext.TypeSolver,
            components.InvolvedClasses,
            environment, candidate);

        for (final var suggestedStatement : after.getStatements()) {

          final var statement = sourceFinder.parseInMethodContext(symbolResolver, typeSolver, javaParser, defaultType,
              parseResult,
              method, suggestedStatement);
          final var expression = statement.asExpressionStmt().getExpression();

          // System.out.println("parsed expression: " + expression.toString());
          final var instructionExpression = compiler.compile(expression);
          if (instructionExpression == null) {
            continue;
          }
          var convertedExpression = new ExpressionStatement(instructionExpression);
          candidate.Block.Statements.add(convertedExpression);

        }

        // System.out.println(candidate.Block.toNode().toString());
        // System.out.println("done");

        @SuppressWarnings("unchecked")
        Candidate res = (Candidate) candidate;
        listener.initial(res);
        return res;

      } catch (Exception e) {
        listener.initial(null);
        throw new NoSuchElementException("parse error: " + e.getMessage());
      }

    } else {
      listener.initial(null);
      throw new NoSuchElementException("not implemented");
    }
  }


  /**
   * 
   * @param hints
   * @return either a {@link BlockStmt} or a {@link CompilationUnit}
   */
  private Object parseHints(final String hints) {

    ParseResult<BlockStmt> block = parserContext.JavaParser.parseBlock(hints);
    if (block.getResult().isPresent()) {
      return block.getResult().get();
    }

    ParseResult<Statement> statement = parserContext.JavaParser.parseStatement(hints);
    if (statement.getResult().isPresent() && statement.getResult().get() instanceof IfStmt) {
      var ifStmt = (IfStmt) statement.getResult().get();
      if (ifStmt.getThenStmt() instanceof BlockStmt &&
          ((BlockStmt) ifStmt.getThenStmt()).getStatements().isEmpty() &&
          ifStmt.getElseStmt().isEmpty()) {
        return new BlockStmt(new NodeList<>(new ExpressionStmt(ifStmt.getCondition())));
      }
    }

    block = parserContext.JavaParser.parseBlock("{" + hints + "}");
    if (block.getResult().isPresent()) {
      return block.getResult().get();
    }

    final ParseResult<CompilationUnit> unit = parserContext.JavaParser.parse(hints);
    if (unit.getResult().isPresent()) {
      return unit.getResult().get();
    }

    throw new NoSuchElementException("hints not parsable");
  }

  private NodeList<Statement> resolveArguments(final Map<String, IExpression> environment) {

    final NodeList<Statement> result = new NodeList<>();

    for (Entry<String, IExpression> entry : environment.entrySet()) {
      result.add(parserContext.JavaParser
          .parseStatement(entry.getValue().getType().asString() + " " + entry.getKey() + ";").getResult().get());
    }

    return result;
  }
}
