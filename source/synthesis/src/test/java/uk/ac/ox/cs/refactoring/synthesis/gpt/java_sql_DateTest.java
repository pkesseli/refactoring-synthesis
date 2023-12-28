
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class java_sql_DateTest {
  @Disabled("No replacement")
  @Test
  void getHours() throws Exception {
assertThat (synthesiseGPT ("getHours" , "this.getHours();" , "\nfinal Calendar calendar = Calendar.getInstance();\ncalendar.setTime(this);\ncalendar.get(Calendar.HOUR_OF_DAY);\n" , "java.sql.Date" , "getHours") , Matchers . anything ()) ;
  }

  @Disabled("No replacement")
  @Test
  void getMinutes() throws Exception {
assertThat (synthesiseGPT ("getMinutes" , "this.getMinutes();" , "\nfinal Calendar calendar = Calendar.getInstance();\ncalendar.setTime(this);\ncalendar.get(Calendar.MINUTE);\n" , "java.sql.Date" , "getMinutes") , Matchers . anything ()) ;
  }

  @Disabled("No replacement")
  @Test
  void getSeconds() throws Exception {
assertThat (synthesiseGPT ("getSeconds" , "this.getSeconds();" , "\nfinal Calendar calendar = Calendar.getInstance();\ncalendar.setTime(this);\ncalendar.get(Calendar.SECOND);\n" , "java.sql.Date" , "getSeconds") , Matchers . anything ()) ;
  }

  @Disabled("No replacement")
  @Test
  void setHours() throws Exception {
assertThat (synthesiseGPT ("setHours" , "this.setHours(param0);" , "\nfinal Calendar calendar = Calendar.getInstance();\ncalendar.setTime(this);\ncalendar.set(Calendar.HOUR_OF_DAY, param0);\nthis.setTime(calendar.getTimeInMillis());\n" , "java.sql.Date" , "setHours" , "int") , Matchers . anything ()) ;
  }

  @Disabled("No replacement")
  @Test
  void setMinutes() throws Exception {
assertThat (synthesiseGPT ("setMinutes" , "this.setMinutes(param0);" , "\nfinal Calendar calendar = Calendar.getInstance();\ncalendar.setTime(this);\ncalendar.set(Calendar.MINUTE, param0);\nthis.setTime(calendar.getTimeInMillis());\n" , "java.sql.Date" , "setMinutes" , "int") , Matchers . anything ()) ;
  }

  @Disabled("No replacement")
  @Test
  void setSeconds() throws Exception {
assertThat (synthesiseGPT ("setSeconds" , "this.setSeconds(param0);" , "\nfinal Calendar calendar = Calendar.getInstance();\ncalendar.setTime(this);\ncalendar.set(Calendar.SECOND, param0);\nthis.setTime(calendar.getTimeInMillis());\n" , "java.sql.Date" , "setSeconds" , "int") , Matchers . anything ()) ;
  }
}
