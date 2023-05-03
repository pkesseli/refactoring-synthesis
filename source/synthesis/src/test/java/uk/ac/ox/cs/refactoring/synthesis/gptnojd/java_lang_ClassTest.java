
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_lang_ClassTest {
  @Test
  void newInstance() throws Exception {
    assertThat(synthesiseGPT("MyClass obj = MyClass.class.newInstance();\n\n", "MyClass obj = MyClass.class.getDeclaredConstructor().newInstance();\n", "java.lang.Class", "newInstance"), anyOf(contains("InvocationTargetException"), contains("NoSuchMethodException"), contains("ReflectiveOperationException"), contains("newInstance")));
  }
}