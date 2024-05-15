package uk.ac.ox.cs.refactoring.synthesis.neural;

public class MockEngine extends CodeEngine {
  private final String answer = """
<code>
String originalText = this.getText();
String before = originalText.substring(0, start);
String after = originalText.substring(end);
String replaced = before + str + after;
this.setText(replaced);
</code>
      """;;

  @Override
  public String query(String prompt) {
    return answer;
  }
  
}
