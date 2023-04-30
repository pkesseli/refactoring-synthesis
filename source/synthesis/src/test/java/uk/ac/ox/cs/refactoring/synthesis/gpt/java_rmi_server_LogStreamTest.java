
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_rmi_server_LogStreamTest {
  @Test
  void getDefaultStream() throws Exception {
    assertThat(synthesiseGPT("import java.rmi.server.LogStream;\n\npublic class MyClass {\n    public void myMethod() {\n        LogStream logStream = this.getDefaultStream();\n        // rest of the code\n    }\n}\n", "import java.util.logging.Logger;\n\npublic class MyClass {\n    public void myMethod() {\n        Logger logger = Logger.getGlobal();\n        // rest of the code\n    }\n}\n", "java.rmi.server.LogStream", "getDefaultStream"), Matchers.anything());
  }

  @Test
  void getOutputStream() throws Exception {
    assertThat(synthesiseGPT("import java.rmi.server.LogStream;\n\npublic class MyClass {\n    private LogStream logStream;\n\n    public MyClass() {\n        logStream = LogStream.getDefault();\n    }\n\n    public void myMethod() {\n        logStream.getOutputStream().println(\"Hello World!\");\n    }\n}\n", "import java.io.PrintStream;\nimport java.rmi.server.LogStream;\n\npublic class MyClass {\n    private PrintStream printStream;\n\n    public MyClass() {\n        printStream = System.out;\n    }\n\n    public void myMethod() {\n        printStream.println(\"Hello World!\");\n    }\n}\n", "java.rmi.server.LogStream", "getOutputStream"), Matchers.anything());
  }

  @Test
  void log() throws Exception {
    assertThat(synthesiseGPT("this.log(a);\n", "", "java.rmi.server.LogStream", "log", "java.lang.String"), Matchers.anything());
  }

  @Test
  void parseLevel() throws Exception {
    assertThat(synthesiseGPT("String a = \"INFO\";\nint level = parseLevel(a);\n\n", "String a = \"INFO\";\nint level = Level.parse(a).intValue();\n", "java.rmi.server.LogStream", "parseLevel", "java.lang.String"), Matchers.anything());
  }

  @Test
  void setDefaultStream() throws Exception {
    assertThat(synthesiseGPT("this.setDefaultStream(a);\n", "", "java.rmi.server.LogStream", "setDefaultStream", "java.io.PrintStream"), Matchers.anything());
  }

  @Test
  void setOutputStream() throws Exception {
    assertThat(synthesiseGPT("this.setOutputStream(a);\n", "", "java.rmi.server.LogStream", "setOutputStream", "java.io.OutputStream"), Matchers.anything());
  }

  @Test
  void toString1() throws Exception {
    assertThat(synthesiseGPT("public class MyClass {\n    public String myMethod() {\n        return this.toString();\n    }\n}\n\n", "public class MyClass {\n    public String myMethod() {\n        return getClass().getName() + \"@\" + Integer.toHexString(hashCode());\n    }\n}\n", "java.rmi.server.LogStream", "toString"), Matchers.anything());
  }

  @Test
  void write1() throws Exception {
    assertThat(synthesiseGPT("this.write(a, b, c);\n", "try {\n    byte[] bytes = Arrays.copyOfRange(a, b, b + c);\n    String message = new String(bytes, StandardCharsets.UTF_8);\n    System.out.println(message);\n} catch (Exception e) {\n    e.printStackTrace();\n}\n", "java.rmi.server.LogStream", "write", "byte[]", "int", "int"), Matchers.anything());
  }

  @Test
  void write2() throws Exception {
    assertThat(synthesiseGPT("this.write(a);\n\n", "", "java.rmi.server.LogStream", "write", "int"), Matchers.anything());
  }
}