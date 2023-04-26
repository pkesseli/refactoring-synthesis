package uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.javadoc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class CamelCaseDetecteorTest {

  @Test
  void getMethodInvocationMatch() {
    final String deprecatedJavaDoc = "Replaced by isJavaIdentifierStart(char).";
    final String actual = CamelCaseDetector.getMethodInvocation(deprecatedJavaDoc);
    assertEquals("isJavaIdentifierStart(char)", actual);
  }

  @Test
  void getMethodInvocationNoMatch() {
    final String deprecatedJavaDoc = "Nothing to see here (not here either). We even ignore sleep().";
    assertNull(CamelCaseDetector.getMethodInvocation(deprecatedJavaDoc));
  }

  @Test
  void getMethodNameMatch() {
    final String deprecatedJavaDoc = "use getTimeToLive instead.";
    final String actual = CamelCaseDetector.getMethodName(deprecatedJavaDoc);
    assertEquals("getTimeToLive", actual);
  }

  @Test
  void getMethodNameNoMatch() {
    final String deprecatedJavaDoc = "Again, nothing to see here. Ignore it all.";
    assertEquals(null, CamelCaseDetector.getMethodName(deprecatedJavaDoc));
  }
}
