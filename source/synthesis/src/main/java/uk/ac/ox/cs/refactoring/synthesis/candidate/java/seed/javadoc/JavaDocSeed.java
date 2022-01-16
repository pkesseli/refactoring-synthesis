package uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.javadoc;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ParserConfiguration.LanguageLevel;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.comments.CommentsCollection;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.javadoc.Javadoc;
import com.github.javaparser.javadoc.JavadocBlockTag;
import com.github.javaparser.javadoc.description.JavadocDescription;
import com.github.javaparser.javadoc.description.JavadocInlineTag;
import com.github.javaparser.resolution.SymbolResolver;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ClassLoaderTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.ox.cs.refactoring.classloader.JavaLanguage;
import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.ComponentDirectory;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaComponents;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.Invoke;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.methods.MethodIdentifier;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.methods.Methods;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.context.InstructionSetSeed;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.type.TypeFactory;

/** Adds instructions based on hints in JavaDoc. */
public class JavaDocSeed implements InstructionSetSeed {

  /** Sink for errors about accessing the source files. */
  private static final Logger logger = LoggerFactory.getLogger(JavaDocSeed.class);

  /** Used to load pre-configured classes. */
  private final ClassLoader classLoader;

  /** Method to be replaced. */
  private final MethodIdentifier methodToRefactor;

  /** Source directories and ZIP files. */
  private final List<Path> sourceContainers = new ArrayList<>();

  /**
   * @param classLoader      {@link #classLoader}
   * @param methodIdentifier {@link #methodToRefactor}
   */
  public JavaDocSeed(final ClassLoader classLoader, final MethodIdentifier methodIdentifier) {
    this.classLoader = classLoader;
    this.methodToRefactor = methodIdentifier;
    final Path javaHome = Paths.get(SystemUtils.JAVA_HOME);
    final Path srcZip = javaHome.resolve("lib/src.zip");
    if (Files.exists(srcZip)) {
      sourceContainers.add(srcZip);
    }
  }

  @Override
  public void seed(final ComponentDirectory components)
      throws ClassNotFoundException, IllegalAccessException, NoSuchFieldException, NoSuchMethodException {
    final String className = methodToRefactor.FullyQualifiedClassName;
    final ParserConfiguration parserConfiguration = new ParserConfiguration();
    parserConfiguration.setLanguageLevel(LanguageLevel.JAVA_15);
    final CombinedTypeSolver typeSolver = new CombinedTypeSolver(new ReflectionTypeSolver(),
        new ClassLoaderTypeSolver(classLoader));
    typeSolver.setExceptionHandler(SecurityException.class::isInstance);
    final JavaSymbolSolver symbolResolver = new JavaSymbolSolver(typeSolver);
    parserConfiguration.setSymbolResolver(symbolResolver);
    final JavaParser javaParser = new JavaParser(parserConfiguration);
    final ParseResult<CompilationUnit> parseResult;
    try {
      parseResult = findSource(javaParser, className);
    } catch (final IOException e) {
      logger.error("Could not access source code for class {}", className, e);
      return;
    }

    final MethodDeclaration method = findMethod(symbolResolver, typeSolver, parseResult);
    if (method == null)
      return;
    final Optional<Javadoc> javadocComment = method.getJavadoc();
    if (!javadocComment.isPresent())
      return;

    final Javadoc javadoc = javadocComment.get();
    final String deprecatedCodeExample = getDeprecatedCodeExample(javadoc);
    final JavaComponents javaComponents = new JavaComponents(components);
    if (deprecatedCodeExample != null) {
      final Expression expression = parseInMethodContext(symbolResolver, typeSolver, javaParser, parseResult, method,
          deprecatedCodeExample);
      final ResolvedType resolvedType = expression.calculateResolvedType();
      final Type type = TypeFactory.create(javaParser, resolvedType);

      final SnippetComponent snippetComponent = new SnippetComponent(classLoader, javaParser, typeSolver, expression,
          components.InvolvedClasses);
      javaComponents.nonnull(type, snippetComponent);
    }

    final String deprecatedLink = getLink(javadoc);
    if (deprecatedLink == null)
      return;

    final StringBuilder code = new StringBuilder(deprecatedLink.replace('#', JavaLanguage.PACKAGE_SEPARATOR).trim());
    if (code.charAt(0) == '.') {
      code.deleteCharAt(0);
    }
    final Expression expression = parseInMethodContext(symbolResolver, typeSolver, javaParser, parseResult, method,
        code.toString());
    if (!(expression instanceof MethodCallExpr))
      return;
    final MethodCallExpr methodCall = (MethodCallExpr) expression;
    final ResolvedMethodDeclaration methodDeclaration = methodCall.resolve();
    final String fullyQualifiedClassName = methodDeclaration.declaringType().getQualifiedName();
    final String methodName = methodDeclaration.getName();
    final List<String> fullyQualifiedParameterClassNames = new ArrayList<>();
    for (int i = 0; i < methodDeclaration.getNumberOfParams(); ++i) {
      final String type = methodDeclaration.getParam(i).getType().describe();
      fullyQualifiedParameterClassNames.add(type);
    }
    final Method methodToRegister = Methods.getMethod(classLoader,
        new MethodIdentifier(fullyQualifiedClassName, methodName, fullyQualifiedParameterClassNames));
    Invoke.register(javaComponents, methodToRegister);
  }

