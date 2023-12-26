package uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.javadoc;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.expr.ThisExpr;
import com.github.javaparser.ast.expr.TypeExpr;
import com.github.javaparser.ast.expr.UnaryExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceTypeImpl;

import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.IExpression;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.IStatement;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.SnippetCandidate;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaLanguageKey;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaLanguageKeys;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.FieldAccess;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.InvokeConstructor;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.InvokeMethod;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.Literal;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.SymbolExpression;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.This;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.VarArgsObjectArrayExpression;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.methods.MethodIdentifier;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.methods.MethodIdentifiers;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.methods.Methods;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.statement.ExpressionStatement;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.type.TypeFactory;

/**
 * Extending {@link SnippetComponentVisitor} with `this` expression
 */
class IRGenerationVisitor extends VoidVisitorAdapter<Void> {

  /** Used to look up a method by its identifier. */
  private final ClassLoader classLoader;

  /** Used to convert resolved types to regular ones. */
  private final JavaParser javaParser;

  /** Used to resolve the type of implicit {@code this} expressions. */
  private final TypeSolver typeSolver;

  /** {@link SnippetComponent#involvedClasses} */
  private final Set<String> involvedClasses;

  /** Placeholders for paramters in {@link #expression}. */
  final List<Placeholder> Parameters = new ArrayList<>();

  /**
   * Matching keys for {@link #Parameters} to identify suitable components during
   * synthesis.
   */
  final List<JavaLanguageKey> ParameterKeys = new ArrayList<>();

  /** Child expressions of {@link #expression} discovered during visiting. */
  final LinkedList<IExpression> stack = new LinkedList<>();

  /** Mapping local names to {@link SymbolExpression} */
  final Map<String, IExpression> environment;

  /**
   * Partially constructed candidate. Keep a reference here to store
   * intermediate statements
   */
  final SnippetCandidate candidate;

  /**
   * @param classLoader     {@link #classLoader}
   * @param javaParser      {@link #javaParser}
   * @param typeSolver      {@link #typeSolver}
   * @param involvedClasses {@link #involvedClasses}
   */
  IRGenerationVisitor(final ClassLoader classLoader, final JavaParser javaParser, final TypeSolver typeSolver,
      final Set<String> involvedClasses, final Map<String, IExpression> environment,
      final SnippetCandidate candidate) {
    this.classLoader = classLoader;
    this.javaParser = javaParser;
    this.typeSolver = typeSolver;
    this.involvedClasses = involvedClasses;
    this.environment = environment;
    this.candidate = candidate;
  }

  /**
   * Provides expression for the synthesis instruction set constructed from the
   * code examples.
   * 
   * @return Instruction set expression.
   */
  IExpression getExpression() {
    // TODO `null` represent noop. It is appropriate?
    if (stack.isEmpty()) {
      return null;
    } else {
      return stack.removeLast();
    }
  }

  @Override
  public void visit(final IntegerLiteralExpr n, final Void arg) {
    stack.add(new Literal(n.asNumber(), getType(n)));
  }

  @Override
  public void visit(final BooleanLiteralExpr n, final Void arg) {
    stack.add(new Literal(n.getValue(), getType(n)));
  }

  @Override
  public void visit(final StringLiteralExpr n, final Void arg) {
    // TODO
    // System.out.println(n.asString());
    stack.add(new Literal(n.asString(), getType(n), null));
    // throw new IllegalArgumentException("string literals are not supported");
  }

  @Override
  public void visit(final BinaryExpr n, final Void arg) {
    super.visit(n, arg);
    final Type type = getType(n);
    if (!type.isPrimitiveType()) {
      throw new UnsupportedOperationException();
    }

    final IExpression rhs = stack.removeLast();
    final IExpression lhs = stack.removeLast();
    final PrimitiveType primitiveType = type.asPrimitiveType();
    switch (primitiveType.getType()) {
      case INT:
        visitIntBinaryExpression(n, lhs, rhs);
        break;
      default:
        throw new UnsupportedOperationException();
    }
  }

