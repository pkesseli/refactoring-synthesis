package uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.javadoc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class LinksTest {

  @Test
  void getContent() {
    assertEquals("#setVisible(boolean)", Links.getLink(" #setVisible(boolean) setVisible(boolean)"));
    assertEquals("#setVisible(boolean)", Links.getLink("#setVisible(boolean) setVisible(boolean)"));
    assertEquals("#isMimeTypeEqual(String)", Links.getLink("#isMimeTypeEqual(String)"));
  }
}
