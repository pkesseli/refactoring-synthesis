
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_util_PropertiesTest {
  @Test
  void save() throws Exception {
assertThat (synthesiseGPT ("this.save(param0, param1);" , "\nProperties props = new Properties();\n// set properties\ntry (OutputStream output = new FileOutputStream(\"file.properties\")) {\n    props.store(output, \"Properties file\");\n} catch (IOException e) {\n    e.printStackTrace();\n}\n;" , "java.util.Properties" , "save" , "java.io.OutputStream" , "java.lang.String") , anyOf (contains ("store") , contains ("storeToXML"))) ;
  }
}
