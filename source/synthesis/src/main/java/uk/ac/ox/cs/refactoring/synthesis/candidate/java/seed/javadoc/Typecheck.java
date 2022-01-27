package uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.javadoc;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.TypeExpr;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.UnsolvedSymbolException;
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
  private final Map<Map.Entry<Node, Node>, Node> replacements = new HashMap<>();

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
    for (final Map.Entry<Map.Entry<Node, Node>, Node> entry : typecheck.replacements.entrySet()) {
      final Map.Entry<Node, Node> parentAndNodeToReplace = entry.getKey();
      final Node parent = parentAndNodeToReplace.getKey();
      final Node nodeToReplace = parentAndNodeToReplace.getValue();
      final Node newNode = entry.getValue();
      parent.replace(nodeToReplace, newNode);
    }
  }

  @Override
  public void visit(final NameExpr n, final Void arg) {
    super.visit(n, arg);
    if (canBeResolved(n))
      return;

    final Node parent = n.getParentNode().get();
    final Map.Entry<Node, Node> key = new AbstractMap.SimpleImmutableEntry<>(parent, n);
    replacements.put(key, new TypeExpr(getType(n)));
  }

  private Type getType(final NameExpr n) {
    final ResolvedType resolvedType;
    try {
      resolvedType = facade.getType(n);
    } catch (final UnsolvedSymbolException e) {
      return defaultType;
    }

    return TypeFactory.create(javaParser, resolvedType);
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
    } catch (final UnsolvedSymbolException e) {
      return false;
    }
    return true;
  }
}
