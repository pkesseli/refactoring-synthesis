package uk.ac.ox.cs.refactoring.synthesis.induction;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.lang3.NotImplementedException;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.javadoc.Javadoc;
import com.github.javaparser.resolution.SymbolResolver;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.pholser.junit.quickcheck.internal.generator.GeneratorRepository;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import uk.ac.ox.cs.refactoring.classloader.ClassLoaders;
import uk.ac.ox.cs.refactoring.synthesis.candidate.api.CandidateExecutor;
import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.ComponentDirectory;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.IExpression;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.SnippetCandidate;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.api.GeneratorConfiguration;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.javadoc.ExpressionCompiler;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.javadoc.JavaDocSeed;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.javadoc.ResolveArgument;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.statement.ExpressionStatement;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.type.TypeFactory;
import uk.ac.ox.cs.refactoring.synthesis.cegis.CegisLoopListener;
import uk.ac.ox.cs.refactoring.synthesis.cegis.GPTHints;
import uk.ac.ox.cs.refactoring.synthesis.counterexample.Counterexample;
import uk.ac.ox.cs.refactoring.synthesis.invocation.ExecutionResult;
import uk.ac.ox.cs.refactoring.synthesis.neural.CodeEngine;
import uk.ac.ox.cs.refactoring.synthesis.neural.Legacy;
import uk.ac.ox.cs.refactoring.synthesis.neural.Prompt;
import uk.ac.ox.cs.refactoring.synthesis.neural.TextTagger;

public class NeuralSynthesis<Candidate> extends FuzzingSynthesis<Candidate> {
  private CodeEngine codeEngine;
  private String javadocComment = null;
  private String templateCallString = null;
  private ParseResult<CompilationUnit> parseResult = null;
  private MethodDeclaration method = null;
  private Type defaultType = null;
  private Map<String, IExpression> environment = null;

  public NeuralSynthesis(final GeneratorConfiguration generatorConfiguration,
      final GeneratorRepository generatorRepository, final SourceOfRandomness sourceOfRandomness,
      final Class<Candidate> candidateType, final Method frameworkMethodPlaceholder,
      final CandidateExecutor<Candidate> executor, final CegisLoopListener<Candidate> listener,
      final GPTHints hints) {
    super(generatorConfiguration, generatorRepository, sourceOfRandomness, candidateType, frameworkMethodPlaceholder,
        executor, listener);

    codeEngine = new Legacy(hints);

    final String className = generatorConfiguration.javaDocSeed.methodToRefactor.FullyQualifiedClassName;
    final CombinedTypeSolver typeSolver = generatorConfiguration.javaDocSeed.parserContext.TypeSolver;
    final JavaSymbolSolver symbolResolver = generatorConfiguration.javaDocSeed.parserContext.SymbolResolver;
    final JavaParser javaParser = generatorConfiguration.javaDocSeed.parserContext.JavaParser;
    try {
      parseResult = generatorConfiguration.javaDocSeed.findSource(javaParser, className);
      defaultType = TypeFactory.createClassType(ClassLoaders.loadClass(generatorConfiguration.javaDocSeed.classLoader, className));
    } catch (final Exception e) {
      return;
    }

    method = generatorConfiguration.javaDocSeed.findMethod(symbolResolver, typeSolver, parseResult);
    if (method == null)
      return;

    StringBuilder templateCallString = new StringBuilder();
    if (!method.isStatic()) {
      templateCallString.append("this.");
    }
    templateCallString.append(method.getNameAsString());
    templateCallString.append('(');
    templateCallString.append(method.getParameters().stream().map(param -> param.getNameAsString()).collect(Collectors.joining(", ")));
    templateCallString.append(");");
    this.templateCallString = templateCallString.toString();


    final Optional<Javadoc> javadocComment = method.getJavadoc();
    if (!javadocComment.isPresent())
      return;

    this.javadocComment = javadocComment.get().toText();


    var callStmt = parseString(this.templateCallString);
    System.out.println(callStmt);
    if (callStmt instanceof BlockStmt) {
      environment = new HashMap<>();
      final var resolveArg = new ResolveArgument(generatorConfiguration.javaDocSeed.methodToRefactor, javaParser);
      for (final var stmt : ((BlockStmt) callStmt).getStatements()) {
        final var statement = parseInMethodContext(symbolResolver, typeSolver, javaParser, defaultType,
            parseResult,
            method, stmt);
        final var expression = statement.asExpressionStmt().getExpression();
        resolveArg.checkExpression(expression);
      }
      environment.putAll(resolveArg.arguments);

    } else {
      return;
    }
  }

  @Override
  public Candidate getDefault() {
    String code = codeEngine.generateCode(basePrompt());
    try {
      Candidate candidate = processCode(code);
      listener.initial(candidate);
      return candidate;
    } catch (final NoSuchElementException e) {
      listener.initial(null);
      throw e;
    }
  }

  @Override
  public Candidate synthesise(final Map<Counterexample, ExecutionResult> counterexamples)
      throws ClassNotFoundException, IOException, IllegalAccessException, NoSuchElementException, NoSuchFieldException {

    String code = codeEngine.generateCode(inductionPrompt(counterexamples));
    try {
      Candidate candidate = processCode(code);
      return candidate;
    } catch (final NoSuchElementException e) {
      throw e;
    }
  }

