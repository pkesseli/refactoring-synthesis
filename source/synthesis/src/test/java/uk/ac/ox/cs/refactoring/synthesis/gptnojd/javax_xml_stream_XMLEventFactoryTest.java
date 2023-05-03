
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_xml_stream_XMLEventFactoryTest {
  @Test
  void newInstance() throws Exception {
    assertThat(synthesiseGPT("XMLEventFactory eventFactory = XMLEventFactory.newInstance(\"com.sun.xml.internal.stream.events.XMLEventFactoryImpl\", this.getClass().getClassLoader());\n\n", "XMLEventFactory eventFactory = XMLEventFactory.newFactory();\n", "javax.xml.stream.XMLEventFactory", "newInstance", "java.lang.String", "java.lang.ClassLoader"), anyOf(contains("newFactory")));
  }
}