package uk.ac.ox.cs.refactoring.synthesis.induction;

import com.github.javaparser.JavaParser;

import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.IStatement;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.SnippetCandidate;

public class GPTSynthesis {


  public final JavaParser JavaParser;

  public GPTSynthesis() {
    this.JavaParser = null;
  }

  public SnippetCandidate synthesise(final String code) {
    
    // TODO maybe re-parse within the method context
    var block = JavaParser.parseBlock(code);
    final var candidate = new SnippetCandidate();


    for (final var stmt: block.getResult().get().getStatements()) {
        candidate.Block.Statements.add((IStatement) stmt);
    }

    return candidate;
  }
}
