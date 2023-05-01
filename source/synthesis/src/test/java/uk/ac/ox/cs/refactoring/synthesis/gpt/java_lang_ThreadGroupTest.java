
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_lang_ThreadGroupTest {
  @Test
  void allowThreadSuspension() throws Exception {
    assertThat(synthesiseGPT("this.allowThreadSuspension(true);\n\n", "", "java.lang.ThreadGroup", "allowThreadSuspension", "boolean"), anyOf(contains("suspend")));
  }

  @Test
  void resume() throws Exception {
    assertThat(synthesiseGPT("this.resume();\n", "Thread.currentThread().resume();\n", "java.lang.ThreadGroup", "resume"), anyOf(contains("suspend")));
  }

  @Test
  void stop() throws Exception {
    assertThat(synthesiseGPT("this.stop();\n\n", "Thread.currentThread().getThreadGroup().interrupt();\n", "java.lang.ThreadGroup", "stop"), anyOf(contains("stop")));
  }

  @Test
  void suspend() throws Exception {
    assertThat(synthesiseGPT("this.suspend();\n", "Thread.currentThread().suspend();\n", "java.lang.ThreadGroup", "suspend"), anyOf(contains("suspend")));
  }
}