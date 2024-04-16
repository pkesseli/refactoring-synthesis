
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseNeural;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class java_sql_DateTest {
  @Disabled("No replacement")
  @Test
  void getHours() throws Exception {
assertThat (synthesiseNeural ("getHours" , "this.getHours();" , "\nLocalDateTime localDateTime = LocalDateTime.ofInstant(this.toInstant(), ZoneId.systemDefault());\nlocalDateTime.getHour();\n" , "java.sql.Date" , "getHours") , Matchers . anything ()) ;
  }

  @Disabled("No replacement")
  @Test
  void getMinutes() throws Exception {
assertThat (synthesiseNeural ("getMinutes" , "this.getMinutes();" , "\nthis.toLocalDateTime().getMinute();\n" , "java.sql.Date" , "getMinutes") , Matchers . anything ()) ;
  }

  @Disabled("No replacement")
  @Test
  void getSeconds() throws Exception {
assertThat (synthesiseNeural ("getSeconds" , "this.getSeconds();" , "\nthis.toLocalDateTime().getSecond();\n" , "java.sql.Date" , "getSeconds") , Matchers . anything ()) ;
  }

  @Disabled("No replacement")
  @Test
  void setHours() throws Exception {
assertThat (synthesiseNeural ("setHours" , "this.setHours(param0);" , "\nthis.toInstant().atZone(ZoneId.systemDefault()).withHour(param0).toLocalDateTime().toLocalDate();\n" , "java.sql.Date" , "setHours" , "int") , Matchers . anything ()) ;
  }

  @Disabled("No replacement")
  @Test
  void setMinutes() throws Exception {
assertThat (synthesiseNeural ("setMinutes" , "this.setMinutes(param0);" , "\nthis.setMinutes(param0);\n" , "java.sql.Date" , "setMinutes" , "int") , Matchers . anything ()) ;
  }

  @Disabled("No replacement")
  @Test
  void setSeconds() throws Exception {
assertThat (synthesiseNeural ("setSeconds" , "this.setSeconds(param0);" , "\nthis.toInstant().atZone(ZoneId.systemDefault()).withSecond(param0).toLocalDateTime().toLocalDate();\n" , "java.sql.Date" , "setSeconds" , "int") , Matchers . anything ()) ;
  }
}
