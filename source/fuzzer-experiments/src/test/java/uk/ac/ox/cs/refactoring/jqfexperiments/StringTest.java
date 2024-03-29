package uk.ac.ox.cs.refactoring.jqfexperiments;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assume.assumeTrue;

import com.pholser.junit.quickcheck.From;

import org.junit.runner.RunWith;

import edu.berkeley.cs.jqf.fuzz.Fuzz;
import edu.berkeley.cs.jqf.fuzz.JQF;

@RunWith(JQF.class)
public class StringTest {
  private static final boolean isEnabled = Boolean.getBoolean("enableFuzzerExperiments");

  @Fuzz
  public void strings(@From(StringGenerator.class) String value) {
    assumeTrue(isEnabled);
    assumeTrue(value.length() > 3);

    assertThat(value, not(containsString("a")));
  }
}
