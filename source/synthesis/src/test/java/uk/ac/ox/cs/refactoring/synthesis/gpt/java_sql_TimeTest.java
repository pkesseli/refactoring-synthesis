
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseNeural;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class java_sql_TimeTest {
  @Disabled("No replacement")
  @Test
  void getDate() throws Exception {
assertThat (synthesiseNeural ("getDate" , "this.getDate();" , "\nthis.toLocalTime().toLocalDate();\n" , "java.sql.Time" , "getDate") , Matchers . anything ()) ;
  }

  @Disabled("No replacement")
  @Test
  void getDay() throws Exception {
assertThat (synthesiseNeural ("getDay" , "this.getDay();" , "\n// There is no direct replacement for getDay() method.\n// Instead, we can use the following code to get the day of the month.\nLocalDateTime localDateTime = LocalDateTime.ofInstant(this.toInstant(), ZoneId.systemDefault());\nint dayOfMonth = localDateTime.getDayOfMonth();\n" , "java.sql.Time" , "getDay") , Matchers . anything ()) ;
  }

  @Disabled("No replacement")
  @Test
  void getMonth() throws Exception {
assertThat (synthesiseNeural ("getMonth" , "this.getMonth();" , "" , "java.sql.Time" , "getMonth") , Matchers . anything ()) ;
  }

  @Disabled("No replacement")
  @Test
  void getYear() throws Exception {
assertThat (synthesiseNeural ("getYear" , "this.getYear();" , "" , "java.sql.Time" , "getYear") , Matchers . anything ()) ;
  }

  @Disabled("No replacement")
  @Test
  void setDate() throws Exception {
assertThat (synthesiseNeural ("setDate" , "this.setDate(param0);" , "" , "java.sql.Time" , "setDate" , "int") , Matchers . anything ()) ;
  }

  @Disabled("No replacement")
  @Test
  void setMonth() throws Exception {
assertThat (synthesiseNeural ("setMonth" , "this.setMonth(param0);" , "" , "java.sql.Time" , "setMonth" , "int") , Matchers . anything ()) ;
  }

  @Disabled("No replacement")
  @Test
  void setYear() throws Exception {
assertThat (synthesiseNeural ("setYear" , "this.setYear(param0);" , "" , "java.sql.Time" , "setYear" , "int") , Matchers . anything ()) ;
  }
}
