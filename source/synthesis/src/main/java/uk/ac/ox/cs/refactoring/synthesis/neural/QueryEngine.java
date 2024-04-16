package uk.ac.ox.cs.refactoring.synthesis.neural;

/** Base engine interfacing OpenAi API */
public interface QueryEngine {
  public default String query(Prompt prompt) throws Exception {
    return query(prompt.toString());
  }
  public String query(String prompt) throws Exception;
}
