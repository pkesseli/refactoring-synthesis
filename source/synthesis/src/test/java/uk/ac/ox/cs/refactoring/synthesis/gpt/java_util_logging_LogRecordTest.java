
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_util_logging_LogRecordTest {
  @Test
  void setMillis() throws Exception {
assertThat (synthesiseGPT ("setMillis" , "this.setMillis(param0);" , "\nthis.setInstant(Instant.ofEpochMilli(param0));\n" , "java.util.logging.LogRecord" , "setMillis" , "long") , anyOf (contains ("Instant") , contains ("setInstant"))) ;
  }
}
