
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
assertThat (synthesiseGPT ("getDate" , "this.getDate();" , "\nfinal Calendar calendar = Calendar.getInstance();\ncalendar.setTime(this);\ncalendar.get(Calendar.DAY_OF_MONTH);\n" , "java.util.Date" , "getDate") , anyOf (contains ("get"))) ;
  }

  @Test
  void getDay() throws Exception {
assertThat (synthesiseGPT ("getDay" , "this.getDay();" , "\nfinal Calendar calendar = Calendar.getInstance();\ncalendar.setTime(this);\ncalendar.get(Calendar.DAY_OF_WEEK);\n" , "java.util.Date" , "getDay") , anyOf (contains ("get"))) ;
  }

  @Test
  void getHours() throws Exception {
assertThat (synthesiseGPT ("getHours" , "this.getHours();" , "\nfinal Calendar calendar = Calendar.getInstance();\ncalendar.setTime(this);\ncalendar.get(Calendar.HOUR_OF_DAY);\n" , "java.util.Date" , "getHours") , anyOf (contains ("get"))) ;
  }

  @Test
  void getMinutes() throws Exception {
assertThat (synthesiseGPT ("getMinutes" , "this.getMinutes();" , "\nfinal Calendar calendar = Calendar.getInstance();\ncalendar.setTime(this);\ncalendar.get(Calendar.MINUTE);\n" , "java.util.Date" , "getMinutes") , anyOf (contains ("get"))) ;
  }

  @Test
  void getMonth() throws Exception {
assertThat (synthesiseGPT ("getMonth" , "this.getMonth();" , "\nfinal Calendar calendar = Calendar.getInstance();\ncalendar.setTime(this);\ncalendar.get(Calendar.MONTH);\n" , "java.util.Date" , "getMonth") , anyOf (contains ("get"))) ;
  }

  @Test
  void getSeconds() throws Exception {
assertThat (synthesiseGPT ("getSeconds" , "this.getSeconds();" , "\nfinal Calendar calendar = Calendar.getInstance();\ncalendar.setTime(this);\ncalendar.get(Calendar.SECOND);\n" , "java.util.Date" , "getSeconds") , anyOf (contains ("get"))) ;
  }

  @Test
  void getTimezoneOffset() throws Exception {
assertThat (synthesiseGPT ("getTimezoneOffset" , "this.getTimezoneOffset();" , "\nfinal Calendar calendar = Calendar.getInstance();\ncalendar.setTime(this);\n-(calendar.get(Calendar.ZONE_OFFSET) + calendar.get(Calendar.DST_OFFSET)) / (60 * 1000);\n" , "java.util.Date" , "getTimezoneOffset") , anyOf (contains ("DST_OFFSET)) / "))) ;
  }

  @Test
  void getYear() throws Exception {
assertThat (synthesiseGPT ("getYear" , "this.getYear();" , "\nfinal Calendar calendar = Calendar.getInstance();\ncalendar.setTime(this);\ncalendar.get(Calendar.YEAR) - 1900;\n" , "java.util.Date" , "getYear") , anyOf (contains ("get"))) ;
  }

  @Test
  void parse() throws Exception {
assertThat (synthesiseGPT ("parse" , "this.parse(param0);" , "\nfinal DateFormat dateFormat = new SimpleDateFormat(\"EEE MMM dd HH:mm:ss zzz yyyy\");\ndateFormat.parse(param0);\n" , "java.util.Date" , "parse" , "java.lang.String") , anyOf (contains ("parse"))) ;
  }

  @Test
  void setDate() throws Exception {
assertThat (synthesiseGPT ("setDate" , "this.setDate(param0);" , "\nfinal Calendar calendar = Calendar.getInstance();\ncalendar.setTime(this);\ncalendar.set(Calendar.DAY_OF_MONTH, param0);\nthis.setTime(calendar.getTimeInMillis());\n" , "java.util.Date" , "setDate" , "int") , anyOf (contains ("set"))) ;
  }

  @Test
  void setHours() throws Exception {
assertThat (synthesiseGPT ("setHours" , "this.setHours(param0);" , "\nfinal Calendar calendar = Calendar.getInstance();\ncalendar.setTime(this);\ncalendar.set(Calendar.HOUR_OF_DAY, param0);\nthis.setTime(calendar.getTimeInMillis());\n" , "java.util.Date" , "setHours" , "int") , anyOf (contains ("set"))) ;
  }

  @Test
  void setMinutes() throws Exception {
assertThat (synthesiseGPT ("setMinutes" , "this.setMinutes(param0);" , "\nfinal Calendar calendar = Calendar.getInstance();\ncalendar.setTime(this);\ncalendar.set(Calendar.MINUTE, param0);\nthis.setTime(calendar.getTimeInMillis());\n" , "java.util.Date" , "setMinutes" , "int") , anyOf (contains ("set"))) ;
  }

  @Test
  void setMonth() throws Exception {
assertThat (synthesiseGPT ("setMonth" , "this.setMonth(param0);" , "\nfinal Calendar calendar = Calendar.getInstance();\ncalendar.setTime(this);\ncalendar.set(Calendar.MONTH, param0);\nthis.setTime(calendar.getTimeInMillis());\n" , "java.util.Date" , "setMonth" , "int") , anyOf (contains ("set"))) ;
  }

  @Test
  void setSeconds() throws Exception {
assertThat (synthesiseGPT ("setSeconds" , "this.setSeconds(param0);" , "\nfinal Calendar calendar = Calendar.getInstance();\ncalendar.setTime(this);\ncalendar.set(Calendar.SECOND, param0);\nthis.setTime(calendar.getTimeInMillis());\n" , "java.util.Date" , "setSeconds" , "int") , anyOf (contains ("set"))) ;
  }

  @Test
  void setYear() throws Exception {
assertThat (synthesiseGPT ("setYear" , "this.setYear(param0);" , "\nfinal Calendar calendar = Calendar.getInstance();\ncalendar.setTime(this);\ncalendar.set(Calendar.YEAR, param0 + 1900);\nthis.setTime(calendar.getTimeInMillis());\n" , "java.util.Date" , "setYear" , "int") , anyOf (contains ("set"))) ;
  }

  @Test
  void toGMTString() throws Exception {
assertThat (synthesiseGPT ("toGMTString" , "this.toGMTString();" , "\nfinal DateFormat dateFormat = new SimpleDateFormat(\"EEE, dd MMM yyyy HH:mm:ss z\", Locale.US);\ndateFormat.setTimeZone(TimeZone.getTimeZone(\"GMT\"));\ndateFormat.format(this);\n" , "java.util.Date" , "toGMTString") , anyOf (contains ("TimeZone") , contains ("format"))) ;
  }

  @Test
  void toLocaleString() throws Exception {
assertThat (synthesiseGPT ("toLocaleString" , "this.toLocaleString();" , "\nfinal DateFormat dateFormat = DateFormat.getDateTimeInstance();\ndateFormat.format(this);\n" , "java.util.Date" , "toLocaleString") , anyOf (contains ("format"))) ;
  }

  @Test
  void UTC() throws Exception {
assertThat (synthesiseGPT ("UTC" , "this.UTC(param0, param1, param2, param3, param4, param5);" , "\nfinal Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(\"UTC\"));\ncalendar.set(param0 + 1900, param1, param2, param3, param4, param5);\ncalendar.getTime().getTime();\n" , "java.util.Date" , "UTC" , "int" , "int" , "int" , "int" , "int" , "int") , anyOf (contains ("GregorianCalendar") , contains ("TimeZone") , contains ("getTime") , contains ("set"))) ;
  }
}
