
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_io_ObjectOutputStream_PutFieldTest {
  @Test
  void write() throws Exception {
    assertThat(synthesiseGPT("private void writeObject(ObjectOutputStream out) throws IOException {\n    PutField fields = out.putFields();\n    fields.put(\"field1\", this.field1);\n    fields.put(\"field2\", this.field2);\n    fields.put(\"field3\", this.field3);\n    fields.put(\"field4\", this.field4);\n    fields.put(\"field5\", this.field5);\n    fields.put(\"field6\", this.field6);\n    fields.put(\"field7\", this.field7);\n    fields.put(\"field8\", this.field8);\n    fields.put(\"field9\", this.field9);\n    fields.put(\"field10\", this.field10);\n    fields.put(\"field11\", this.field11);\n    fields.put(\"field12\", this.field12);\n    fields.put(\"field13\", this.field13);\n    fields.put(\"field14\", this.field14);\n    fields.put(\"field15\", this.field15);\n    fields.put(\"field16\", this.field16);\n    fields.put(\"field17\", this.field17);\n    fields.put(\"field18\", this.field18);\n    fields.put(\"field19\", this.field19);\n    fields.put(\"field20\", this.field20);\n    out.writeFields();\n}\n\n", "private void writeObject(ObjectOutputStream out) throws IOException {\n    out.defaultWriteObject();\n}\n", "java.io.ObjectOutputStream.PutField", "write", "java.io.ObjectOutput"), anyOf(contains("PutField"), contains("writeFields")));
  }
}