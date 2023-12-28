
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class javax_management_ValueExpTest {
  @Disabled("No replacement")
  @Test
  void setMBeanServer() throws Exception {
assertThat (synthesiseGPT ("setMBeanServer" , "this.setMBeanServer(param0);" , "\n// Remove the call to setMBeanServer\n// Use QueryEval.getMBeanServer() instead\nfinal MBeanServer mBeanServer = QueryEval.getMBeanServer();\n// Use mBeanServer as needed\n" , "javax.management.ValueExp" , "setMBeanServer" , "javax.management.MBeanServer") , anyOf (contains ("ValueExp") , contains ("getMBeanServer"))) ;
  }
}
