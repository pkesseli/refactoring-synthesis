
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_lang_ThreadTest {
  @Test
  void countStackFrames() throws Exception {
    assertThat(synthesiseGPT("int stackFrames = this.countStackFrames();\n\n", "StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();\nint stackFrames = stackTrace.length;\n", "java.lang.Thread", "countStackFrames"), Matchers.anything());
  }

  @Test
  void resume() throws Exception {
    assertThat(synthesiseGPT("this.resume();\n", "private boolean isPaused = false;\n\npublic void pauseThread() {\n    isPaused = true;\n}\n\npublic void resumeThread() {\n    isPaused = false;\n}\n\npublic void run() {\n    while (true) {\n        if (!isPaused) {\n            // Your thread code here\n        }\n        try {\n            Thread.sleep(100);\n        } catch (InterruptedException e) {\n            e.printStackTrace();\n        }\n    }\n}\n", "java.lang.Thread", "resume"), anyOf(contains("suspend")));
  }

  @Test
  void stop() throws Exception {
    assertThat(synthesiseGPT("this.stop();\n\n", "this.interrupt();\n", "java.lang.Thread", "stop"), anyOf(contains("ThreadDeath"), contains("interrupt"), contains("stop")));
  }

  @Test
  void suspend() throws Exception {
    assertThat(synthesiseGPT("this.suspend();\n\n", "Thread.currentThread().suspend();\n", "java.lang.Thread", "suspend"), anyOf(contains("resume")));
  }
}