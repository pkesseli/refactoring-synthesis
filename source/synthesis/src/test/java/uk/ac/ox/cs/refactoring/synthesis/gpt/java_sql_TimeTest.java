
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
assertThat (synthesiseGPT ("this.getDate();" , "\nCalendar calendar = Calendar.getInstance();\ncalendar.setTime(this);\nint minute = calendar.get(Calendar.MINUTE);\n;" , "java.sql.Time" , "getDate") , Matchers . anything ()) ;
  }

  @Test
  void getDay() throws Exception {
assertThat (synthesiseGPT ("this.getDay();" , "\nCalendar calendar = Calendar.getInstance();\ncalendar.setTime(this);\nint day = calendar.get(Calendar.DAY_OF_MONTH);\n;" , "java.sql.Time" , "getDay") , Matchers . anything ()) ;
  }

  @Test
  void getMonth() throws Exception {
assertThat (synthesiseGPT ("this.getMonth();" , "" , "java.sql.Time" , "getMonth") , Matchers . anything ()) ;
  }

  @Test
  void getYear() throws Exception {
assertThat (synthesiseGPT ("this.getYear();" , "\nCalendar calendar = Calendar.getInstance();\ncalendar.setTime(this);\nint year = calendar.get(Calendar.YEAR);\n;" , "java.sql.Time" , "getYear") , Matchers . anything ()) ;
  }

  @Test
  void setDate() throws Exception {
assertThat (synthesiseGPT ("this.setDate(param0);" , "```java\nCalendar cal = Calendar.getInstance();\ncal.setTime(this);\ncal.set(Calendar.DATE, param0);\nthis.setTime(cal.getTimeInMillis());\n```;" , "java.sql.Time" , "setDate" , "int") , Matchers . anything ()) ;
  }

  @Test
  void setMonth() throws Exception {
assertThat (synthesiseGPT ("this.setMonth(param0);" , "" , "java.sql.Time" , "setMonth" , "int") , Matchers . anything ()) ;
  }

  @Test
  void setYear() throws Exception {
assertThat (synthesiseGPT ("this.setYear(param0);" , "\nCalendar cal = Calendar.getInstance();\ncal.setTime(this);\ncal.set(Calendar.YEAR, param0);\nthis.setTime(cal.getTime().getTime());\n;" , "java.sql.Time" , "setYear" , "int") , Matchers . anything ()) ;
  }
}
