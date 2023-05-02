
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_lang_reflect_AccessibleObjectTest {
  @Test
  void isAccessible() throws Exception {
    assertThat(synthesiseGPT("boolean isAccessible = this.isAccessible();\n\n", "boolean isAccessible = AccessController.doPrivileged((PrivilegedAction<Boolean>) () -> {\n    boolean accessible = false;\n    try {\n        accessible = this.canAccess(null);\n    } catch (IllegalAccessException e) {\n        // ignore\n    }\n    return accessible;\n});\n", "java.lang.reflect.AccessibleObject", "isAccessible"), anyOf(contains("canAccess"), contains("false")));
  }
}