
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_sql_TimeTest {
  @Test
  void getDate() throws Exception {
    assertThat(synthesiseGPT("java.sql.Time time = new java.sql.Time(System.currentTimeMillis());\nDate date = time.getDate();\n\n", "java.sql.Time time = new java.sql.Time(System.currentTimeMillis());\nDate date = new Date(time.getTime());\n", "java.sql.Time", "getDate"), Matchers.anything());
  }

  @Test
  void getDay() throws Exception {
    assertThat(synthesiseGPT("int day = this.getDay();\n\n", "LocalDate localDate = LocalDate.now();\nint day = localDate.getDayOfMonth();\n", "java.sql.Time", "getDay"), Matchers.anything());
  }

  @Test
  void getMonth() throws Exception {
    assertThat(synthesiseGPT("<code before refactoring here>\njava.sql.Time time = new java.sql.Time(System.currentTimeMillis());\nint month = time.getMonth();\n\n<code after refactoring here>\njava.sql.Time time = new java.sql.Time(System.currentTimeMillis());\nCalendar calendar = Calendar.getInstance();\ncalendar.setTime(time);\nint month = calendar.get(Calendar.MONTH);\n", "", "java.sql.Time", "getMonth"), Matchers.anything());
  }

  @Test
  void getYear() throws Exception {
    assertThat(synthesiseGPT("int year = this.getYear() + 1900;\n\n", "LocalDateTime dateTime = LocalDateTime.ofInstant(this.toInstant(), ZoneId.systemDefault());\nint year = dateTime.getYear();\n", "java.sql.Time", "getYear"), Matchers.anything());
  }

  @Test
  void setDate() throws Exception {
    assertThat(synthesiseGPT("this.setDate(a);\n\n", "this.setTime(new java.sql.Time(a * 24L * 60L * 60L * 1000L).getTime());\n", "java.sql.Time", "setDate", "int"), Matchers.anything());
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