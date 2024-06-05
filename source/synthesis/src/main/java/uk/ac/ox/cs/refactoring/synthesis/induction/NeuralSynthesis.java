package uk.ac.ox.cs.refactoring.synthesis.induction;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.resolution.SymbolResolver;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.pholser.junit.quickcheck.internal.generator.GeneratorRepository;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import uk.ac.ox.cs.refactoring.synthesis.candidate.api.CandidateExecutor;
import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.ComponentDirectory;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.IExpression;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.SnippetCandidate;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.api.GeneratorConfiguration;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.javadoc.ExpressionCompiler;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.javadoc.ResolveArgument;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.statement.ExpressionStatement;
import uk.ac.ox.cs.refactoring.synthesis.cegis.CegisLoopListener;
import uk.ac.ox.cs.refactoring.synthesis.counterexample.Counterexample;
import uk.ac.ox.cs.refactoring.synthesis.invocation.ExecutionResult;
import uk.ac.ox.cs.refactoring.synthesis.neural.CodeEngine;
import uk.ac.ox.cs.refactoring.synthesis.neural.Claude2;
import uk.ac.ox.cs.refactoring.synthesis.neural.Claude3;
import uk.ac.ox.cs.refactoring.synthesis.neural.LocalCodeLLaMa2;
import uk.ac.ox.cs.refactoring.synthesis.neural.Prompt;
import uk.ac.ox.cs.refactoring.synthesis.neural.TextTagger;

public class NeuralSynthesis<Candidate> extends FuzzingSynthesis<Candidate> {
  private static final Logger LOGGER = Logger.getLogger(NeuralSynthesis.class.getName());
  private static final String MODEL = System.getProperty("model");
  private static final Boolean WITH_CODE_HINTS = Boolean.getBoolean("codehints");
  private final CodeEngine codeEngine;

  // private CodeEngine codeEngine;
  private String methodCommentContext = null;
  private String methodSignatureContext = null;
  // private String javadocComment = null;
  private String templateCallString = null;
  private ParseResult<CompilationUnit> parseResult = null;
  private MethodDeclaration method = null;
  private Map<String, IExpression> environment = null;

  private int budget = Integer.getInteger("budget");

  private void consumeBudget() {
    budget -= 1;
  }

  private boolean noBudget() {
    return budget == 0;
  }

  public NeuralSynthesis(final GeneratorConfiguration generatorConfiguration,
      final GeneratorRepository generatorRepository, final SourceOfRandomness sourceOfRandomness,
      final Class<Candidate> candidateType, final Method frameworkMethodPlaceholder,
      final CandidateExecutor<Candidate> executor, final CegisLoopListener<Candidate> listener) {
    super(generatorConfiguration, generatorRepository, sourceOfRandomness, candidateType, frameworkMethodPlaceholder,
        executor, listener);

    if (MODEL != null) {
      switch (MODEL) {
        case "codellama2":
          codeEngine = new LocalCodeLLaMa2();
          break;
        case "claude2":
          codeEngine = new Claude2();
          break;
        case "claude3":
          codeEngine = new Claude3();
          break;
        default:
          codeEngine = null;
          break;
      }      
    } else {
      codeEngine = null;
    }


    final String className = generatorConfiguration.javaDocSeed.methodToRefactor.FullyQualifiedClassName;
    final CombinedTypeSolver typeSolver = generatorConfiguration.javaDocSeed.parserContext.TypeSolver;
    final JavaSymbolSolver symbolResolver = generatorConfiguration.javaDocSeed.parserContext.SymbolResolver;
    final JavaParser javaParser = generatorConfiguration.javaDocSeed.parserContext.JavaParser;
    try {
      parseResult = generatorConfiguration.javaDocSeed.findSource(javaParser, className);
    } catch (final Exception e) {
      return;
    }

    method = generatorConfiguration.javaDocSeed.findMethod(symbolResolver, typeSolver, parseResult);
    if (method == null)
      return;

    if (WITH_CODE_HINTS && method.getJavadocComment().isPresent()) {
      var javadocComment = method.getJavadocComment().get();
      methodCommentContext = javadocComment.getContent();
    }
    // if (!WITH_CODE_HINTS) {
    //   method.setComment(null);
    // }
    // methodSignatureContext = method.getSignature().asString();
    method.setComment(null);
    methodSignatureContext = method.toString();

    StringBuilder templateCallString = new StringBuilder();
    if (!method.isStatic()) {
      templateCallString.append("this.");
    }
    templateCallString.append(method.getNameAsString());
    templateCallString.append('(');
    templateCallString.append(method.getParameters().stream().map(param -> param.getNameAsString()).collect(Collectors.joining(", ")));
    templateCallString.append(");");
    this.templateCallString = templateCallString.toString();


    // final Optional<Javadoc> javadocComment = method.getJavadoc();
    // if (javadocComment.isPresent()) {
    //   this.javadocComment = org.apache.commons.lang3.StringEscapeUtils.unescapeHtml4(javadocComment.get().toText());
    // }

    var callStmt = parseString(this.templateCallString);
    if (callStmt instanceof BlockStmt) {
      environment = new HashMap<>();
      final var resolveArg = new ResolveArgument(generatorConfiguration.javaDocSeed.methodToRefactor, javaParser);
      for (final var stmt : ((BlockStmt) callStmt).getStatements()) {
        final var statement = parseInMethodContext(symbolResolver, typeSolver, javaParser, stmt);
        final var expression = statement.asExpressionStmt().getExpression();
        resolveArg.checkExpression(expression);
      }
      environment.putAll(resolveArg.arguments);

    }
  }

