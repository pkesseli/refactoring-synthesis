package uk.ac.ox.cs.refactoring.synthesis.neural;

import java.util.List;

/** Base engine interfacing OpenAi API */
public interface QueryEngine {
  public default String query(Prompt prompt) throws Exception {
    return query(prompt.toString());
  }
  public String query(String prompt) throws Exception;

  public default List<String> queryN(Prompt prompt, int beamSize) throws Exception {
    return queryN(prompt.toString(), beamSize);
  }

  public List<String> queryN(String prompt, int beamSize) throws Exception;
}
