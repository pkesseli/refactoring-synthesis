
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_lang_ObjectTest {
  @Test
  void finalize1() throws Exception {
    assertThat(synthesiseGPT("<code before refactoring here>\nimport java.lang.ref.Cleaner;\n\npublic class MyClass implements AutoCloseable {\n    private final Cleaner cleaner = Cleaner.create();\n\n    private static class MyResource implements Runnable {\n        private final Resource resource;\n\n        private MyResource(Resource resource) {\n            this.resource = resource;\n        }\n\n        @Override\n        public void run() {\n            // Release the resource here\n            resource.release();\n        }\n    }\n\n    private final MyResource myResource;\n\n    public MyClass() {\n        Resource resource = new Resource();\n        myResource = new MyResource(resource);\n        cleaner.register(this, myResource);\n    }\n\n    @Override\n    public void close() {\n        // Release the resource here\n        myResource.run();\n    }\n\n    private static class Resource {\n        public void release() {\n            // Release the resource here\n        }\n    }\n}\n", "", "java.lang.Object", "finalize"), anyOf(contains("AutoCloseable"), contains("Cleaner"), contains("PhantomReference"), contains("finalize")));
  }
}