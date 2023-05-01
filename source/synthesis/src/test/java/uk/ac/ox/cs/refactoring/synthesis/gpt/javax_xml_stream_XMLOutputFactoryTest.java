
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_xml_stream_XMLOutputFactoryTest {
  @Test
  void newInstance() throws Exception {
    assertThat(synthesiseGPT("XMLOutputFactory factory = XMLOutputFactory.newInstance(\"javax.xml.stream.XMLOutputFactory\", getClass().getClassLoader());\n\n", "XMLOutputFactory factory = XMLOutputFactory.newFactory(\"javax.xml.stream.XMLOutputFactory\", getClass().getClassLoader());\n", "javax.xml.stream.XMLOutputFactory", "newInstance", "java.lang.String", "java.lang.ClassLoader"), anyOf(contains("newFactory")));
  }
}