
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class javax_management_StringValueExpTest {
  @Disabled("No replacement")
  @Test
  void setMBeanServer() throws Exception {
assertThat (synthesiseGPT ("setMBeanServer" , "this.setMBeanServer(param0);" , "\nfinal MBeanServerConnection mbsc = new MBeanServerInvocationHandler(\n        param0, new ObjectName(MBeanServerDelegate.DELEGATE_NAME)).getMBeanServerConnection();\nthis.setMBeanServer(mbsc);\n" , "javax.management.StringValueExp" , "setMBeanServer" , "javax.management.MBeanServer") , Matchers . anything ()) ;
  }
}
