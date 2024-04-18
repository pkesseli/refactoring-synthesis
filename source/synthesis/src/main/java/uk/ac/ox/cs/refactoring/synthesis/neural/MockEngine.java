package uk.ac.ox.cs.refactoring.synthesis.neural;

public class MockEngine extends CodeEngine {
  private final String answer = "I'm mocking GPT4. <code>this.getComponentAt(x, y);</code>";

  @Override
  public String query(String prompt) {
    return answer;
  }
  
}
