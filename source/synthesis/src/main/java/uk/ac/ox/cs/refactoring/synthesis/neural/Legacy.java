package uk.ac.ox.cs.refactoring.synthesis.neural;

import uk.ac.ox.cs.refactoring.synthesis.cegis.GPTHints;

public class Legacy implements CodeEngine {
  private final String answer;
  public Legacy(GPTHints hints) {
    answer = hints.after;
  }

  @Override
  public String query(String prompt) {
    return answer;
  }
}
