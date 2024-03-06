package uk.ac.ox.cs.refactoring.synthesis.neural;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

public class CodeEngineTest {
  @Test
  void testExtract() {
    String taggedCode = """
        <code>
          this.blah();
          for (int i = 0; i < maxVal; i += 1) {
            // ...
          }
        </code>
        """;

    assertTrue(CodeEngine.extract(taggedCode).contains("this.blah()"));
    assertTrue(CodeEngine.extract(taggedCode).contains("for (int i = 0; i < maxVal; i += 1) {"));

    String backtickedCode = """
      ```java
        this.blah();
        for (int i = 0; i < maxVal; i += 1) {
          // ...
        }
      ```
      """;

    assertTrue(CodeEngine.extract(backtickedCode).contains("this.blah()"));
    assertTrue(CodeEngine.extract(backtickedCode).contains("for (int i = 0; i < maxVal; i += 1) {"));
  }

  @Test
  void testConstraints() {
    CodeEngine codeEngine = new CodeEngine();
    String prompt = "Show me how to solve n-queens puzzle in Java.";
    String code = codeEngine.generateCode(prompt);
    System.out.println(code);
  }
}
