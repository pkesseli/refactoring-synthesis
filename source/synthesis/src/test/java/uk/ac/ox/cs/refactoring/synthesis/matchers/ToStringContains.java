package uk.ac.ox.cs.refactoring.synthesis.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

class ToStringContains<T> extends TypeSafeMatcher<T> {

  private final String expected;

  ToStringContains(final String expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(final Description description) {
    description.appendText("object with `toString()` containing ");
    description.appendValue(expected);
  }

  @Override
  protected boolean matchesSafely(final Object item) {
    return item.toString().contains(expected);
  }
}
