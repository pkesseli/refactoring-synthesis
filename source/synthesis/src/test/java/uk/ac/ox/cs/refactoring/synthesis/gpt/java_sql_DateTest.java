
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_sql_DateTest {
  @Test
  void getHours() throws Exception {
assertThat (synthesiseGPT ("getHours" , "this.getHours();" , "\njava.sql.Date sqlDate = this; // Assuming 'this' is an instance of java.sql.Date\nLocalDate localDate = sqlDate.toLocalDate();\nLocalTime localTime = LocalDateTime.of(localDate, LocalTime.MIDNIGHT).toLocalTime();\nint hours = localTime.getHour();\n" , "java.sql.Date" , "getHours") , Matchers . anything ()) ;
  }

  @Test
  void getMinutes() throws Exception {
assertThat (synthesiseGPT ("getMinutes" , "this.getMinutes();" , "\njava.sql.Date sqlDate = // ... your java.sql.Date object\nLocalDateTime localDateTime = sqlDate.toLocalDate().atStartOfDay();\nint minutes = localDateTime.getMinute();\n" , "java.sql.Date" , "getMinutes") , Matchers . anything ()) ;
  }

  @Test
  void getSeconds() throws Exception {
assertThat (synthesiseGPT ("getSeconds" , "this.getSeconds();" , "" , "java.sql.Date" , "getSeconds") , Matchers . anything ()) ;
  }

  @Test
  void setHours() throws Exception {
assertThat (synthesiseGPT ("setHours" , "this.setHours(param0);" , "\n// Assuming 'this' is an instance of java.sql.Date and param0 is the hour of the day\nLocalDateTime dateTime = this.toLocalDate().atStartOfDay().withHour(param0);\n" , "java.sql.Date" , "setHours" , "int") , Matchers . anything ()) ;
  }

  @Test
  void setMinutes() throws Exception {
assertThat (synthesiseGPT ("setMinutes" , "this.setMinutes(param0);" , "\nimport java.sql.Date;\nimport java.time.LocalDateTime;\nimport java.time.ZoneId;\n\n// Assuming 'this' is a java.sql.Date object and param0 is the minutes value\nLocalDateTime localDateTime = this.toLocalDate().atStartOfDay();\nlocalDateTime = localDateTime.withMinute(param0);\nDate newSqlDate = Date.valueOf(localDateTime.toLocalDate());\n" , "java.sql.Date" , "setMinutes" , "int") , Matchers . anything ()) ;
  }

  @Test
  void setSeconds() throws Exception {
assertThat (synthesiseGPT ("setSeconds" , "this.setSeconds(param0);" , "\nCalendar calendar = Calendar.getInstance();\ncalendar.setTime(this); // Assuming 'this' is an instance of java.sql.Date\ncalendar.set(Calendar.SECOND, param0);\nthis.setTime(calendar.getTimeInMillis());\n" , "java.sql.Date" , "setSeconds" , "int") , Matchers . anything ()) ;
  }
}
