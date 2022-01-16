package uk.ac.ox.cs.refactoring.synthesis.matchers;

import java.util.function.BiPredicate;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.SnippetCandidate;

class MapsToPredicate<T, U> extends TypeSafeMatcher<SnippetCandidate> {

  private final Executor executor;

  private final BiPredicate<T, U> predicate;

  MapsToPredicate(final BiPredicate<T, U> predicate, final Object instance, final Object... arguments) {
    executor = new Executor(instance, arguments);
    this.predicate = predicate;
  }

  @Override
  public void describeTo(final Description description) {
    description.appendText("candidate mapping ");
    description.appendValue(executor.Input);
    description.appendText(" to match predicate ");
    description.appendValue(predicate);
  }

  @Override
  protected boolean matchesSafely(final SnippetCandidate candidate) {
    final Object actual = executor.run(candidate);
    @SuppressWarnings("unchecked")
    final T instance = (T) executor.Input.Instance;
    @SuppressWarnings("unchecked")
    final U converted = (U) actual;
    return predicate.test(instance, converted);
  }
}