  public Prompt basePrompt() {
    StringBuilder contextBuilder = new StringBuilder();
    contextBuilder.append("The method" + generatorConfiguration.javaDocSeed.methodToRefactor.MethodName + "is deprecated.");
    if (javadocComment != null) {
      contextBuilder.append("The related deprecation comment in the Javadoc is contained in the following <deprecation-comment> tag\n");
      contextBuilder.append(TextTagger.tag("deprecation-comment", javadocComment));
    }
    contextBuilder.append("However, I used this method call in my code base, the code snippet is contained in the following <code> tag:");
    contextBuilder.append(TextTagger.tag("code", templateCallString));

    String context = contextBuilder.toString();
    String instruction = "Help me refactor this code snippet.";
    Prompt prompt = new Prompt(context, instruction);
    prompt.constraints.add("Your answer must only contain a sequence of Java statements");
    return new Prompt(context, instruction);
  }


  public String textualCounterExamples(final Map<Counterexample, ExecutionResult> counterexamples) {
    throw new NotImplementedException();
  }

  public Prompt inductionPrompt(final Map<Counterexample, ExecutionResult> counterexamples) {
    Prompt prompt = basePrompt();
    return prompt;
  }

  public Statement parseInMethodContext(final SymbolResolver symbolResolver, final TypeSolver typeSolver,
      final JavaParser javaParser, final Type defaultType, final ParseResult<CompilationUnit> compilationUnit,
      final MethodDeclaration method, final Statement statement) {
    final Optional<BlockStmt> optionalBody = method.getBody();
    if (optionalBody.isPresent())
      optionalBody.get().addStatement(0, statement);
    else {
      final BlockStmt newBody = new BlockStmt();
      newBody.addStatement(0, statement);
      method.setBody(newBody);
    }
    System.out.println(statement.toString());
    System.out.println(method.toString());
    final ParseResult<CompilationUnit> parseResult = javaParser.parse(compilationUnit.getResult().get().toString());
    final MethodDeclaration container = generatorConfiguration.javaDocSeed.findMethod(symbolResolver, typeSolver, parseResult);
    final Statement result = container.getBody().get().getStatement(0);
    return result;
  }


  /**
   * 
   * @param code
   * @return either a {@link BlockStmt} or a {@link CompilationUnit}
   */
  private Object parseString(final String code) {

    ParseResult<BlockStmt> block = generatorConfiguration.javaDocSeed.parserContext.JavaParser.parseBlock(code);
    if (block.getResult().isPresent()) {
      return block.getResult().get();
    }

    ParseResult<Statement> statement = generatorConfiguration.javaDocSeed.parserContext.JavaParser.parseStatement(code);
    if (statement.getResult().isPresent() && statement.getResult().get() instanceof IfStmt) {
      var ifStmt = (IfStmt) statement.getResult().get();
      if (ifStmt.getThenStmt() instanceof BlockStmt &&
          ((BlockStmt) ifStmt.getThenStmt()).getStatements().isEmpty() &&
          ifStmt.getElseStmt().isEmpty()) {
        return new BlockStmt(new NodeList<>(new ExpressionStmt(ifStmt.getCondition())));
      }
    }

    block = generatorConfiguration.javaDocSeed.parserContext.JavaParser.parseBlock("{" + code + "}");
    if (block.getResult().isPresent()) {
      return block.getResult().get();
    }

    final ParseResult<CompilationUnit> unit = generatorConfiguration.javaDocSeed.parserContext.JavaParser.parse(code);
    if (unit.getResult().isPresent()) {
      return unit.getResult().get();
    }

    throw new NoSuchElementException("hints not parsable");
  }

  private NodeList<Statement> resolveArguments(final Map<String, IExpression> environment) {

    final NodeList<Statement> result = new NodeList<>();

    for (Entry<String, IExpression> entry : environment.entrySet()) {
      result.add(generatorConfiguration.javaDocSeed.parserContext.JavaParser
          .parseStatement(entry.getValue().getType().asString() + " " + entry.getKey() + ";").getResult().get());
    }

    return result;
  }

  private Candidate processCode(String code) throws NoSuchElementException {
    final ComponentDirectory components = generatorConfiguration.Components;
    final SymbolResolver symbolResolver = generatorConfiguration.javaDocSeed.parserContext.SymbolResolver;
    final TypeSolver typeSolver = generatorConfiguration.javaDocSeed.parserContext.TypeSolver;
    final JavaParser javaParser = generatorConfiguration.javaDocSeed.parserContext.JavaParser;


    try {
      var stmts = (BlockStmt) parseString(code);
      var arguments = resolveArguments(environment);
      for (final var argument : arguments) {
        stmts.addStatement(0, argument);
      }

      SnippetCandidate candidate = new SnippetCandidate();
      final var compiler = new ExpressionCompiler(generatorConfiguration.javaDocSeed.classLoader, generatorConfiguration.javaDocSeed.parserContext.JavaParser, generatorConfiguration.javaDocSeed.parserContext.TypeSolver,
          components.InvolvedClasses,
          environment, candidate);

      for (final var stmt : stmts.getStatements()) {

        final var statement = parseInMethodContext(symbolResolver, typeSolver, javaParser, defaultType,
            parseResult,
            method, stmt);
        final var expression = statement.asExpressionStmt().getExpression();

        // System.out.println("parsed expression: " + expression.toString());
        final var instructionExpression = compiler.compile(expression);
        if (instructionExpression == null) {
          continue;
        }
        var convertedExpression = new ExpressionStatement(instructionExpression);
        candidate.Block.Statements.add(convertedExpression);

      }
      @SuppressWarnings("unchecked")
      Candidate res = (Candidate) candidate;
      return res;

    } catch (final Exception e) {
      throw new NoSuchElementException(e.getMessage());
    }
  }
}
