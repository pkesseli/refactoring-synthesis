
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_tools_StandardJavaFileManagerTest {
  @Test
  void getJavaFileObjectsFromPaths() throws Exception {
    assertThat(synthesiseGPT("Iterable<? extends Path> a = ...;\nStandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);\nIterable<? extends JavaFileObject> fileObjects = fileManager.getJavaFileObjectsFromPaths(a);\n\n", "Iterable<? extends Path> a = ...;\nStandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);\nIterable<? extends JavaFileObject> fileObjects = fileManager.getJavaFileObjects(a.toArray(Path[]::new));\n", "javax.tools.StandardJavaFileManager", "getJavaFileObjectsFromPaths", "Iterable&lt;? extends Path&gt;"), anyOf(contains("Iterable"), contains("Iterable<Path>"), contains("Path"), contains("getJavaFileObjectsFromPaths")));
  }
}