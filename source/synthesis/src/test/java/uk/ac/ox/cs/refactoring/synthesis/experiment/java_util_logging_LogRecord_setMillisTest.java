
package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Disabled;
import java.util.logging.LogRecord;

import org.junit.jupiter.api.Test;

class java_util_logging_LogRecord_setMillisTest {

  @Test
  @Disabled("Change of signature")
  void setMillis() throws Exception {
    assertThat(synthesiseAlias(LogRecord.class.getName(), "setMillis", "long"),
        contains(".setInstance("));

  }
}
