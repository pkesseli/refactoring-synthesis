package uk.ac.ox.cs.refactoring.synthesis.induction;


import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.ComponentDirectory;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.SnippetCandidate;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.parser.ParserContext;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.javadoc.SourceCodeConvertor;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.statement.ExpressionStatement;

public class GPTSynthesis {

  public final ClassLoader classLoader;

  public final ParserContext parserContext;

  public final ComponentDirectory components;

  public GPTSynthesis(final ClassLoader classLoader, final ParserContext parserContext, final ComponentDirectory components) {
    this.classLoader = classLoader;
    this.parserContext = parserContext;
    this.components = components;
  }

  public SnippetCandidate synthesise(final String code) {
    
    // TODO maybe re-parse within the method context
    var block = parserContext.JavaParser.parseBlock(code);
    final var candidate = new SnippetCandidate();


    for (final var stmt: block.getResult().get().getStatements()) {
      System.out.println(stmt.toString());
      final var expr = stmt.asExpressionStmt().getExpression();
      final var convertor = new SourceCodeConvertor(classLoader, parserContext.JavaParser, parserContext.TypeSolver, components.InvolvedClasses);
      candidate.Block.Statements.add(new ExpressionStatement(convertor.convertExpression(expr)));
    }

    System.out.println(candidate.Block.toNode().toString());
    System.out.println("done");
    return candidate;
  }
}