  /**
   * Assumes that the type of {@code node} is {@code int} and puts an integer
   * binary expression on {@link #stack}.
   * 
   * @param node {@link BinaryExpr} to convert.
   * @param lhs  {@link IExpression} left-hand-side operand.
   * @param rhs  {@link IExpression} right-hand-side operand.
   */
  private void visitIntBinaryExpression(final BinaryExpr node, final IExpression lhs, final IExpression rhs) {
    final IExpression binaryExpression;
    switch (node.getOperator()) {
      case DIVIDE:
        binaryExpression = new uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.Integer.Divide(lhs, rhs);
        break;
      case MINUS:
        binaryExpression = new uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.Integer.Minus(lhs, rhs);
        break;
      case MULTIPLY:
        binaryExpression = new uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.Integer.Multiply(lhs, rhs);
        break;
      case PLUS:
        binaryExpression = new uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.Integer.Plus(lhs, rhs);
        break;
      default:
        throw new UnsupportedOperationException();
    }
    stack.add(binaryExpression);
  }

  @Override
  public void visit(final UnaryExpr n, final Void arg) {
    super.visit(n, arg);
    final Type type = getType(n);
    if (!type.isPrimitiveType()) {
      throw new UnsupportedOperationException();
    }

    final IExpression op = stack.removeLast();
    final PrimitiveType primitiveType = type.asPrimitiveType();
    switch (primitiveType.getType()) {
      case INT:
        visitIntUnaryExpression(n, op);
        break;
      default:
        throw new UnsupportedOperationException();
    }
  }

  /**
   * Assumes that the type of {@code node} is {@code int} and puts an integer
   * unary expression on {@link #stack}.
   * 
   * @param node {@link BinaryExpr} to convert.
   * @param lhs  {@link IExpression} operand.
   */
  private void visitIntUnaryExpression(final UnaryExpr node, final IExpression op) {
    final IExpression zero = new Literal(0, PrimitiveType.intType());
    final IExpression unaryExpression;
    switch (node.getOperator()) {
      case MINUS:
        unaryExpression = new uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.Integer.Minus(zero, op);
        break;
      default:
        throw new UnsupportedOperationException();
    }
    stack.add(unaryExpression);
  }

  @Override
  public void visit(final ThisExpr n, final Void unused) {
    stack.add(This.create(TypeFactory.create(javaParser, n.calculateResolvedType())));
  }

  @Override
  public void visit(final ObjectCreationExpr n, final Void unused) {
    super.visit(n, unused);
    final int numberOfArguments = n.getArguments().size();
    final List<IExpression> arguments = new ArrayList<>(numberOfArguments);
    for (int i = 0; i < numberOfArguments; ++i)
      arguments.add(stack.removeLast());

    Collections.reverse(arguments);

    final List<IExpression> invocationArguments = arguments;

    // FIXME find constructor
    stack.add(new InvokeConstructor(invocationArguments, null));
  }

  @Override
  public void visit(final MethodCallExpr n, final Void unused) {
    // super.visit(n, unused);
    final ResolvedMethodDeclaration method = n.resolve();
    final IExpression instance;
    if (method.isStatic()) {
      // do not visit scope when the method call is static
      n.getArguments().forEach(p -> p.accept(this, unused));
      instance = null;
    } else {
      super.visit(n, unused);
      if (!n.getScope().isPresent()) {
        final ResolvedReferenceTypeDeclaration typeDeclaration = method.declaringType();
        final ResolvedReferenceType type = ReferenceTypeImpl.undeterminedParameters(typeDeclaration, typeSolver);
        addPlaceholder(TypeFactory.create(javaParser, type), true);
      }
      instance = stack.removeLast();
    }
    final int numberOfArguments = n.getArguments().size();
    final List<IExpression> arguments = new ArrayList<>(numberOfArguments);
    for (int i = 0; i < numberOfArguments; ++i)
      arguments.add(stack.removeLast());
    Collections.reverse(arguments);

    final String fullyQualifiedClassName = method.declaringType().getQualifiedName();
    involvedClasses.add(fullyQualifiedClassName);
    final MethodIdentifier methodIdentifier = MethodIdentifiers.create(method);
    final Method invokedMethod;
    try {
      invokedMethod = Methods.getMethod(classLoader, methodIdentifier);
    } catch (final NoSuchMethodException | ClassNotFoundException e) {
      throw new IllegalArgumentException(e);
    }

    final List<IExpression> invocationArguments;
    if (invokedMethod.isVarArgs()) {
      final int offset = invokedMethod.getParameterCount() - 1;
      invocationArguments = new ArrayList<>(arguments.subList(0, offset));
      final List<IExpression> varargs = arguments.subList(offset, arguments.size());
      final Class<?> componentType = invokedMethod.getParameterTypes()[offset].componentType();
      invocationArguments.add(new VarArgsObjectArrayExpression(componentType, varargs));
    } else {
      invocationArguments = arguments;
    }

    stack.add(new InvokeMethod(instance, invocationArguments, invokedMethod));
  }

