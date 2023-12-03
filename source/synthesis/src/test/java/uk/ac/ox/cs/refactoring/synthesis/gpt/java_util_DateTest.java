
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_util_DateTest {
  @Test
  void getDate() throws Exception {
assertThat (synthesiseGPT ("this.getDate();" , "\nLogger.logrb(Level.WARNING, null, null, null, \"Deprecated method called: getDate()\");\n;" , "java.util.Date" , "getDate") , anyOf (contains ("get"))) ;
  }

  @Test
  void getDay() throws Exception {
assertThat (synthesiseGPT ("this.getDay();" , "\nCalendar.getInstance().get(Calendar.DAY_OF_WEEK)\n;" , "java.util.Date" , "getDay") , anyOf (contains ("get"))) ;
  }

  @Test
  void getHours() throws Exception {
assertThat (synthesiseGPT ("this.getHours();" , "" , "java.util.Date" , "getHours") , anyOf (contains ("get"))) ;
  }

  @Test
  void getMinutes() throws Exception {
assertThat (synthesiseGPT ("this.getMinutes();" , "\nthis.toInstant().atZone(ZoneId.systemDefault()).getMinute()\n;" , "java.util.Date" , "getMinutes") , anyOf (contains ("get"))) ;
  }

  @Test
  void getMonth() throws Exception {
assertThat (synthesiseGPT ("this.getMonth();" , "\nthis.toInstant().atZone(ZoneId.systemDefault()).getMonthValue()\n;" , "java.util.Date" , "getMonth") , anyOf (contains ("get"))) ;
  }

  @Test
  void getSeconds() throws Exception {
assertThat (synthesiseGPT ("this.getSeconds();" , "\nthis.toInstant().getEpochSecond() % 60\n;" , "java.util.Date" , "getSeconds") , anyOf (contains ("get"))) ;
  }

  @Test
  void getTimezoneOffset() throws Exception {
assertThat (synthesiseGPT ("this.getTimezoneOffset();" , "\nthis.toInstant().atZone(ZoneId.systemDefault()).getOffset().getTotalSeconds() / 60\n;" , "java.util.Date" , "getTimezoneOffset") , anyOf (contains ("DST_OFFSET)) / "))) ;
  }

  @Test
  void getYear() throws Exception {
assertThat (synthesiseGPT ("this.getYear();" , "\nthis.getYear() + 1900\n;" , "java.util.Date" , "getYear") , anyOf (contains ("get"))) ;
  }

  @Test
  void parse() throws Exception {
assertThat (synthesiseGPT ("this.parse(param0);" , "\nDateFormat dateFormat = new SimpleDateFormat(\"yyyy-MM-dd\");\nDate date = dateFormat.parse(param0);\n;" , "java.util.Date" , "parse" , "java.lang.String") , anyOf (contains ("parse"))) ;
  }

  @Test
  void setDate() throws Exception {
assertThat (synthesiseGPT ("this.setDate(param0);" , "\nCalendar calendar = Calendar.getInstance();\ncalendar.setTime(this);\ncalendar.set(Calendar.DATE, param0);\nthis.setTime(calendar.getTimeInMillis());\n;" , "java.util.Date" , "setDate" , "int") , anyOf (contains ("set"))) ;
  }

  @Test
  void setHours() throws Exception {
assertThat (synthesiseGPT ("this.setHours(param0);" , "\nCalendar calendar = Calendar.getInstance();\ncalendar.setTime(this);\ncalendar.set(Calendar.HOUR_OF_DAY, param0);\nthis.setTime(calendar.getTimeInMillis());\n;" , "java.util.Date" , "setHours" , "int") , anyOf (contains ("set"))) ;
  }

  @Test
  void setMinutes() throws Exception {
assertThat (synthesiseGPT ("this.setMinutes(param0);" , "\nCalendar calendar = Calendar.getInstance();\ncalendar.setTime(this);\ncalendar.set(Calendar.MINUTE, param0);\nthis.setTime(calendar.getTimeInMillis());\n;" , "java.util.Date" , "setMinutes" , "int") , anyOf (contains ("set"))) ;
  }

  @Test
  void setMonth() throws Exception {
assertThat (synthesiseGPT ("this.setMonth(param0);" , "\nCalendar calendar = Calendar.getInstance();\ncalendar.set(Calendar.MONTH, param0);\nDate date = calendar.getTime();\n;" , "java.util.Date" , "setMonth" , "int") , anyOf (contains ("set"))) ;
  }

  @Test
  void setSeconds() throws Exception {
assertThat (synthesiseGPT ("this.setSeconds(param0);" , "" , "java.util.Date" , "setSeconds" , "int") , anyOf (contains ("set"))) ;
  }

  @Test
  void setYear() throws Exception {
assertThat (synthesiseGPT ("this.setYear(param0);" , "\nCalendar calendar = Calendar.getInstance();\ncalendar.set(Calendar.YEAR, param0);\nDate date = calendar.getTime();\n;" , "java.util.Date" , "setYear" , "int") , anyOf (contains ("set"))) ;
  }

  @Test
  void toGMTString() throws Exception {
assertThat (synthesiseGPT ("this.toGMTString();" , "\nDateFormat df = new SimpleDateFormat(\"EEE, dd MMM yyyy HH:mm:ss z\");\ndf.setTimeZone(TimeZone.getTimeZone(\"GMT\"));\ndf.format(this);\n;" , "java.util.Date" , "toGMTString") , anyOf (contains ("TimeZone") , contains ("format"))) ;
  }

  @Test
  void toLocaleString() throws Exception {
assertThat (synthesiseGPT ("this.toLocaleString();" , "" , "java.util.Date" , "toLocaleString") , anyOf (contains ("format"))) ;
  }

  @Test
  void UTC() throws Exception {
assertThat (synthesiseGPT ("this.UTC(param0, param1, param2, param3, param4, param5);" , "\nInstant instant = Instant.ofEpochSecond(0)\n    .plus(param0, ChronoUnit.YEARS)\n    .plus(param1, ChronoUnit.MONTHS)\n    .plus(param2, ChronoUnit.DAYS)\n    .plus(param3, ChronoUnit.HOURS)\n    .plus(param4, ChronoUnit.MINUTES)\n    .plus(param5, ChronoUnit.SECONDS);\nlong epochMillis = instant.toEpochMilli();\n;" , "java.util.Date" , "UTC" , "int" , "int" , "int" , "int" , "int" , "int") , anyOf (contains ("GregorianCalendar") , contains ("TimeZone") , contains ("getTime") , contains ("set"))) ;
  }
}
