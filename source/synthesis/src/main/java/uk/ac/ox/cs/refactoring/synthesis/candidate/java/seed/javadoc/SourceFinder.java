package uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.javadoc;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.comments.CommentsCollection;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.javadoc.Javadoc;
import com.github.javaparser.javadoc.JavadocBlockTag;
import com.github.javaparser.javadoc.description.JavadocDescription;
import com.github.javaparser.javadoc.description.JavadocInlineTag;
import com.github.javaparser.resolution.SymbolResolver;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.core.resolution.Context;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFactory;
import com.github.javaparser.symbolsolver.javaparsermodel.contexts.ClassOrInterfaceDeclarationContext;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;

import uk.ac.ox.cs.refactoring.classloader.JavaLanguage;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.methods.MethodIdentifier;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.methods.MethodIdentifiers;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.methods.Methods;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.parser.ParserContext;

/** Literally a copy of {@link JavaDocSeed} but with fields and methods made public */
public class SourceFinder {

  /** Sink for errors about accessing the source files. */
  private static final Logger logger = LoggerFactory.getLogger(JavaDocSeed.class);

  /** Used to parse JavaDoc hints. */
  public final ParserContext parserContext;

  /** Used to load pre-configured classes. */
  public final ClassLoader classLoader;

  /** Method to be replaced. */
  public final MethodIdentifier methodToRefactor;

  /** Source directories and ZIP files. */
  public final List<Path> sourceContainers = new ArrayList<>();

  /**
   * @param classLoader      {@link #classLoader}
   * @param methodIdentifier {@link #methodToRefactor}
   */
  public SourceFinder(final ParserContext parserContext, final ClassLoader classLoader,
      final MethodIdentifier methodIdentifier) {
    this.parserContext = parserContext;
    this.classLoader = classLoader;
    this.methodToRefactor = methodIdentifier;
    final Path javaHome = Paths.get(SystemUtils.JAVA_HOME);
    final Path srcZip = javaHome.resolve("lib/src.zip");
    if (Files.exists(srcZip)) {
      sourceContainers.add(srcZip);
    }
  }

