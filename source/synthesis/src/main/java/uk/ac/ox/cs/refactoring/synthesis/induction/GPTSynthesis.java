package uk.ac.ox.cs.refactoring.synthesis.induction;

import com.github.javaparser.JavaParser;

import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.IExpression;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.IStatement;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.SnippetCandidate;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.javadoc.SourceCodeConvertor;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.statement.ExpressionStatement;

public class GPTSynthesis {

  public final ClassLoader classLoader;

  public final JavaParser JavaParser;

  public GPTSynthesis(final ClassLoader classLoader, final JavaParser JavaParser) {
    this.classLoader = classLoader;
    this.JavaParser = JavaParser;
  }

  public SnippetCandidate synthesise(final String code) {
    
    // TODO maybe re-parse within the method context
    var block = JavaParser.parseBlock(code);
    final var candidate = new SnippetCandidate();


    for (final var stmt: block.getResult().get().getStatements()) {

      System.out.println(stmt.toString());
      final var expr = stmt.asExpressionStmt().getExpression();
      final var convertor = new SourceCodeConvertor(classLoader, JavaParser, null, null);
      candidate.Block.Statements.add(new ExpressionStatement(convertor.convertExpression(expr)));
    }

    return candidate;
  }
}