  @Override
  public Candidate getDefault() {
    if (noBudget()) {
      throw new NoSuchElementException("Running out of budget.");
    }
    consumeBudget();
    String code = codeEngine.generateCode(basePrompt());
    try {
      Candidate candidate = processCode(code);
      listener.initial(candidate);
      return candidate;
    } catch (final NoSuchElementException e) {
      listener.initial(null);
      return null;
    }
  }

  @Override
  public Candidate synthesise(final Map<Counterexample, ExecutionResult> counterexamples)
      throws ClassNotFoundException, IOException, IllegalAccessException, NoSuchElementException, NoSuchFieldException {
    if (noBudget()) {
      throw new NoSuchElementException("Running out of budget.");
    }
    consumeBudget();
    Prompt prompt = inductionPrompt(counterexamples);
    String code = codeEngine.generateCode(prompt);
    try {
      Candidate candidate = processCode(code);
      return candidate;
    } catch (final NoSuchElementException e) {
      return null;
    }
  }

  public Prompt basePrompt() {
    StringBuilder contextBuilder = new StringBuilder();
    contextBuilder.append(String.format("The method %s of the class %s is deprecated.\n",
        TextTagger.tag("method-name", generatorConfiguration.javaDocSeed.methodToRefactor.MethodName, true),
        TextTagger.tag("class-name", generatorConfiguration.javaDocSeed.methodToRefactor.FullyQualifiedClassName, true)));
    // contextBuilder.append("Below is the method signature");
    // contextBuilder.append(":\n");
    // contextBuilder.append(String.format("\n%s\n", TextTagger.tag("method-signature", methodSignatureContext)));
    contextBuilder.append("The method definition can be found below\n");
    contextBuilder.append(String.format("\n%s\n", TextTagger.tag("method-definition", methodSignatureContext)));
    if (WITH_CODE_HINTS) {
      var deprecationCommentDescription = "Here are its Javadoc comments that may contain a @deprecated tag explaining why the item has been deprecated and suggesting what to use instead:\n";
      contextBuilder.append(deprecationCommentDescription);
      contextBuilder.append(TextTagger.tag("javadoc-comment", methodCommentContext));
    }
    // if (javadocComment != null && WITH_CODE_HINTS) {
    //   contextBuilder.append("The related deprecation comment in the Javadoc is contained in the following <deprecation-comment> tag:\n");
    //   contextBuilder.append(TextTagger.tag("deprecation-comment", javadocComment));
    // }
    contextBuilder.append("However, I used this method call in my code base, the code snippet is contained in the following <code-snippet> tag:\n");
    contextBuilder.append(TextTagger.tag("code-snippet", templateCallString));

    String context = contextBuilder.toString();
    String instruction = "Help me refactor this code snippet so that it doesn't use the deprecated method.";
    if (WITH_CODE_HINTS) {
      instruction += " Do not simply inline the method body, use APIs suggested by <javadoc-comment> if there is any.";
    } else {
      instruction += " Do not simply inline the method body.";
    }
    Prompt prompt = new Prompt(context, instruction);
    // if (WITH_CODE_HINTS) {
    //   prompt.constraints.add("The javaDoc comments might contain useful components for you to construct your answer.");
    // }
    // prompt.constraints.add("Prefer method invocations over operators (e.g. choose `append` over String `+` operator).");
    prompt.constraints.add("Your answer must only contain a straight line Java program. Use variables available in the <code-snippet>. Do not use if-conditions or loops.");
    // prompt.constraints.add("Do not simply inline original implementations");
    return prompt;
  }

