package uk.ac.ox.cs.refactoring.synthesis.neural;

public class Claude2 extends CodeEngine {

  @Override
  public String query(String prompt) throws Exception {
    String response = Bedrock.invokeClaude(prompt.toString());
    return response;
  }
  
}