  public static boolean isEqual(final ResolvedMethodDeclaration resolvedMethodDeclaration,
      final MethodDeclaration methodDeclaration) {
    final ResolvedMethodDeclaration other;
    try {
      other = methodDeclaration.resolve();
    } catch (final Exception e) {
      return false;
    }

    return resolvedMethodDeclaration.getQualifiedSignature().equals(other.getQualifiedSignature());
  }

//   @Override
//   public void seed(final ComponentDirectory components)
//       throws ClassNotFoundException, IllegalAccessException, NoSuchFieldException, NoSuchMethodException {
//     final String className = methodToRefactor.FullyQualifiedClassName;
//     final CombinedTypeSolver typeSolver = parserContext.TypeSolver;
//     final JavaSymbolSolver symbolResolver = parserContext.SymbolResolver;
//     final JavaParser javaParser = parserContext.JavaParser;
//     final ParseResult<CompilationUnit> parseResult;
//     try {
//       parseResult = findSource(javaParser, className);
//     } catch (final IOException e) {
//       logger.error("Could not access source code for class {}", className, e);
//       return;
//     }

//     final MethodDeclaration method = findMethod(symbolResolver, typeSolver, parseResult);
//     if (method == null)
//       return;
//     final Optional<Javadoc> javadocComment = method.getJavadoc();
//     if (!javadocComment.isPresent())
//       return;

//     final Type defaultType = TypeFactory.createClassType(ClassLoaders.loadClass(classLoader, className));
//     final Javadoc javadoc = javadocComment.get();
//     final JavaComponents javaComponents = new JavaComponents(components);
//     final Expression deprecatedCodeExample = parseDeprecatedCodeExample(symbolResolver, typeSolver, javaParser,
//         defaultType, parseResult, method, javadoc);
//     if (deprecatedCodeExample != null) {
//       try {
//         final ResolvedType resolvedType = deprecatedCodeExample.calculateResolvedType();
//         final Type type = TypeFactory.create(javaParser, resolvedType);

//         final SnippetComponent snippetComponent = new SnippetComponent(classLoader, javaParser, typeSolver,
//             deprecatedCodeExample, components.InvolvedClasses);
//         if (!snippetComponent.isUnresolved())
//           javaComponents.nonnull(type, snippetComponent);
//       } catch (final RuntimeException e) {
//         logger.warn("Could not parse JavaDoc code example.", e);

//         if (deprecatedCodeExample instanceof MethodCallExpr) {
//           final MethodCallExpr methodCallExpr = (MethodCallExpr) deprecatedCodeExample;
//           final ResolvedReferenceTypeDeclaration resolvedReferenceTypeDeclaration = getDeclaringClass(methodCallExpr,
//               typeSolver);
//           if (resolvedReferenceTypeDeclaration != null) {
//             final Set<ResolvedMethodDeclaration> methods = resolvedReferenceTypeDeclaration.getDeclaredMethods()
//                 .stream()
//                 .filter(new MatchesMethodName(methodCallExpr.getNameAsString())).collect(Collectors.toSet());
//             if (methods.size() == 1) {
//               final ResolvedMethodDeclaration methodInCodeTag = IterableUtils.first(methods);
//               if (!isEqual(methodInCodeTag, method)) {
//                 register(classLoader, javaComponents, methodInCodeTag);
//                 return;
//               }
//             }
//           }
//         }
//       }
//     }

//     final Set<String> deprecatedLinks = getLink(javadoc);
//     for (final String deprecatedLink : deprecatedLinks) {
//       final StringBuilder code = new StringBuilder(deprecatedLink.replace('#', JavaLanguage.PACKAGE_SEPARATOR).trim());
//       if (code.charAt(0) == '.') {
//         code.deleteCharAt(0);
//       }

//       final String codeWithParametersAsExpressions = code.toString();
//       ResolvedMethodDeclaration resolvedMethodDeclaration = getResolvedMethodDeclarationFromLink(symbolResolver,
//           typeSolver, javaParser, defaultType, parseResult, method, codeWithParametersAsExpressions);
//       if (resolvedMethodDeclaration == null) {
//         String codeAsType = code.toString().replaceAll("([,)])", " raw_param$1");
//         int paramIndex = 0;
//         String previous = "";
//         while (!codeAsType.equals(previous)) {
//           previous = codeAsType;
//           codeAsType = codeAsType.replaceFirst("raw_param", "param" + paramIndex);
//         }
//         resolvedMethodDeclaration = getResolvedMethodDeclarationFromLink(symbolResolver, typeSolver, javaParser,
//             defaultType, parseResult, method, codeAsType);
//       }
//       if (resolvedMethodDeclaration != null) {
//         register(classLoader, javaComponents, resolvedMethodDeclaration);
//         break;
//       }
//     }
//   }

  // public ResolvedMethodDeclaration getResolvedMethodDeclarationFromLink(final JavaSymbolSolver symbolResolver,
  //     final CombinedTypeSolver typeSolver, final JavaParser javaParser, final Type defaultType,
  //     final ParseResult<CompilationUnit> parseResult, final MethodDeclaration method, final String code) {
  //   final Expression expression;
  //   try {
  //     expression = parseInMethodContext(symbolResolver, typeSolver, javaParser, defaultType, parseResult, method, code);
  //   } catch (final Exception e) {
  //     logger.warn("Could not identify method linked in JavaDoc", e);
  //     return null;
  //   }

