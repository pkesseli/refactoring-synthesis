
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
assertThat (synthesiseGPT ("this.getDate();" , "\nimport java.time.LocalDate;\nimport java.time.ZoneId;\n\n// ...\n\nDate date = new Date(); // this is your existing Date object\nLocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();\nint dayOfMonth = localDate.getDayOfMonth();\n```\n\nIn the context of the original code fragment, it would look like this:\n\n```java\nimport java.time.LocalDate;\nimport java.time.ZoneId;\n\n// ...\n\nLocalDate localDate = this.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();\nint dayOfMonth = localDate.getDayOfMonth();\n;" , "java.util.Date" , "getDate") , anyOf (contains ("get"))) ;
  }

  @Test
  void getDay() throws Exception {
assertThat (synthesiseGPT ("this.getDay();" , "\nimport java.util.Date;\nimport java.time.LocalDate;\nimport java.time.ZoneId;\n\n// ...\n\nDate date = new Date(); // this represents the 'this' in your original code\nLocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();\nint dayOfWeek = localDate.getDayOfWeek().getValue(); // This will give you the day of the week (1 = Monday, ..., 7 = Sunday)\n```\n\nIf you specifically need the day of the month, you can use:\n\n```java\nint dayOfMonth = localDate.getDayOfMonth();\n```\n\nIf you need the day of the week as it was represented by the deprecated `getDay()` method (0 = Sunday, ..., 6 = Saturday), you can adjust the `dayOfWeek` value accordingly:\n\n```java\nint dayOfWeekAdjusted = (dayOfWeek == 7) ? 0 : dayOfWeek;\n;" , "java.util.Date" , "getDay") , anyOf (contains ("get"))) ;
  }

  @Test
  void getHours() throws Exception {
assertThat (synthesiseGPT ("this.getHours();" , "\nCalendar calendar = Calendar.getInstance();\ncalendar.setTime(this);\nint hours = calendar.get(Calendar.HOUR_OF_DAY);\n```\n\nOr using `java.time` with `LocalDateTime` (if `this` is a `java.util.Date` object):\n\n```java\nLocalDateTime localDateTime = this.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();\nint hours = localDateTime.getHour();\n;" , "java.util.Date" , "getHours") , anyOf (contains ("get"))) ;
  }

  @Test
  void getMinutes() throws Exception {
assertThat (synthesiseGPT ("this.getMinutes();" , "\nCalendar calendar = Calendar.getInstance();\ncalendar.setTime(this);\nint minutes = calendar.get(Calendar.MINUTE);\n```\n\nAnd here's how you can do it using `java.time` classes, which is the preferred way since Java 8:\n\n```java\nLocalDateTime localDateTime = LocalDateTime.ofInstant(this.toInstant(), ZoneId.systemDefault());\nint minutes = localDateTime.getMinute();\n;" , "java.util.Date" , "getMinutes") , anyOf (contains ("get"))) ;
  }

  @Test
  void getMonth() throws Exception {
assertThat (synthesiseGPT ("this.getMonth();" , "\nimport java.time.LocalDate;\nimport java.time.ZoneId;\n\nLocalDate localDate = this.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();\nint month = localDate.getMonthValue() - 1; // 0-based month index as with Date.getMonth()\n;" , "java.util.Date" , "getMonth") , anyOf (contains ("get"))) ;
  }

  @Test
  void getSeconds() throws Exception {
assertThat (synthesiseGPT ("this.getSeconds();" , "\nthis.toInstant().getEpochSecond() % 60\n;" , "java.util.Date" , "getSeconds") , anyOf (contains ("get"))) ;
  }

  @Test
  void getTimezoneOffset() throws Exception {
assertThat (synthesiseGPT ("this.getTimezoneOffset();" , "\nimport java.time.ZoneId;\nimport java.time.ZonedDateTime;\nimport java.time.temporal.ChronoField;\n\n// ...\n\nZonedDateTime zonedDateTime = ZonedDateTime.now(); // or ZonedDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()) if you have a Date object\nint offsetInSeconds = zonedDateTime.getOffset().getTotalSeconds();\nint offsetInMinutes = offsetInSeconds / 60;\n;" , "java.util.Date" , "getTimezoneOffset") , anyOf (contains ("DST_OFFSET)) / "))) ;
  }

  @Test
  void getYear() throws Exception {
assertThat (synthesiseGPT ("this.getYear();" , "\nimport java.util.Calendar;\n\nCalendar cal = Calendar.getInstance();\ncal.setTime(this);\nint year = cal.get(Calendar.YEAR) - 1900;\n;" , "java.util.Date" , "getYear") , anyOf (contains ("get"))) ;
  }

  @Test
  void parse() throws Exception {
assertThat (synthesiseGPT ("this.parse(param0);" , "\nimport java.time.LocalDate;\nimport java.time.format.DateTimeFormatter;\n\n// ...\n\nDateTimeFormatter formatter = DateTimeFormatter.ofPattern(\"your pattern here\");\nLocalDate date = LocalDate.parse(param0, formatter);\n;" , "java.util.Date" , "parse" , "java.lang.String") , anyOf (contains ("parse"))) ;
  }

  @Test
  void setDate() throws Exception {
assertThat (synthesiseGPT ("this.setDate(param0);" , "\nCalendar calendar = Calendar.getInstance();\ncalendar.setTime(this);\ncalendar.set(Calendar.DATE, param0);\nthis.setTime(calendar.getTimeInMillis());\n;" , "java.util.Date" , "setDate" , "int") , anyOf (contains ("set"))) ;
  }

  @Test
  void setHours() throws Exception {
assertThat (synthesiseGPT ("this.setHours(param0);" , "\nimport java.util.Calendar;\n\nCalendar calendar = Calendar.getInstance();\ncalendar.setTime(this);\ncalendar.set(Calendar.HOUR_OF_DAY, param0);\nthis.setTime(calendar.getTimeInMillis());\n;" , "java.util.Date" , "setHours" , "int") , anyOf (contains ("set"))) ;
  }

  @Test
  void setMinutes() throws Exception {
assertThat (synthesiseGPT ("this.setMinutes(param0);" , "\nimport java.util.Calendar;\n\n// ...\n\nCalendar calendar = Calendar.getInstance();\ncalendar.setTime(this);\ncalendar.set(Calendar.MINUTE, param0);\nthis.setTime(calendar.getTimeInMillis());\n;" , "java.util.Date" , "setMinutes" , "int") , anyOf (contains ("set"))) ;
  }

  @Test
  void setMonth() throws Exception {
assertThat (synthesiseGPT ("this.setMonth(param0);" , "\nimport java.util.Calendar;\n\n// ...\n\nCalendar calendar = Calendar.getInstance();\ncalendar.setTime(this); // Assuming 'this' is a java.util.Date object\ncalendar.set(Calendar.MONTH, param0 - 1); // Calendar months are 0-based\nthis.setTime(calendar.getTimeInMillis());\n;" , "java.util.Date" , "setMonth" , "int") , anyOf (contains ("set"))) ;
  }

  @Test
  void setSeconds() throws Exception {
assertThat (synthesiseGPT ("this.setSeconds(param0);" , "\n// Assuming 'this' is a java.util.Date object, we need to use Calendar to set the seconds\nCalendar calendar = Calendar.getInstance();\ncalendar.setTime(this);\ncalendar.set(Calendar.SECOND, param0);\nthis.setTime(calendar.getTimeInMillis());\n;" , "java.util.Date" , "setSeconds" , "int") , anyOf (contains ("set"))) ;
  }

  @Test
  void setYear() throws Exception {
assertThat (synthesiseGPT ("this.setYear(param0);" , "\nimport java.time.ZoneId;\nimport java.time.LocalDate;\nimport java.time.ZonedDateTime;\nimport java.util.Date;\n\n// Assuming 'this' is a Date object and param0 is the year you want to set\nDate date = this; // 'this' refers to the current Date object\nint yearToSet = param0; // 'param0' is the year you want to set\n\n// Convert the Date to a LocalDate\nLocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();\n\n// Set the year\nlocalDate = localDate.withYear(yearToSet);\n\n// Convert back to Date if necessary\nZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());\nDate updatedDate = Date.from(zonedDateTime.toInstant());\n\n// If 'this' needs to be updated, you might need to set it directly if it's a mutable object\n// However, Date objects are not mutable in terms of their fields, so you would return or use the updatedDate\n;" , "java.util.Date" , "setYear" , "int") , anyOf (contains ("set"))) ;
  }

  @Test
  void toGMTString() throws Exception {
assertThat (synthesiseGPT ("this.toGMTString();" , "\nSimpleDateFormat sdf = new SimpleDateFormat(\"EEE, dd MMM yyyy HH:mm:ss 'GMT'\", Locale.US);\nsdf.setTimeZone(TimeZone.getTimeZone(\"GMT\"));\nString gmtString = sdf.format(this);\n```\n\nOr using `java.time` package (preferred for new code):\n\n```java\nimport java.time.ZoneId;\nimport java.time.ZonedDateTime;\nimport java.time.format.DateTimeFormatter;\n\nZonedDateTime zdt = ZonedDateTime.ofInstant(this.toInstant(), ZoneId.of(\"GMT\"));\nDateTimeFormatter formatter = DateTimeFormatter.ofPattern(\"EEE, dd MMM yyyy HH:mm:ss 'z'\").withLocale(Locale.US);\nString gmtString = zdt.format(formatter);\n;" , "java.util.Date" , "toGMTString") , anyOf (contains ("TimeZone") , contains ("format"))) ;
  }

  @Test
  void toLocaleString() throws Exception {
assertThat (synthesiseGPT ("this.toLocaleString();" , "\nimport java.text.DateFormat;\nimport java.util.Date;\nimport java.util.Locale;\n\n// ...\n\nDate date = new Date(); // this is your existing Date object\nDateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.getDefault());\nString formattedDate = dateFormat.format(date);\n```\n\nIf you want to use the `java.time` API, which is the recommended way since Java 8:\n\n```java\nimport java.time.ZoneId;\nimport java.time.ZonedDateTime;\nimport java.time.format.DateTimeFormatter;\nimport java.time.format.FormatStyle;\nimport java.util.Locale;\n\n// ...\n\nDate date = new Date(); // this is your existing Date object\nZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());\nDateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(Locale.getDefault());\nString formattedDate = zonedDateTime.format(formatter);\n;" , "java.util.Date" , "toLocaleString") , anyOf (contains ("format"))) ;
  }

  @Test
  void UTC() throws Exception {
assertThat (synthesiseGPT ("this.UTC(param0, param1, param2, param3, param4, param5);" , "\nCalendar calendar = Calendar.getInstance(TimeZone.getTimeZone(\"UTC\"));\ncalendar.set(Calendar.YEAR, param0 + 1900); // Calendar.YEAR expects the year as it is, Date.UTC expects years since 1900\ncalendar.set(Calendar.MONTH, param1); // Calendar.MONTH is 0-based, just like Date.UTC\ncalendar.set(Calendar.DATE, param2);\ncalendar.set(Calendar.HOUR_OF_DAY, param3);\ncalendar.set(Calendar.MINUTE, param4);\ncalendar.set(Calendar.SECOND, param5);\ncalendar.set(Calendar.MILLISECOND, 0);\n\nDate date = calendar.getTime();\n```\n\nAnd here's how you can refactor it using `java.time` (preferred for new code):\n\n```java\nZonedDateTime zdt = ZonedDateTime.of(param0, param1 + 1, param2, param3, param4, param5, 0, ZoneOffset.UTC);\nDate date = Date.from(zdt.toInstant());\n;" , "java.util.Date" , "UTC" , "int" , "int" , "int" , "int" , "int" , "int") , anyOf (contains ("GregorianCalendar") , contains ("TimeZone") , contains ("getTime") , contains ("set"))) ;
  }
}
