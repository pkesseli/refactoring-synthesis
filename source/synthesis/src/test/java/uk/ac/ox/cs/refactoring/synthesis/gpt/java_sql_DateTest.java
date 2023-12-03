
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
assertThat (synthesiseGPT ("this.getHours();" , "\nthis.toLocalTime().getHour()\n;" , "java.sql.Date" , "getHours") , Matchers . anything ()) ;
  }

  @Test
  void getMinutes() throws Exception {
assertThat (synthesiseGPT ("this.getMinutes();" , "\nLocalTime time = LocalTime.now();\nint minutes = time.getMinute();\n;" , "java.sql.Date" , "getMinutes") , Matchers . anything ()) ;
  }

  @Test
  void getSeconds() throws Exception {
assertThat (synthesiseGPT ("this.getSeconds();" , "\nthis.getCharacterStream()\n;" , "java.sql.Date" , "getSeconds") , Matchers . anything ()) ;
  }

  @Test
  void setHours() throws Exception {
assertThat (synthesiseGPT ("this.setHours(param0);" , "\njava.util.Calendar cal = java.util.Calendar.getInstance();\ncal.setTime(this);\ncal.set(java.util.Calendar.HOUR_OF_DAY, param0);\njava.sql.Date newDate = new java.sql.Date(cal.getTimeInMillis());\nthis.setTime(newDate.getTime());\n;" , "java.sql.Date" , "setHours" , "int") , Matchers . anything ()) ;
  }

  @Test
  void setMinutes() throws Exception {
assertThat (synthesiseGPT ("this.setMinutes(param0);" , "\njava.util.Calendar cal = java.util.Calendar.getInstance();\ncal.setTime(this);\ncal.set(java.util.Calendar.MINUTE, param0);\njava.sql.Date newDate = new java.sql.Date(cal.getTimeInMillis());\nthis.setTime(newDate.getTime());\n;" , "java.sql.Date" , "setMinutes" , "int") , Matchers . anything ()) ;
  }

  @Test
  void setSeconds() throws Exception {
assertThat (synthesiseGPT ("this.setSeconds(param0);" , "" , "java.sql.Date" , "setSeconds" , "int") , Matchers . anything ()) ;
  }
}