  /**
   * Locates the {@link MethodDeclaration} for {@link #methodToRefactor} in a
   * parsed {@link CompilationUnit}.
   * 
   * @param symbolResolver Used to check whether a type matches
   *                       {@link #methodToRefactor}.
   * @param typeSolver     Used to compare types and signatures against
   *                       {@link #methodToRefactor}.
   * @param parseResult    Parsed compilation unit in which to search.
   * @return {@link MethodDeclaration} for {@link #methodToRefactor}, if
   *         available. {@code null} otherwise.
   */
  private MethodDeclaration findMethod(final SymbolResolver symbolResolver, final TypeSolver typeSolver,
      final ParseResult<CompilationUnit> parseResult) {
    return parseResult.getResult().stream().map(CompilationUnit::getTypes).flatMap(Collection::stream)
        .filter(new MatchesMethodIdentifier(symbolResolver, typeSolver, methodToRefactor))
        .map(TypeDeclaration::getMethods).flatMap(Collection::stream)
        .filter(new MatchesSignature(typeSolver, methodToRefactor)).findAny().orElse(null);
  }

  /**
   * Parses a code snippet extracted from JavaDoc as if it were a satement in the
   * documented method. This helps resolve unqualified names, as JavaDoc code
   * examples are often written as though in the scope of the documented method.
   * 
   * @param symbolResolver  {@link #findMethod(SymbolResolver, TypeSolver, ParseResult)}
   * @param typeSolver      {@link #findMethod(SymbolResolver, TypeSolver, ParseResult)}
   * @param javaParser      {@link Typecheck}
   * @param compilationUnit Compilation unit which is modified and parsed again.
   * @param method          Method in whose context to parse {@code code}.
   * @param code            Java code snippet to parse.
   * @return JavaDoc AST expression.
   */
  private Expression parseInMethodContext(final SymbolResolver symbolResolver, final TypeSolver typeSolver,
      final JavaParser javaParser, final ParseResult<CompilationUnit> compilationUnit, final MethodDeclaration method,
      final String code) {
    final ParseResult<Expression> textExpression = javaParser.parseExpression("__RESYNTH(" + code + ")");
    method.getBody().get().addStatement(0, textExpression.getResult().get());
    final ParseResult<CompilationUnit> parseResult = javaParser.parse(compilationUnit.getResult().get().toString());
    final MethodDeclaration container = findMethod(symbolResolver, typeSolver, parseResult);
    final Expression result = container.getBody().get().getStatement(0).asExpressionStmt().getExpression()
        .asMethodCallExpr().getArgument(0);
    Typecheck.apply(javaParser, typeSolver, result);
    return result;
  }

  /**
   * Provides a Javadoc link from the given {@link Javadoc} in the context of a
   * deprecated block.
   * {@link #parseInMethodContext(SymbolResolver, TypeSolver, JavaParser, ParseResult, MethodDeclaration, String)
   * somethig}
   * 
   * @param javadoc Comment in which to search.
   * @return {@link Javadoc} link reference.
   */
  private static String getLink(final Javadoc javadoc) {
    return getDeprecatedInlineTags(javadoc).filter(tag -> JavadocInlineTag.Type.LINK == tag.getType())
        .map(JavadocInlineTag::getContent).map(Links::getLink).findAny().orElse(null);
  }

  /**
   * Extracts a code example in the context of a deprecated block tag.
   * 
   * @param javadoc Comment from which to extract the example.
   * @return Code example to replace the method call.
   */
  private static String getDeprecatedCodeExample(final Javadoc javadoc) {
    return getDeprecatedInlineTags(javadoc)
        .filter(tag -> JavadocInlineTag.Type.CODE == tag.getType()).map(JavadocInlineTag::getContent)
        .findAny().orElse(null);
  }

  /**
   * Provides all {@link JavadocInlineTag} in deprecated block tags in a
   * {@link Javadoc}.
   * 
   * @param javadoc {@link Javadoc} in which to search.
   * @return {@link Stream} of matching {@link JavadocInlineTag}.
   */
  private static Stream<JavadocInlineTag> getDeprecatedInlineTags(final Javadoc javadoc) {
    return javadoc.getBlockTags().stream().filter(b -> JavadocBlockTag.Type.DEPRECATED == b.getType())
        .map(JavadocBlockTag::getContent).map(JavadocDescription::getElements).flatMap(Collection::stream)
        .filter(JavadocInlineTag.class::isInstance).map(JavadocInlineTag.class::cast);
  }

  /**
   * Finds and parses the source file for the given class name.
   * 
   * @param javaParser Parser to use for parsing the source.
   * @param className  Class whose source to locate.
   * @return Parsed compilation unit, if available.
   * @throws IOException if file access fails.
   */
  private ParseResult<CompilationUnit> findSource(final JavaParser javaParser, final String className)
      throws IOException {
    final String relativePath = className.replace(String.valueOf(JavaLanguage.PACKAGE_SEPARATOR), "[\\/]");
    final Pattern pattern = Pattern.compile(".*" + relativePath + ".java");
    for (final Path sourceContainer : sourceContainers) {
      if (Files.isRegularFile(sourceContainer)) {
        try (final ZipFile zipFile = new ZipFile(sourceContainer.toFile())) {
          for (final ZipEntry entry : Collections.list(zipFile.entries())) {
            if (pattern.matcher(entry.getName()).matches()) {
              try (final InputStream is = zipFile.getInputStream(entry)) {
                return javaParser.parse(is);
              }
            }
          }
        }
      }
    }
    return new ParseResult<CompilationUnit>(null, Collections.emptyList(), new CommentsCollection());
  }

}
