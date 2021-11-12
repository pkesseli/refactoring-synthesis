package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import uk.ac.ox.cs.refactoring.synthesis.candidate.api.ExecutionContext;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.SnippetCandidate;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.methods.MethodIdentifier;
import uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation;
import uk.ac.ox.cs.refactoring.synthesis.state.State;

public class DateTest {
  @ParameterizedTest
  @CsvSource({ "getMonth," + Calendar.MONTH, "getDate," + Calendar.DAY_OF_MONTH, "getHours," + Calendar.HOUR_OF_DAY,
      "getMinutes," + Calendar.MINUTE, "getSeconds," + Calendar.SECOND })
  void get(final String methodName, final int field) throws Exception {
    final MethodIdentifier methodToRefactor = new MethodIdentifier(Date.class.getName(), methodName,
        Collections.emptyList());
    final SnippetCandidate candidate = Deprecation.synthesise(methodToRefactor);
    final Calendar calendar = Calendar.getInstance();
    final Date date = calendar.getTime();
    final int expected = calendar.get(field);
    final State state = new State(date);
    final ExecutionContext context = new ExecutionContext(DateTest.class.getClassLoader(), state);
    assertEquals(expected, candidate.Block.execute(context));
  }

  @Test
  @Disabled
  void getYear() throws Exception {
    final String methodName = "getYear";
    final int field = Calendar.YEAR;
    final MethodIdentifier methodToRefactor = new MethodIdentifier(Date.class.getName(), methodName,
        Collections.emptyList());
    final SnippetCandidate candidate = Deprecation.synthesise(methodToRefactor);
    final Calendar calendar = Calendar.getInstance();
    final Date date = calendar.getTime();
    final int expected = calendar.get(field);
    final State state = new State(date);
    final ExecutionContext context = new ExecutionContext(DateTest.class.getClassLoader(), state);
    assertEquals(expected, candidate.Block.execute(context));
  }
}
