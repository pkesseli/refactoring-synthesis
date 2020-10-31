package uk.ac.ox.cs.refactoring.jqfexperiments;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeTrue;

import com.pholser.junit.quickcheck.From;

import org.junit.runner.RunWith;

import edu.berkeley.cs.jqf.fuzz.Fuzz;
import edu.berkeley.cs.jqf.fuzz.JQF;

@RunWith(JQF.class)
public class StringTest {
  @Fuzz
  public void strings(@From(StringGenerator.class) String value) {
    assumeTrue(value.length() > 3);

    assertThat(value, not(containsString("a")));
  }
}