  private String marshalObject(Object object) throws JsonProcessingException {
    var mapper = new ObjectMapper()
      .enable(SerializationFeature.INDENT_OUTPUT);
    mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    mapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector() {
      @Override
      public boolean hasIgnoreMarker(final AnnotatedMember m) {
        return super.hasIgnoreMarker(m) || m.getName().contains("Mockito");
      }
    });
    // mapper.setVisibility
    var json = mapper.writeValueAsString(object);
    return json;
  }

  public class SerializableExecution {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Object thisInstance;
    public List<Object> methodArguments;
    public Object returnValue;
  }
  private List<String> counterexamplesReprs(final Map<Counterexample, ExecutionResult> counterexamples) throws JsonProcessingException {
    ArrayList<String> representables = new ArrayList<>();
    for (Map.Entry<Counterexample, ExecutionResult> entry : counterexamples.entrySet()) {
      Counterexample counterexample = entry.getKey();
      ExecutionResult executionResult = entry.getValue();
      Object instance = counterexample.Instance;
      List<Object> arguments = counterexample.Arguments;
      Throwable error = executionResult.Error;
      Object returnValue = executionResult.Value;
      if (error == null && returnValue != null) {
        var serialisable = new SerializableExecution();
        serialisable.thisInstance = instance;
        serialisable.methodArguments = arguments;
        serialisable.returnValue = returnValue;
        representables.add(marshalObject(serialisable));
      }
    }

    return representables;
  }

  public Prompt inductionPrompt(final Map<Counterexample, ExecutionResult> counterexamples) {
    try {
      Prompt prompt = basePrompt();
      List<String> reprs = counterexamplesReprs(counterexamples);
      if (reprs.size() == 0) {
        return prompt;
      }
      StringBuilder extraInformationBuilder = new StringBuilder();
      extraInformationBuilder.append("Here is a set of input/output examples that you should respect:\n");
      String examplesList = reprs.stream()
          .map(example -> String.format("  <example>\n    %s\n  </example>", example))
          .collect(Collectors.joining("\n"));
      extraInformationBuilder.append(TextTagger.tag("examples-list", examplesList));
      prompt.extraInformation = extraInformationBuilder.toString();
      return prompt;
    } catch (JsonProcessingException e) {
      return basePrompt();
    }
  }

  public Statement parseInMethodContext(final SymbolResolver symbolResolver, final TypeSolver typeSolver,
      final JavaParser javaParser, final Statement statement) {
    final Optional<BlockStmt> optionalBody = method.getBody();
    if (optionalBody.isPresent())
      optionalBody.get().addStatement(0, statement);
    else {
      final BlockStmt newBody = new BlockStmt();
      newBody.addStatement(0, statement);
      method.setBody(newBody);
    }
    final ParseResult<CompilationUnit> compilationUnit = javaParser.parse(parseResult.getResult().get().toString());
    final MethodDeclaration container = generatorConfiguration.javaDocSeed.findMethod(symbolResolver, typeSolver, compilationUnit);
    final Statement result = container.getBody().get().getStatement(0);
    return result;
  }


  /**
   * 
   * @param code
   * @return either a {@link BlockStmt} or a {@link CompilationUnit}
   */
  private Object parseString(final String code) {

    var parser = generatorConfiguration.javaDocSeed.parserContext.JavaParser;


    ParseResult<BlockStmt> block = parser.parseBlock(code);
    if (block.isSuccessful()) {
      return block.getResult().get();
    }

    block = parser.parseBlock("{" + code + "}");
    if (block.isSuccessful()) {
      return block.getResult().get();
    }

    block = parser.parseBlock(String.format("{%s;}", code));
    if (block.isSuccessful()) {
      return block.getResult().get();
    }


    throw new NoSuchElementException(String.format("Failed to Parse Code:\n%s\n", code));
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

    // System.out.printf("Processing %s\n", code);
    try {
      var stmts = (BlockStmt) parseString(code);
      System.out.println("Parsed code:");
      System.out.println(stmts.toString());
      // System.out.printf("getting %d statements\n", stmts.getStatements().size());
      var arguments = resolveArguments(environment);
      for (final var argument : arguments) {
        stmts.addStatement(0, argument);
      }

      SnippetCandidate candidate = new SnippetCandidate();
      final var compiler = new ExpressionCompiler(generatorConfiguration.javaDocSeed.classLoader, 
          generatorConfiguration.javaDocSeed.parserContext.JavaParser, 
          generatorConfiguration.javaDocSeed.parserContext.TypeSolver,
          components.InvolvedClasses,
          environment, candidate);

      for (final var stmt : stmts.getStatements()) {

        final var statement = parseInMethodContext(symbolResolver, typeSolver, javaParser, stmt);
        final var expression = statement.asExpressionStmt().getExpression();

        // System.out.printf("Compiling expression: %s of kind %s\n", expression.toString(), expression.getClass().getName());

        // System.out.println("parsed expression: " + expression.toString());
        final var instructionExpression = compiler.compile(expression);
        if (instructionExpression == null) {
          // System.out.println("results: noop");
          continue;
        }
        // System.out.printf("%s\n", instructionExpression.toString());
        var convertedExpression = new ExpressionStatement(instructionExpression);
        candidate.Block.Statements.add(convertedExpression);

      }
      if (candidate.Block.Statements.isEmpty()) {
        LOGGER.log(Level.WARNING, "getting empty solution!");
      }
      @SuppressWarnings("unchecked")
      Candidate res = (Candidate) candidate;
      return res;

    } catch (final Exception e) {
      LOGGER.log(Level.INFO, "Exception was thrown during code processing", e);
      throw new NoSuchElementException(e.getMessage());
    }
  }
}