  //   if (!(expression instanceof MethodCallExpr))
  //     return null;
  //   final MethodCallExpr methodCall = (MethodCallExpr) expression;
  //   try {
  //     return methodCall.resolve();
  //   } catch (final Exception e) {
  //     logger.warn("Could not resolved method linked in JavaDoc", e);
  //     return null;
  //   }
  // }

//   /**
//    * Registers a generic method instruction in {@code javaComponents}.
//    * 
//    * @param classLoader       {@link Methods#getMethod(ClassLoader, MethodIdentifier)}
//    * @param javaComponents    {@link JavaComponents} to extend.
//    * @param methodDeclaration Method to add.
//    * @throws ClassNotFoundException {@link Methods#getMethod(ClassLoader, MethodIdentifier)}
//    * @throws NoSuchMethodException  {@link Methods#getMethod(ClassLoader, MethodIdentifier)}
//    */
//   private static void register(final ClassLoader classLoader, final JavaComponents javaComponents,
//       final ResolvedMethodDeclaration methodDeclaration) throws ClassNotFoundException, NoSuchMethodException {
//     final Method methodToRegister = Methods.getMethod(classLoader, MethodIdentifiers.create(methodDeclaration));
//     Invoke.register(javaComponents, methodToRegister);
//   }

  /**
   * Resolves the class which declares the called method.
   * 
   * @param methodCall {@link MethodCallExpr} whose declaring class to resolve.
   * @param typeSolver Used for resolution.
   * @return Declaring type or {@code null}, if not found.
   */
  public static ResolvedReferenceTypeDeclaration getDeclaringClass(final MethodCallExpr methodCall,
      final TypeSolver typeSolver) {
    final Optional<Expression> scope = methodCall.getScope();
    if (scope.isPresent()) {
      final Context context = JavaParserFactory.getContext(scope.get(), typeSolver);
      if (context instanceof ClassOrInterfaceDeclarationContext) {
        return getDeclaration((ClassOrInterfaceDeclarationContext) context);
      }

      final ResolvedType type = scope.get().calculateResolvedType();
      if (!(type instanceof ResolvedReferenceType))
        return null;

      final ResolvedReferenceType resolvedReferenceType = (ResolvedReferenceType) type;
      return resolvedReferenceType.getTypeDeclaration().get();
    } else {
      Context tmp = JavaParserFactory.getContext(methodCall, typeSolver);
      while (!(tmp instanceof ClassOrInterfaceDeclarationContext)) {
        final Optional<Context> parent = tmp.getParent();
        if (!parent.isPresent()) {
          return null;
        }
        tmp = parent.get();
      }

      if (tmp instanceof ClassOrInterfaceDeclarationContext)
        return getDeclaration((ClassOrInterfaceDeclarationContext) tmp);
    }

    return null;
  }

