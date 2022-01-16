package uk.ac.ox.cs.refactoring.synthesis.matchers;

import java.util.function.BiPredicate;

import org.hamcrest.Matcher;

import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.SnippetCandidate;

public final class CegisMatchers {

  public static <T> Matcher<T> contains(final String expected) {
    return new ToStringContains<>(expected);
  }

  public static Matcher<SnippetCandidate> mapsTo(final Object expected, final Object instance,
      final Object... arguments) {
    return new MapsToValue(expected, instance, arguments);
  }

  public static <T, U> Matcher<SnippetCandidate> mapsTo(final BiPredicate<T, U> predicate, final Object instance,
      final Object... arguments) {
    return new MapsToPredicate<>(predicate, instance, arguments);
  }
}
