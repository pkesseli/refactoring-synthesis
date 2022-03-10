package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.mapsTo;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import uk.ac.ox.cs.refactoring.synthesis.candidate.api.ExecutionContext;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.SnippetCandidate;
import uk.ac.ox.cs.refactoring.synthesis.state.State;

public class java_util_DateTest {

  @ParameterizedTest
  @CsvSource({ "getMonth," + Calendar.MONTH, "getDate," + Calendar.DAY_OF_MONTH, "getHours," + Calendar.HOUR_OF_DAY,
      "getMinutes," + Calendar.MINUTE, "getSeconds," + Calendar.SECOND })
  void get(final String methodName, final int field) throws Exception {
    final SnippetCandidate candidate = synthesiseAlias(Date.class.getName(), methodName);
    final Calendar calendar = Calendar.getInstance();
    final Date date = calendar.getTime();
    final int expected = calendar.get(field);
    final State state = new State(date);
    final ExecutionContext context = new ExecutionContext(java_util_DateTest.class.getClassLoader(), state);
    assertEquals(expected, candidate.Block.execute(context));
  }

  @Test
  void getYear() throws Exception {
    final String methodName = "getYear";
    final int field = Calendar.YEAR;
    final SnippetCandidate candidate = synthesiseAlias(Date.class.getName(), methodName);
    final Calendar calendar = Calendar.getInstance();
    final Date date = calendar.getTime();
    final int expected = calendar.get(field) - 1900;
    final State state = new State(date);
    final ExecutionContext context = new ExecutionContext(java_util_DateTest.class.getClassLoader(), state);
    assertEquals(expected, candidate.Block.execute(context));
  }

  @Test
  void getTimezoneOffset() throws Exception {
    assertThat(synthesiseAlias(Date.class.getName(), "getTimezoneOffset"), allOf(contains(".get("),
        contains("Calendar.ZONE_OFFSET"), contains("Calendar.DST_OFFSET"), contains("/"), contains("60 * 100")));
  }
}
