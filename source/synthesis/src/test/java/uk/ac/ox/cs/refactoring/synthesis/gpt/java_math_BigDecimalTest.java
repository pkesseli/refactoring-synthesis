
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
assertThat (synthesiseGPT ("divide1" , "this.divide(param0, param1);" , "\nthis.divide(param0, param1, RoundingMode.HALF_UP) // Choose appropriate RoundingMode based on your requirements\n;" , "java.math.BigDecimal" , "divide" , "java.math.BigDecimal" , "int") , anyOf (contains ("divide"))) ;
  }

  @Test
  void divide2() throws Exception {
assertThat (synthesiseGPT ("divide2" , "this.divide(param0, param1, param2);" , "\nthis.divide(param0).setScale(param1, RoundingMode.valueOf(param2))\n;" , "java.math.BigDecimal" , "divide" , "java.math.BigDecimal" , "int" , "int") , anyOf (contains ("divide"))) ;
  }

  @Test
  void setScale() throws Exception {
assertThat (synthesiseGPT ("setScale" , "this.setScale(param0, param1);" , "\nimport java.math.BigDecimal;\nimport java.math.RoundingMode;\n\n// ...\n\nBigDecimal thisBigDecimal = new BigDecimal(\"123.456\"); // Example BigDecimal initialization\nint newScale = param0; // The new scale you want to set\nint roundingModeInt = param1; // The rounding mode as an integer\nRoundingMode roundingMode = RoundingMode.valueOf(roundingModeInt); // Convert int to RoundingMode\n\nthisBigDecimal = thisBigDecimal.setScale(newScale, roundingMode);\n" , "java.math.BigDecimal" , "setScale" , "int" , "int") , anyOf (contains ("setScale"))) ;
  }
}