  /**
   * {@link ClassOrInterfaceDeclarationContext#getDeclaration}
   * 
   * @param context {@link ClassOrInterfaceDeclarationContext#getDeclaration}
   * @return {@link ClassOrInterfaceDeclarationContext#getDeclaration}
   */
  private static ResolvedReferenceTypeDeclaration getDeclaration(final ClassOrInterfaceDeclarationContext context) {
    try {
      final Method method = ClassOrInterfaceDeclarationContext.class.getDeclaredMethod("getDeclaration");
      method.setAccessible(true);
      return (ResolvedReferenceTypeDeclaration) method.invoke(context);
    } catch (final IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      throw new IllegalStateException("JavaParser `ClassOrInterfaceDeclarationContext` API changed.", e);
    }
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
  public MethodDeclaration findMethod(final SymbolResolver symbolResolver, final TypeSolver typeSolver,
      final ParseResult<CompilationUnit> parseResult) {
    return parseResult.getResult().stream().map(CompilationUnit::getTypes).flatMap(Collection::stream)
        .flatMap(SourceFinder::expandInnerTypes)
        .filter(new MatchesMethodIdentifier(symbolResolver, typeSolver, methodToRefactor))
        .map(TypeDeclaration::getMethods).flatMap(Collection::stream)
        .filter(new SimplyMatchesSignature(typeSolver, methodToRefactor)).findAny().get();
  }

  private static Stream<TypeDeclaration<?>> expandInnerTypes(final TypeDeclaration<?> type) {
    return Stream.concat(Stream.of(type),
        type.getMembers().stream().filter(TypeDeclaration.class::isInstance).map(TypeDeclaration.class::cast)
            .flatMap(SourceFinder::expandInnerTypes));
  }

  // /**
  //  * Parses a code snippet extracted from JavaDoc as if it were a satement in the
  //  * documented method. This helps resolve unqualified names, as JavaDoc code
  //  * examples are often written as though in the scope of the documented method.
  //  * 
  //  * @param symbolResolver  {@link #findMethod(SymbolResolver, TypeSolver, ParseResult)}
  //  * @param typeSolver      {@link #findMethod(SymbolResolver, TypeSolver, ParseResult)}
  //  * @param javaParser      {@link Typecheck}
  //  * @param defaultType     {@link TypeCheck}
  //  * @param compilationUnit Compilation unit which is modified and parsed again.
  //  * @param method          Method in whose context to parse {@code code}.
  //  * @param code            Java code snippet to parse.
  //  * @return JavaDoc AST expression.
  //  */
  // public Expression parseInMethodContext(final SymbolResolver symbolResolver, final TypeSolver typeSolver,
  //     final JavaParser javaParser, final Type defaultType, final ParseResult<CompilationUnit> compilationUnit,
  //     final MethodDeclaration method, final String code) {
  //   final ParseResult<Expression> textExpression = javaParser.parseExpression("__RESYNTH(" + code + ")");
  //   method.getBody().get().addStatement(0, textExpression.getResult().get());
  //   final ParseResult<CompilationUnit> parseResult = javaParser.parse(compilationUnit.getResult().get().toString());
  //   final MethodDeclaration container = findMethod(symbolResolver, typeSolver, parseResult);
  //   final Expression result = container.getBody().get().getStatement(0).asExpressionStmt().getExpression()
  //       .asMethodCallExpr().getArgument(0);
  //   Typecheck.apply(javaParser, typeSolver, defaultType, result);
  //   return result;
  // }


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
    // method.getBody().get().addStatement(0, statement);
    final ParseResult<CompilationUnit> parseResult = javaParser.parse(compilationUnit.getResult().get().toString());
    final MethodDeclaration container = findMethod(symbolResolver, typeSolver, parseResult);
    final Statement result = container.getBody().get().getStatement(0);
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
  public static Set<String> getLink(final Javadoc javadoc) {
    return getDeprecatedInlineTags(javadoc).filter(tag -> JavadocInlineTag.Type.LINK == tag.getType())
        .map(JavadocInlineTag::getContent).map(Links::getLink).collect(Collectors.toSet());
  }

  // /**
  //  * Extracts a code example in the context of a deprecated block tag.
  //  * 
  //  * @param symbolResolver  {@link #parseInMethodContext(SymbolResolver, TypeSolver, JavaParser, Type, ParseResult, MethodDeclaration, String)}
  //  * @param typeSolver      {@link #parseInMethodContext(SymbolResolver, TypeSolver, JavaParser, Type, ParseResult, MethodDeclaration, String)}
  //  * @param javaParser      {@link #parseInMethodContext(SymbolResolver, TypeSolver, JavaParser, Type, ParseResult, MethodDeclaration, String)}
  //  * @param defaultType     {@link #parseInMethodContext(SymbolResolver, TypeSolver, JavaParser, Type, ParseResult, MethodDeclaration, String)}
  //  * @param compilationUnit {@link #parseInMethodContext(SymbolResolver, TypeSolver, JavaParser, Type, ParseResult, MethodDeclaration, String)}
  //  * @param method          {@link #parseInMethodContext(SymbolResolver, TypeSolver, JavaParser, Type, ParseResult, MethodDeclaration, String)}
  //  * @param javadoc         Comment from which to extract the example.
  //  * @return Code example to replace the method call.
  //  */
  // public Expression parseDeprecatedCodeExample(final SymbolResolver symbolResolver, final TypeSolver typeSolver,
  //     final JavaParser javaParser, final Type defaultType, final ParseResult<CompilationUnit> compilationUnit,
  //     final MethodDeclaration method, final Javadoc javadoc) {

  //   final List<JavadocInlineTag> tags = getDeprecatedInlineTags(javadoc)
  //       .filter(tag -> JavadocInlineTag.Type.CODE == tag.getType()).collect(Collectors.toList());
  //   for (final JavadocInlineTag javadocInlineTag : tags) {
  //     final String code = javadocInlineTag.getContent();
  //     final Expression expression = parseInMethodContext(symbolResolver, typeSolver, javaParser, defaultType,
  //         compilationUnit, method, code);
  //     if (!containsDeprectedMethodCalls(classLoader, expression))
  //       return expression;
  //   }

  //   return null;
  // }

  /**
   * Indicates whether {@code expression} contains any invocations of deprecated
   * methods.
   * 
   * @param classLoader Used to reflectively load the method.
   * @param expression  {@link Expression} to check.
   * @return {@code true} if a deprecated method is invoked, {@code false}
   *         otherwise.
   */
  private static boolean containsDeprectedMethodCalls(final ClassLoader classLoader, final Expression expression) {
    final Collection<MethodCallExpr> calledMethods = expression.findAll(MethodCallExpr.class);
    for (final MethodCallExpr calledMethod : calledMethods) {
      final ResolvedMethodDeclaration resolvedMethodDeclaration;
      try {
        resolvedMethodDeclaration = calledMethod.resolve();
      } catch (final UnsolvedSymbolException e) {
        logger.warn("Could not check whether the hinted method is deprecatd.", e);
        continue;
      }
      final MethodIdentifier methodIdentifier = MethodIdentifiers.create(resolvedMethodDeclaration);
      final Method invokedMethod;
      try {
        invokedMethod = Methods.getMethod(classLoader, methodIdentifier);
      } catch (final NoSuchMethodException | ClassNotFoundException e) {
        logger.warn("Could not identify called method", e);
        continue;
      }

      if (invokedMethod.isAnnotationPresent(Deprecated.class))
        return true;
    }
    return false;
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
  public ParseResult<CompilationUnit> findSource(final JavaParser javaParser, final String className)
      throws IOException {
    final String relativePath = getRelativeSourceFilePath(className);
    final Pattern pattern = Pattern.compile(".*" + relativePath + ".java");
    for (final Path sourceContainer : sourceContainers) {
      if (Files.isRegularFile(sourceContainer)) {
        try (final ZipFile zipFile = new ZipFile(sourceContainer.toFile())) {
          for (final ZipEntry entry : Collections.list(zipFile.entries())) {
            if (pattern.matcher(entry.getName()).matches()) {
              final String content;
              try (final InputStream is = zipFile.getInputStream(entry)) {
                content = IOUtils.toString(is, javaParser.getParserConfiguration().getCharacterEncoding());
              }
              return javaParser.parse(replaceHtml(content));
            }
          }
        }
      }
    }
    return new ParseResult<CompilationUnit>(null, Collections.emptyList(), new CommentsCollection());
  }

  /**
   * Replaces Javadoc HTML syntax by modern Javadoc syndax (e.g.
   * &lt; code&gt;xyz()&lt; /code&gt; by {@code xyz()}).
   * 
   * @param content Source file content to mutate.
   * @return New source file content.
   */
  static String replaceHtml(final String content) {
    return content.replaceAll("<(code|link)>(.*?)</\\1>", "{@$1 $2}");
  }

  /**
   * Calculates the expected path relative to a source directory in which the
   * source file for the given class is expected.
   * 
   * @param className Name of the class to identify.
   * @return Relative file path to search for.
   */
  private static String getRelativeSourceFilePath(final String className) {
    final String relativePath = className.replace(String.valueOf(JavaLanguage.PACKAGE_SEPARATOR), "[\\/]");
    final int innerClassStart = relativePath.indexOf('$');
    return innerClassStart == -1 ? relativePath : relativePath.substring(0, innerClassStart);
  }

}
