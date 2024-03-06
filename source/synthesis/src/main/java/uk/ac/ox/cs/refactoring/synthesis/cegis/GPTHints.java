package uk.ac.ox.cs.refactoring.synthesis.cegis;

import uk.ac.ox.cs.refactoring.synthesis.candidate.java.methods.MethodIdentifier;

public class GPTHints {
  public final String before;
  public String after;
  public final MethodIdentifier methodToRefactor;

  public GPTHints(final String before, final String after, final MethodIdentifier methodToRefactor) {
    this.before = before;
    this.after = after;
    this.methodToRefactor = methodToRefactor;
  }
}
