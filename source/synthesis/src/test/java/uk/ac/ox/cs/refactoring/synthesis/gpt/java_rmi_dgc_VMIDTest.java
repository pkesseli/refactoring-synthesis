
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_rmi_dgc_VMIDTest {
  @Test
  void isUnique() throws Exception {
assertThat (synthesiseGPT ("isUnique" , "this.isUnique();" , "" , "java.rmi.dgc.VMID" , "isUnique") , Matchers . anything ()) ;
  }
}
