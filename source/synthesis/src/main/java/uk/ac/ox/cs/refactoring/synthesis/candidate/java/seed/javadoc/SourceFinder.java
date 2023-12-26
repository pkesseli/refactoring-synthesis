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
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.core.resolution.Context;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFactory;
import com.github.javaparser.symbolsolver.javaparsermodel.contexts.ClassOrInterfaceDeclarationContext;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;

import uk.ac.ox.cs.refactoring.classloader.JavaLanguage;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.methods.MethodIdentifier;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.parser.ParserContext;

/**
 * Literally a copy of {@link JavaDocSeed} but with fields and methods made public.
 * TODO: replace it with {@link JavaDocSeed}
 */
public class SourceFinder {

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
