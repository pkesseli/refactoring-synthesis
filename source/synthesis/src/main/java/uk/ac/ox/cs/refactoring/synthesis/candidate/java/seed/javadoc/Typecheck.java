package uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.javadoc;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Optional;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.TypeExpr;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;

import uk.ac.ox.cs.refactoring.synthesis.candidate.java.type.TypeFactory;

/**
 * Amends ambiguity introduced by the more generous JavaDoc grammar in our
 * JavaParser fork. Specifically, our grammar allows expressions such as
 * {@code setSelectedCheckbox(Checkbox)}. In the context of
 * {@code java.awt.CheckboxGroup}, the parameter {@code Checkbox} is intended as
 * a type name, not a symbol. Our parser allows for this, but is unable to
 * discern that {@code Checkbox} must be type rather than a symbol expression
 * without a symbol table. This class applies this differentiation, preferring
 * symbol expressions over types if both are legal.
 */
class Typecheck extends VoidVisitorAdapter<Void> {

  /** Used to convert a symbol expression to a type. */
  private final JavaParser javaParser;

  /** Used to derive expected types in expressions when fixing ambiguity. */
  private final JavaParserFacade facade;

  /**
   * Type to use if an encountered symbol cannot be linked to a a type. As an
   * example, if a JavaDoc example uses an undeclared variable {@code clazz},
   * we would assign it this default type, since "clazz" does not match any type
   * name. Usually, this would be the type containing the method, assuming that
   * undeclared variables in JavaDoc are instances of the type they are in.
   */
  private final Type defaultType;

  /** Node replacements to perform for type correctness. */
  private final Map<Node, Map<Node, Node>> replacements = new IdentityHashMap<>();

  /**
   * @param javaParser {@link #javaParser}
   * @param typeSolver {@link JavaParserFacade}
   */
  private Typecheck(final JavaParser javaParser, final TypeSolver typeSolver, final Type defaultType) {
    this.javaParser = javaParser;
    facade = JavaParserFacade.get(typeSolver);
    this.defaultType = defaultType;
  }

  /**
   * Resolves ambiguity introduced by JavaDoc grammar.
   * 
   * @param javaParser  {@link #Typecheck(JavaParser, TypeSolver, Type)}
   * @param typeSolver  {@link #Typecheck(JavaParser, TypeSolver, Type)}
   * @param defaultType {@link #Typecheck(JavaParser, TypeSolver, Type)}
   * @param expression  Expression whose types to repair.
   */
  public static void apply(final JavaParser javaParser, final TypeSolver typeSolver, final Type defaultType,
      final Expression expression) {
    final Typecheck typecheck = new Typecheck(javaParser, typeSolver, defaultType);
    expression.accept(typecheck, null);
    for (final Map.Entry<Node, Map<Node, Node>> entry : typecheck.replacements.entrySet()) {
      final Node parent = entry.getKey();
      final Map<Node, Node> replacementsForParent = entry.getValue();
      for (final Map.Entry<Node, Node> replacement : replacementsForParent.entrySet())
        parent.replace(replacement.getKey(), replacement.getValue());
    }
  }

  @Override
  public void visit(final NameExpr n, final Void arg) {
    super.visit(n, arg);
    if (canBeResolved(n))
      return;

    final Node parent = n.getParentNode().get();
    final Map<Node, Node> replacementsForParent = replacements.computeIfAbsent(parent, p -> new IdentityHashMap<>());
    replacementsForParent.put(n, new TypeExpr(getType(n)));
  }

  /**
   * Maps the given name to a type. Will manually resolve well-known, unambiguous
   * where the GitHub JavaParser fails.
   * 
   * TODO: Not being able to resolve "Graphics2D" to "java.awt.Graphics2D" despite
   * "java.awt.*" being imported is an issue in JavaParsers which is likely
   * resolved in more recent versions. We should upgrade the library in follow-up
   * projects.
   * 
   * @param n Name to resolve to a type.
   * @return Resolved type if available, otherwise {@link #defaultType}.
   */
  private Type getType(final NameExpr n) {
    final ResolvedType resolvedType = getResolvedType(n);
    if (resolvedType != null)
      return TypeFactory.create(javaParser, resolvedType);

    if ("Graphics2D".equals(n.getNameAsString())) {
      final Optional<Type> parseResult = javaParser.parseType("java.awt.Graphics2D").getResult();
      if (parseResult.isPresent())
        return parseResult.get();
    }
    return defaultType;
  }

  /**
   * Uses {@link #facade} to provide a {@link ResolvedType} for the given name.
   * Will never throw.
   * 
   * @param n Name to resolve.
   * @return Type provided by {@code #facade} or {@code null} on failure.
   */
  private ResolvedType getResolvedType(final NameExpr n) {
    try {
      return facade.getType(n);
    } catch (final Throwable e) {
      return null;
    }
  }

  /**
   * Indicates whether {@link Expression#calculateResolvedType()} in
   * {@link JavaDocSeed} would succeed.
   * 
   * @param expression {@link Expression} to verify.
   * @return {@code true} if no ambiguity is present, {@code false} othwerwise.
   */
  private boolean canBeResolved(final Expression expression) {
    try {
      facade.getType(expression, false);
    } catch (final Throwable e) {
      return false;
    }
    return true;
  }
}
