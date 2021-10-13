package uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ParserConfiguration.LanguageLevel;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.javadoc.Javadoc;
import com.github.javaparser.javadoc.JavadocBlockTag;
import com.github.javaparser.javadoc.description.JavadocDescription;
import com.github.javaparser.javadoc.description.JavadocInlineTag;
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
import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.FunctionComponent;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.IExpression;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.BoundInvokableFactory;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.InvokeMethodFactory;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaComponents;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaLanguageKey;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaLanguageKeys;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.InvokeMethod;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.Literal;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.methods.MethodIdentifier;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.type.TypeFactory;

/**
 * Adds instructions based on hints in JavaDoc.
 * 
 * TODO: Implement!
 */
public class JavaDocSeed implements InstructionSetSeed {

  /** */
  private static final Logger logger = LoggerFactory.getLogger(JavaDocSeed.class);

  /**
   * Used to load pre-configured classes.
   */
  private final ClassLoader classLoader;

  /**
   * Method to be replaced.
   */
  private final MethodIdentifier methodToRefactor;

  /** */
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

    final MethodDeclaration method = findMethod(typeSolver, parseResult);
    if (method == null)
      return;
    final Optional<Javadoc> javadoc = method.getJavadoc();
    if (!javadoc.isPresent())
      return;

    final String deprecatedCodeExample = getDeprecatedCodeExample(javadoc.get());
    if (deprecatedCodeExample == null)
      return;

    final Expression expression = parseInMethodContext(typeSolver, javaParser, parseResult, method,
        deprecatedCodeExample);

    final Class<?> calendar = classLoader.loadClass("java.util.Calendar");
    final Method get = calendar.getDeclaredMethod("get", int.class);
    final InvokeMethodFactory invokeFactory = new InvokeMethodFactory(get);
    final Map<Integer, IExpression> boundArguments = new HashMap<>();
    final Field hours = calendar.getDeclaredField("HOUR_OF_DAY");
    final Object value = hours.get(null);
    final Type type = PrimitiveType.intType();
    boundArguments.put(1, new Literal(value, type));
    final BoundInvokableFactory<InvokeMethod> bound = new BoundInvokableFactory<>(invokeFactory, boundArguments);

    final JavaComponents javaComponents = new JavaComponents(components);
    final Type resultType = TypeFactory.create(get.getReturnType());
    final List<JavaLanguageKey> parameterKeys = Arrays
        .asList(JavaLanguageKeys.nonnull(TypeFactory.createClassType(calendar)));
    javaComponents.nonnull(resultType, new FunctionComponent<>(parameterKeys, bound));
  }

  private MethodDeclaration findMethod(final TypeSolver typeSolver, final ParseResult<CompilationUnit> parseResult) {
    return parseResult.getResult().stream().map(CompilationUnit::getTypes).flatMap(Collection::stream)
        .filter(new MatchesMethodIdentifier(typeSolver, methodToRefactor)).map(TypeDeclaration::getMethods)
        .flatMap(Collection::stream).filter(new MatchesSignature(typeSolver, methodToRefactor)).findAny().orElse(null);
  }

  private Expression parseInMethodContext(final TypeSolver typeSolver, final JavaParser javaParser,
      final ParseResult<CompilationUnit> compilationUnit, final MethodDeclaration method, final String code) {
    final ParseResult<Expression> textExpression = javaParser.parseExpression(code);
    method.getBody().get().addStatement(0, textExpression.getResult().get());
    final ParseResult<CompilationUnit> parseResult = javaParser.parse(compilationUnit.getResult().get().toString());
    final MethodDeclaration container = findMethod(typeSolver, parseResult);
    return container.getBody().get().getStatement(0).asExpressionStmt().getExpression();
  }

  private static String getDeprecatedCodeExample(final Javadoc javadoc) {
    return javadoc.getBlockTags().stream().map(JavadocBlockTag::getContent).map(JavadocDescription::getElements)
        .flatMap(Collection::stream).filter(JavadocInlineTag.class::isInstance).map(JavadocInlineTag.class::cast)
        .map(JavadocInlineTag::getContent).findAny().orElse(null);
  }

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
    return null;
  }

}
