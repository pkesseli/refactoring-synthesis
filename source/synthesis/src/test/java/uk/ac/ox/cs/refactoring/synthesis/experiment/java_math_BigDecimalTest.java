package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.mapsTo;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import java.math.BigDecimal;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class java_math_BigDecimalTest {

  @Test
  @Disabled("Not in our instruction set. A simple addition would be nested ternary operations.")
  @SuppressWarnings("deprecation")
  void divide() throws Exception {
    assertThat(synthesiseAlias("java.math.BigDecimal", "divide", "java.math.BigDecimal", "int"),
        allOf(
            contains(".getBounds("),
            mapsTo(BigDecimal.valueOf(2.7), BigDecimal.valueOf(10.5), BigDecimal.valueOf(4), BigDecimal.ROUND_UP),
            mapsTo(BigDecimal.valueOf(2.6), BigDecimal.valueOf(10.5), BigDecimal.valueOf(4), BigDecimal.ROUND_DOWN),
            mapsTo(BigDecimal.valueOf(-2.6), BigDecimal.valueOf(-10.5), BigDecimal.valueOf(4),
                BigDecimal.ROUND_CEILING),
            mapsTo(BigDecimal.valueOf(-2.7), BigDecimal.valueOf(-10.5), BigDecimal.valueOf(4), BigDecimal.ROUND_FLOOR),
            mapsTo(BigDecimal.valueOf(8), BigDecimal.valueOf(30), BigDecimal.valueOf(4), BigDecimal.ROUND_HALF_UP),
            mapsTo(BigDecimal.valueOf(7), BigDecimal.valueOf(30), BigDecimal.valueOf(4), BigDecimal.ROUND_HALF_DOWN),
            mapsTo(BigDecimal.valueOf(8), BigDecimal.valueOf(30), BigDecimal.valueOf(4), BigDecimal.ROUND_HALF_EVEN),
            mapsTo(BigDecimal.valueOf(10), BigDecimal.valueOf(30), BigDecimal.valueOf(3),
                BigDecimal.ROUND_UNNECESSARY)));
  }

  @Test
  @Disabled("Not in our instruction set, for the same reason as previous test.")
  void divideWithScale() throws Exception {
  }

  @Test
  @Disabled("Not in our instruction set, for the same reason as previous test.")
  void setScale() throws Exception {
  }
}
