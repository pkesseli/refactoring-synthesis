
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_rmi_server_RemoteRefTest {
  @Test
  void done() throws Exception {
assertThat (synthesiseGPT ("done" , "this.done(param0);" , "" , "java.rmi.server.RemoteRef" , "done" , "java.rmi.server.RemoteCall") , anyOf (contains ("done") , contains ("invoke") , contains ("newCall"))) ;
  }

  @Test
  void invoke() throws Exception {
assertThat (synthesiseGPT ("invoke" , "this.invoke(param0);" , "" , "java.rmi.server.RemoteRef" , "invoke" , "java.rmi.server.RemoteCall") , anyOf (contains ("done") , contains ("invoke") , contains ("newCall"))) ;
  }

  @Test
  void newCall() throws Exception {
assertThat (synthesiseGPT ("newCall" , "this.newCall(param0, param1, param2, param3);" , "" , "java.rmi.server.RemoteRef" , "newCall" , "java.rmi.server.RemoteObject" , "java.rmi.server.Operation[]" , "int" , "long") , anyOf (contains ("done") , contains ("invoke") , contains ("newCall"))) ;
  }
}
