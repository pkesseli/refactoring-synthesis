package uk.ac.ox.cs.refactoring.synthesis.neural;

import org.junit.jupiter.api.Test;

public class QueryEngineTest {
    
  @Test
  void testAPIKey() throws Exception {
    QueryEngine engine = new QueryEngine();
    String question = "Show me how to solve n-queens puzzle in Java.";
    String response = engine.query(question);
    System.out.println(response);
  }
}
