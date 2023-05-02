
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_sql_DateTest {
  @Test
  void getHours() throws Exception {
    assertThat(synthesiseGPT("Date date = new Date();\nint hours = date.getHours();\n\n", "LocalDateTime localDateTime = LocalDateTime.now();\nint hours = localDateTime.getHour();\n", "java.sql.Date", "getHours"), Matchers.anything());
  }

  @Test
  void getMinutes() throws Exception {
    assertThat(synthesiseGPT("int minutes = this.getMinutes();\n\n", "int minutes = LocalDateTime.ofInstant(this.toInstant(), ZoneId.systemDefault()).getMinute();\n", "java.sql.Date", "getMinutes"), Matchers.anything());
  }

  @Test
  void getSeconds() throws Exception {
    assertThat(synthesiseGPT("public class MyClass {\n    private Date date;\n\n    public int getSeconds() {\n        return date.getSeconds();\n    }\n}\n\n", "public class MyClass {\n    private Date date;\n\n    public int getSeconds() {\n        return (int) (date.getTime() / 1000) % 60;\n    }\n}\n", "java.sql.Date", "getSeconds"), Matchers.anything());
  }

  @Test
  void setHours() throws Exception {
    assertThat(synthesiseGPT("this.setHours(a);\n\n", "LocalDateTime localDateTime = this.toLocalDateTime().withHour(a);\nthis.setTime(Timestamp.valueOf(localDateTime).getTime());\n", "java.sql.Date", "setHours", "int"), Matchers.anything());
  }

  @Test
  void setMinutes() throws Exception {
    assertThat(synthesiseGPT("this.setMinutes(a);\n\n", "Calendar cal = Calendar.getInstance();\ncal.setTime(this);\ncal.set(Calendar.MINUTE, a);\nthis.setTime(cal.getTimeInMillis());\n", "java.sql.Date", "setMinutes", "int"), Matchers.anything());
  }

  @Test
  void setSeconds() throws Exception {
    assertThat(synthesiseGPT("this.setSeconds(a);\n\n", "this.setTime(this.getTime() / 1000 * 1000 + a * 1000);\n", "java.sql.Date", "setSeconds", "int"), Matchers.anything());
  }
}