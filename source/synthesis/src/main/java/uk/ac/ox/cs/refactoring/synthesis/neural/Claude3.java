package uk.ac.ox.cs.refactoring.synthesis.neural;

public class Claude3 extends CodeEngine {
  @Override
  public String query(String prompt) throws Exception {
    String response = Bedrock.invokeClaude3(prompt.toString());
    return response;
  }
}
