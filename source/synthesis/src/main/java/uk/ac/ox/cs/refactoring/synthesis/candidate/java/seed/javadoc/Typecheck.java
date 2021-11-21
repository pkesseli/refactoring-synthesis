package uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.javadoc;

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

  /** Node replacements to perform for type correctness. */
  private final Map<Node, Node> replacements = new HashMap<>();

  /**
   * @param javaParser {@link #javaParser}
   * @param typeSolver {@link JavaParserFacade}
   */
  private Typecheck(final JavaParser javaParser, final TypeSolver typeSolver) {
    this.javaParser = javaParser;
    facade = JavaParserFacade.get(typeSolver);
  }

  /**
   * Resolves ambiguity introduced by JavaDoc grammar.
   * 
   * @param javaParser {@link #Typecheck(JavaParser, TypeSolver)}
   * @param typeSolver {@link #Typecheck(JavaParser, TypeSolver)}
   * @param expression Expression whose types to repair.
   */
  public static void apply(final JavaParser javaParser, final TypeSolver typeSolver, final Expression expression) {
    final Typecheck typecheck = new Typecheck(javaParser, typeSolver);
    expression.accept(typecheck, null);
    for (final Map.Entry<Node, Node> entry : typecheck.replacements.entrySet()) {
      expression.replace(entry.getKey(), entry.getValue());
    }
  }

  @Override
  public void visit(final NameExpr n, final Void arg) {
    super.visit(n, arg);
    if (canBeResolved(n))
      return;

    final Type type = TypeFactory.create(javaParser, facade.getType(n));
    replacements.put(n, new TypeExpr(type));
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
