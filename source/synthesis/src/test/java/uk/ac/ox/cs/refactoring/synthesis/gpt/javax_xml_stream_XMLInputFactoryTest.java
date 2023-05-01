
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_xml_stream_XMLInputFactoryTest {
  @Test
  void newInstance() throws Exception {
    assertThat(synthesiseGPT("XMLInputFactory factory = XMLInputFactory.newInstance(\"com.sun.xml.internal.stream.XMLInputFactoryImpl\", getClass().getClassLoader());\n\n", "XMLInputFactory factory = XMLInputFactory.newFactory(\"com.sun.xml.internal.stream.XMLInputFactoryImpl\", getClass().getClassLoader());\n", "javax.xml.stream.XMLInputFactory", "newInstance", "java.lang.String", "java.lang.ClassLoader"), anyOf(contains("newFactory")));
  }
}