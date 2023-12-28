
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_lang_StringTest {
  @Test
  void getBytes() throws Exception {
assertThat (synthesiseGPT ("getBytes" , "this.getBytes(param0, param1, param2, param3);" , "\nfinal byte[] bytes = this.substring(param0, param1).getBytes();\nSystem.arraycopy(bytes, 0, param2, param3, bytes.length);\n" , "java.lang.String" , "getBytes" , "int" , "int" , "byte[]" , "int") , anyOf (contains ("getBytes"))) ;
  }
}
