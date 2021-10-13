package uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.javadoc;

import java.util.Arrays;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.expr.Expression;

import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.HierarchicalComponent;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.IExpression;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaLanguageKey;

/**
 * Fixed Java snippet to be used in the instruction set, optionally with
 * parameters to be filled with parameters or the {@code this} reference of the
 * synthesised method. In CISC/RISC terminology, this would be a rich
 * instruction. Such large instructions are e.g. created when finding example
 * snippets in a deprecated method's Javadoc.
 */
class SnippetComponent implements HierarchicalComponent<JavaLanguageKey, IExpression> {

  /** Used to look up methods when creating invokable expressions. */
  private final ClassLoader classLoader;

  /** Parser which parsed {@link #javadocExpression} */
  private final JavaParser javaParser;

  /** Expression extracted from Javadoc. */
  private final Expression javadocExpression;

  /**
   * @param classLoader       {@link #classLoader}
   * @param javaParser        {@link #javaParser}
   * @param javadocExpression {@link SnippetComponentVisitor}
   */
  SnippetComponent(final ClassLoader classLoader, final JavaParser javaParser, final Expression javadocExpression) {
    this.classLoader = classLoader;
    this.javaParser = javaParser;
    this.javadocExpression = javadocExpression;
  }

  @Override
  public List<JavaLanguageKey> getParameterKeys() {
    final SnippetComponentVisitor converter = new SnippetComponentVisitor(classLoader, javaParser);
    javadocExpression.accept(converter, null);
    return converter.ParameterKeys;
  }

  @Override
  public IExpression construct(final Object[] arguments) {
    final IExpression[] args = Arrays.stream(arguments).map(IExpression.class::cast).toArray(IExpression[]::new);
    final SnippetComponentVisitor converter = new SnippetComponentVisitor(classLoader, javaParser);
    javadocExpression.accept(converter, null);
    for (int i = 0; i < args.length; ++i) {
      converter.Parameters.get(i).expression = args[i];
    }
    return converter.expression;
  }
}
