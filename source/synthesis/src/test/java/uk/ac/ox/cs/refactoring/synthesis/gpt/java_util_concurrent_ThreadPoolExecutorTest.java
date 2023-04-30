
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_util_concurrent_ThreadPoolExecutorTest {
  @Test
  void finalize1() throws Exception {
    assertThat(synthesiseGPT("public class MyClass {\n    // ...\n    public void finalize() {\n        try {\n            // do some cleanup\n        } finally {\n            super.finalize();\n        }\n    }\n    // ...\n}\n\n", "public class MyClass {\n    // ...\n    protected void finalize() throws Throwable {\n        try {\n            // do some cleanup\n        } finally {\n            super.finalize();\n        }\n    }\n    // ...\n}\n", "java.util.concurrent.ThreadPoolExecutor", "finalize"), Matchers.anything());
  }
}