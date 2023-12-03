
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_math_BigDecimalTest {
  @Test
  void divide1() throws Exception {
assertThat (synthesiseGPT ("this.divide(param0, param1);" , "\nthis.divide(param0, param1, RoundingMode.HALF_UP)\n;" , "java.math.BigDecimal" , "divide" , "java.math.BigDecimal" , "int") , anyOf (contains ("divide"))) ;
  }

  @Test
  void divide2() throws Exception {
assertThat (synthesiseGPT ("this.divide(param0, param1, param2);" , "\nthis.divide(param0, param1, RoundingMode.valueOf(param2))\n              .setScale(param1, RoundingMode.valueOf(param2))\n;" , "java.math.BigDecimal" , "divide" , "java.math.BigDecimal" , "int" , "int") , anyOf (contains ("divide"))) ;
  }

  @Test
  void setScale() throws Exception {
assertThat (synthesiseGPT ("this.setScale(param0, param1);" , "\nthis.setScale(param0, RoundingMode.getTimeToLive(param1))\n;" , "java.math.BigDecimal" , "setScale" , "int" , "int") , anyOf (contains ("setScale"))) ;
  }
}
