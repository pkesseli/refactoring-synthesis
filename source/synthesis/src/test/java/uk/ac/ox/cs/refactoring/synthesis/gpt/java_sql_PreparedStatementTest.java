
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_sql_PreparedStatementTest {
  @Test
  void setUnicodeStream() throws Exception {
assertThat (synthesiseGPT ("this.setUnicodeStream(param0, param1, param2);" , "\nReader reader = new InputStreamReader(param1, StandardCharsets.UTF_8);\nthis.setCharacterStream(param0, reader, param2);\n```\n\nMake sure that `param1` is an `InputStream` containing Unicode text, and `param2` is the length of the stream in characters, not bytes. If you don't know the number of characters, you can omit the length parameter:\n\n```java\nReader reader = new InputStreamReader(param1, StandardCharsets.UTF_8);\nthis.setCharacterStream(param0, reader);\n```\n\nIf `param1` is a `String`, you could use `setString` directly:\n\n```java\nthis.setString(param0, param1);\n" , "java.sql.PreparedStatement" , "setUnicodeStream" , "int" , "java.io.InputStream" , "int") , anyOf (contains ("setCharacterStream"))) ;
  }
}