  @Override
  public void visit(final FieldAccessExpr n, final Void arg) {
    final Type type = getType(n);
    try {
      final ResolvedFieldDeclaration field = n.resolve().asField();
      final String name = field.getName();
      final String cls = field.declaringType().getQualifiedName();
      stack.add(new FieldAccess(name, cls, type));
    } catch (final UnsolvedSymbolException ignored) {
      addPlaceholder(n, type);
    }
  }

  @Override
  public void visit(final NameExpr n, final Void arg) {
    try {
      final IExpression expr = environment.get(n.getNameAsString());
      stack.add(expr);
    } catch (final NullPointerException e) {
      addPlaceholder(n, getType(n));
    }

  }

  @Override
  public void visit(final TypeExpr n, final Void arg) {
    addPlaceholder(n, n.getType());
  }

  @Override
  public void visit(final VariableDeclarationExpr n, final Void arg) {
    if (n.getVariables().size() != 1) {
      throw new IllegalArgumentException();
    }
    final VariableDeclarator declaration = n.getVariable(0);
    final String name = declaration.getNameAsString();

    if (environment.containsKey(name)) {
      // converting parameters to proper ones.
      System.out.println("Discarding declaration of " + name);
      return;
    }

    final Expression initialiser = declaration.getInitializer().get();

    initialiser.accept(this, null);
    final IExpression rhs = stack.removeLast();

    // TODO is this better?
    // if (rhs instanceof SymbolExpression) {
    // stack.add(rhs);
    // } else {
    final IStatement rhsStmt = new ExpressionStatement(rhs);
    candidate.Block.Statements.add(rhsStmt);
    final IExpression rhsRepr = rhsStmt.getSymbolExpression().get();
    environment.put(name, rhsRepr);
    stack.add(rhsRepr);
    // }
  }

  /**
   * Determinse the {@link Type} of the given {@link Expression}.
   * 
   * @param expression {@link Expression} whose type to discover.
   * @return {@link Type} of {@code expression}.
   */
  private Type getType(final Expression expression) {
    final ResolvedType resolvedType = expression.calculateResolvedType();
    return TypeFactory.create(javaParser, resolvedType);
  }

  /**
   * Equivalent to {@link #addPlaceholder(Type, boolean)}, but decides based on
   * the given {@link Node}'s location whether it should be non-null.
   * 
   * @param node Unresolved {@link Node} for which to create a parameter
   *             placeholder.
   * @param type {@link Type} of the node.
   */
  private void addPlaceholder(final Node node, final Type type) {
    final Optional<MethodCallExpr> parent = node.getParentNode().filter(MethodCallExpr.class::isInstance)
        .map(MethodCallExpr.class::cast);
    final boolean isNonnull;
    if (parent.isPresent()) {
      final Optional<Expression> scope = parent.get().getScope();
      isNonnull = scope.isPresent() && scope.get() == node;
    } else {
      isNonnull = false;
    }
    addPlaceholder(type, isNonnull);
  }

  /**
   * Adds a parameter to the created snippet expression. This is usually invoked
   * when an unresolved symbol is encountered in the example code. These
   * unresolved symbols are example arguments in the JavaDoc, and resynth must
   * find the correct arguments to use during the synthesis phase.
   * 
   * @param type      Type of the expression.
   * @param isNonnull Whether {@code null} expressions make sense at this
   *                  expression's location.
   */
  private void addPlaceholder(final Type type, final boolean isNonnull) {
    final JavaLanguageKey key;
    if (isNonnull) {
      key = JavaLanguageKeys.nonnull(type);
    } else {
      key = JavaLanguageKeys.expression(type);
    }

    final Placeholder placeholder = new Placeholder();
    stack.add(placeholder);
    Parameters.add(placeholder);
    ParameterKeys.add(key);
  }
}
