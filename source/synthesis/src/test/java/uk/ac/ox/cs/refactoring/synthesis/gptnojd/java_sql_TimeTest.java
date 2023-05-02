
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_sql_TimeTest {
  @Test
  void getDate() throws Exception {
    assertThat(synthesiseGPT("import java.sql.Time;\n\npublic class MyClass {\n    private Time time;\n\n    public Date getDate() {\n        return time.getDate();\n    }\n}\n\n", "import java.sql.Time;\nimport java.sql.Date;\n\npublic class MyClass {\n    private Time time;\n\n    public Date getDate() {\n        return new Date(time.getTime());\n    }\n}\n", "java.sql.Time", "getDate"), Matchers.anything());
  }

  @Test
  void getDay() throws Exception {
    assertThat(synthesiseGPT("public int getDayOfWeek() {\n    return this.getDay();\n}\n\n", "public int getDayOfWeek() {\n    Calendar calendar = Calendar.getInstance();\n    calendar.setTime(this);\n    return calendar.get(Calendar.DAY_OF_WEEK);\n}\n", "java.sql.Time", "getDay"), Matchers.anything());
  }

  @Test
  void getMonth() throws Exception {
    assertThat(synthesiseGPT("import java.sql.Time;\n\npublic class Example {\n    private Time time;\n\n    public int getMonth() {\n        return time.getMonth();\n    }\n}\n", "import java.time.LocalDate;\nimport java.time.ZoneId;\nimport java.time.temporal.ChronoField;\n\npublic class Example {\n    private Time time;\n\n    public int getMonth() {\n        LocalDate localDate = time.toLocalTime().atDate(LocalDate.now(ZoneId.systemDefault()));\n        return localDate.get(ChronoField.MONTH_OF_YEAR);\n    }\n}\n", "java.sql.Time", "getMonth"), Matchers.anything());
  }

  @Test
  void getYear() throws Exception {
    assertThat(synthesiseGPT("public int getYear() {\n    Time time = new Time(System.currentTimeMillis());\n    return time.getYear();\n}\n", "public int getYear() {\n    Calendar calendar = Calendar.getInstance();\n    calendar.setTimeInMillis(System.currentTimeMillis());\n    return calendar.get(Calendar.YEAR);\n}\n", "java.sql.Time", "getYear"), Matchers.anything());
  }

  @Test
  void setDate() throws Exception {
    assertThat(synthesiseGPT("this.setDate(a);\n\n", "this.setTime(this.getTime() + TimeUnit.DAYS.toMillis(a));\n", "java.sql.Time", "setDate", "int"), Matchers.anything());
  }

  @Test
  void setMonth() throws Exception {
    assertThat(synthesiseGPT("this.setMonth(a);\n\n", "Calendar cal = Calendar.getInstance();\ncal.setTime(this);\ncal.set(Calendar.MONTH, a-1);\nthis.setTime(cal.getTime().getTime());\n", "java.sql.Time", "setMonth", "int"), Matchers.anything());
  }

  @Test
  void setYear() throws Exception {
    assertThat(synthesiseGPT("this.setYear(a);\n\n", "LocalDateTime ldt = LocalDateTime.ofInstant(this.toInstant(), ZoneId.systemDefault());\nldt = ldt.withYear(a);\nthis.setTime(Time.valueOf(ldt.toLocalTime()));\n", "java.sql.Time", "setYear", "int"), Matchers.anything());
  }
}