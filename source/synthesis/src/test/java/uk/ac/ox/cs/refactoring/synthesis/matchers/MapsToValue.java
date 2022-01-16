package uk.ac.ox.cs.refactoring.synthesis.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.SnippetCandidate;

class MapsToValue extends TypeSafeMatcher<SnippetCandidate> {

  private final Executor executor;

  private final Object expected;

  MapsToValue(final Object expected, final Object instance, final Object... arguments) {
    executor = new Executor(instance, arguments);
    this.expected = expected;
  }

  @Override
  public void describeTo(final Description description) {
    description.appendText("candidate mapping ");
    description.appendValue(executor.Input);
    description.appendText(" to ");
    description.appendValue(expected);
  }

  @Override
  protected boolean matchesSafely(final SnippetCandidate candidate) {
    final Object actual = executor.run(candidate);
    return Objects.equals(expected, actual);
  }
}
