package uk.ac.ox.cs.refactoring.synthesis.neural;

import java.util.Collections;
import java.util.List;

public class MockEngine implements CodeEngine {
  private final String answer = "I'm mocking GPT4. <code>this.getComponentAt(x, y);</code>";

  @Override
  public String query(String prompt) {
    return answer;
  }

  @Override
  public List<String> queryN(String prompt, int beamSize) {
    return Collections.singletonList(answer);
  }
  
}
