
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseNeural;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_util_logging_LoggerTest {
  @Test
  void logrb1() throws Exception {
assertThat (synthesiseNeural ("logrb1" , "this.logrb(param0, param1, param2, param3, param4);" , "\nthis.logrb(param0, param1, param2, null, param3, param4);\n" , "java.util.logging.Logger" , "logrb" , "java.util.logging.Level" , "java.lang.String" , "java.lang.String" , "java.lang.String" , "java.lang.String") , anyOf (contains ("logrb"))) ;
  }

  @Test
  void logrb2() throws Exception {
assertThat (synthesiseNeural ("logrb2" , "this.logrb(param0, param1, param2, param3, param4, param5);" , "\nthis.logrb(param0, param1, param2, null, param3, param4);\n" , "java.util.logging.Logger" , "logrb" , "java.util.logging.Level" , "java.lang.String" , "java.lang.String" , "java.lang.String" , "java.lang.String" , "java.lang.Object") , anyOf (contains ("logrb"))) ;
  }

  @Test
  void logrb3() throws Exception {
assertThat (synthesiseNeural ("logrb3" , "this.logrb(param0, param1, param2, param3, param4, param5);" , "\nthis.logrb(param0, param1, param2, null, param3, param4);\n" , "java.util.logging.Logger" , "logrb" , "java.util.logging.Level" , "java.lang.String" , "java.lang.String" , "java.lang.String" , "java.lang.String" , "java.lang.Object[]") , anyOf (contains ("logrb"))) ;
  }

  @Test
  void logrb4() throws Exception {
assertThat (synthesiseNeural ("logrb4" , "this.logrb(param0, param1, param2, param3, param4, param5);" , "\nthis.logrb(param0, param1, param2, null, param3, param5);\n" , "java.util.logging.Logger" , "logrb" , "java.util.logging.Level" , "java.lang.String" , "java.lang.String" , "java.lang.String" , "java.lang.String" , "java.lang.Throwable") , anyOf (contains ("logrb"))) ;
  }
}
