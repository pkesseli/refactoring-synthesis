package uk.ac.ox.cs.refactoring.synthesis.cegis;


public class GPTHints {
  public final String before;
  public String after;

  public GPTHints(final String before, final String after) {
    this.before = before;
    this.after = after;
  }
}
