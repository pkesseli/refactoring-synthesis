
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
    assertThat(synthesiseGPT("Date date = new Date();\nint dayOfMonth = date.getDate();\n\n", "Calendar calendar = Calendar.getInstance();\nint dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);\n", "java.util.Date", "getDate"), anyOf(contains("get")));
  }

  @Test
  void getDay() throws Exception {
    assertThat(synthesiseGPT("Date date = new Date();\nint dayOfWeek = date.getDay();\n\n", "Calendar calendar = Calendar.getInstance();\nint dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);\n", "java.util.Date", "getDay"), anyOf(contains("get")));
  }

  @Test
  void getHours() throws Exception {
    assertThat(synthesiseGPT("Date date = new Date();\nint hours = date.getHours();\n\n", "Calendar calendar = Calendar.getInstance();\nint hours = calendar.get(Calendar.HOUR_OF_DAY);\n", "java.util.Date", "getHours"), anyOf(contains("get")));
  }

  @Test
  void getMinutes() throws Exception {
    assertThat(synthesiseGPT("Date date = new Date();\nint minutes = date.getMinutes();\n\n", "Calendar calendar = Calendar.getInstance();\nint minutes = calendar.get(Calendar.MINUTE);\n", "java.util.Date", "getMinutes"), anyOf(contains("get")));
  }

  @Test
  void getMonth() throws Exception {
    assertThat(synthesiseGPT("Date date = new Date();\nint month = date.getMonth();\n\n", "Calendar calendar = Calendar.getInstance();\nint month = calendar.get(Calendar.MONTH);\n", "java.util.Date", "getMonth"), anyOf(contains("get")));
  }

  @Test
  void getSeconds() throws Exception {
    assertThat(synthesiseGPT("int seconds = this.getSeconds();\n\n", "Calendar calendar = Calendar.getInstance();\nint seconds = calendar.get(Calendar.SECOND);\n", "java.util.Date", "getSeconds"), anyOf(contains("get")));
  }

  @Test
  void getTimezoneOffset() throws Exception {
    assertThat(synthesiseGPT("this.getTimezoneOffset();\n\n", "-(Calendar.getInstance().get(Calendar.ZONE_OFFSET) + Calendar.getInstance().get(Calendar.DST_OFFSET)) / (60 * 1000);\n", "java.util.Date", "getTimezoneOffset"), anyOf(contains("DST_OFFSET)) / ")));
  }

  @Test
  void getYear() throws Exception {
    assertThat(synthesiseGPT("Date date = new Date();\nint year = date.getYear();\n\n", "Date date = new Date();\nCalendar calendar = Calendar.getInstance();\ncalendar.setTime(date);\nint year = calendar.get(Calendar.YEAR) - 1900;\n", "java.util.Date", "getYear"), anyOf(contains("get")));
  }

  @Test
  void parse() throws Exception {
    assertThat(synthesiseGPT("Date date = new Date.parse(\"2022-01-01\");\n\n", "DateFormat dateFormat = new SimpleDateFormat(\"yyyy-MM-dd\");\nDate date = dateFormat.parse(\"2022-01-01\");\n", "java.util.Date", "parse", "java.lang.String"), anyOf(contains("parse")));
  }

  @Test
  void setDate() throws Exception {
    assertThat(synthesiseGPT("this.setDate(a);\n\n", "Calendar calendar = Calendar.getInstance();\ncalendar.setTime(this);\ncalendar.set(Calendar.DAY_OF_MONTH, a);\nthis.setTime(calendar.getTime().getTime());\n", "java.util.Date", "setDate", "int"), anyOf(contains("set")));
  }

  @Test
  void setHours() throws Exception {
    assertThat(synthesiseGPT("this.setHours(a);\n\n", "Calendar calendar = Calendar.getInstance();\ncalendar.setTime(this);\ncalendar.set(Calendar.HOUR_OF_DAY, a);\nthis.setTime(calendar.getTime().getTime());\n", "java.util.Date", "setHours", "int"), anyOf(contains("set")));
  }

  @Test
  void setMinutes() throws Exception {
    assertThat(synthesiseGPT("this.setMinutes(a);\n\n", "Calendar calendar = Calendar.getInstance();\ncalendar.setTime(this);\ncalendar.set(Calendar.MINUTE, a);\nthis.setTime(calendar.getTime().getTime());\n", "java.util.Date", "setMinutes", "int"), anyOf(contains("set")));
  }

  @Test
  void setMonth() throws Exception {
    assertThat(synthesiseGPT("this.setMonth(a);\n\n", "Calendar cal = Calendar.getInstance();\ncal.setTime(this);\ncal.set(Calendar.MONTH, a);\nthis.setTime(cal.getTime());\n", "java.util.Date", "setMonth", "int"), anyOf(contains("set")));
  }

  @Test
  void setSeconds() throws Exception {
    assertThat(synthesiseGPT("this.setSeconds(a);\n\n", "Calendar calendar = Calendar.getInstance();\ncalendar.setTime(this);\ncalendar.set(Calendar.SECOND, a);\nthis.setTime(calendar.getTime().getTime());\n", "java.util.Date", "setSeconds", "int"), anyOf(contains("set")));
  }

  @Test
  void setYear() throws Exception {
    assertThat(synthesiseGPT("this.setYear(a);\n\n", "Calendar cal = Calendar.getInstance();\ncal.setTime(this);\ncal.set(Calendar.YEAR, a + 1900);\nthis.setTime(cal.getTime().getTime());\n", "java.util.Date", "setYear", "int"), anyOf(contains("set")));
  }

  @Test
  void toGMTString() throws Exception {
    assertThat(synthesiseGPT("public class MyClass {\n    private Date myDate;\n\n    public String getGMTString() {\n        return myDate.toGMTString();\n    }\n}\n\n", "public class MyClass {\n    private Date myDate;\n    private DateFormat dateFormat;\n\n    public MyClass() {\n        dateFormat = new SimpleDateFormat(\"EEE, dd MMM yyyy HH:mm:ss z\", Locale.US);\n        dateFormat.setTimeZone(TimeZone.getTimeZone(\"GMT\"));\n    }\n\n    public String getGMTString() {\n        return dateFormat.format(myDate);\n    }\n}\n", "java.util.Date", "toGMTString"), anyOf(contains("TimeZone"), contains("format")));
  }

  @Test
  void toLocaleString() throws Exception {
    assertThat(synthesiseGPT("Date currentDate = new Date();\nString formattedDate = currentDate.toLocaleString();\n\n", "Date currentDate = new Date();\nDateFormat dateFormat = DateFormat.getDateTimeInstance();\nString formattedDate = dateFormat.format(currentDate);\n", "java.util.Date", "toLocaleString"), anyOf(contains("format")));
  }

  @Test
  void UTC() throws Exception {
    assertThat(synthesiseGPT("Date date = new Date(this.UTC(a, b, c, d, e, f));\n\n", "Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(\"UTC\"));\ncalendar.set(a + 1900, b, c, d, e, f);\nDate date = calendar.getTime();\n", "java.util.Date", "UTC", "int", "int", "int", "int", "int", "int"), anyOf(contains("GregorianCalendar"), contains("TimeZone"), contains("getTime"), contains("set")));
  }
}