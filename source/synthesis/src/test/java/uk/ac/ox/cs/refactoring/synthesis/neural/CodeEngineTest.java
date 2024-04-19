package uk.ac.ox.cs.refactoring.synthesis.neural;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

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
  void testMultiple() {
    String multiple = """
 <code>
this.drawEchoCharacter(g, x, y, c);
</code>

This method call is deprecated and should be replaced with the following code:

<code>
this.drawEchoCharacter(g, (float) x, (float) y, c);
</code>

The method signature has changed to accept a float for the X and Y coordinates, and the method is now part of the Graphics2D class instead of the Graphics class.
        """;

    System.out.println(CodeEngine.extract(multiple));
    assertTrue(CodeEngine.extract(multiple).contains("this.drawEchoCharacter(g, x, y, c);"));
  }

  @Test
  void testConstraints() throws IOException {
    CodeEngine codeEngine = new LocalCodeLLaMa2();
    String instruction = "Show me how to solve n-queens puzzle in Java.";
    Prompt prompt = new Prompt(instruction);
    String code = codeEngine.generateCode(prompt);
    System.out.println(code);
  }
}
