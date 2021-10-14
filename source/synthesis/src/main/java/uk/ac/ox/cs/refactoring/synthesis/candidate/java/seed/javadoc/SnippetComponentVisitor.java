package uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.javadoc;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;

import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.IExpression;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaLanguageKey;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaLanguageKeys;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.FieldAccess;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.InvokeMethod;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.methods.MethodIdentifier;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.methods.Methods;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.type.TypeFactory;

class SnippetComponentVisitor extends VoidVisitorAdapter<Void> {

  private final ClassLoader classLoader;

  private final JavaParser javaParser;

  private final Set<String> involvedClasses;

  IExpression expression;

  final List<Placeholder> Parameters = new ArrayList<>();

  final List<JavaLanguageKey> ParameterKeys = new ArrayList<>();

  final LinkedList<IExpression> stack = new LinkedList<>();

  /**
   * @param classLoader     {@link #classLoader}
   * @param javaParser      {@link #javaParser}
   * @param involvedClasses {@link #involvedClasses}
   */
  SnippetComponentVisitor(final ClassLoader classLoader, final JavaParser javaParser,
      final Set<String> involvedClasses) {
    this.classLoader = classLoader;
    this.javaParser = javaParser;
    this.involvedClasses = involvedClasses;
  }

  @Override
  public void visit(final MethodCallExpr n, final Void unused) {
    super.visit(n, unused);
    final ResolvedMethodDeclaration method = n.resolve();
    final IExpression instance;
    if (method.isStatic()) {
      instance = null;
    } else {
      instance = stack.removeLast();
    }
    final List<IExpression> arguments = new ArrayList<IExpression>(stack);
    stack.clear();
    final List<String> parameterTypes = new ArrayList<>();
    for (int i = 0; i < method.getNumberOfParams(); ++i)
      parameterTypes.add(method.getParam(i).getType().describe());

    final String fullyQualifiedClassName = method.declaringType().getQualifiedName();
    involvedClasses.add(fullyQualifiedClassName);
    final MethodIdentifier methodIdentifier = new MethodIdentifier(fullyQualifiedClassName, method.getName(),
        parameterTypes);
    try {
      expression = new InvokeMethod(instance, arguments, Methods.getMethod(classLoader, methodIdentifier));
    } catch (final NoSuchMethodException | ClassNotFoundException e) {
      throw new IllegalArgumentException(e);
    }
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
    // TODO: Currently local names in Javadoc snippets unsupported, always treatd as
    // unresolved.
    addPlaceholder(n, getType(n));
  }

  /**
   * 
   * @param expression
   * @return
   */
  private Type getType(final Expression expression) {
    final ResolvedType resolvedType = expression.calculateResolvedType();
    return TypeFactory.create(javaParser, resolvedType);
  }

  /**
   * 
   * @param type
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
