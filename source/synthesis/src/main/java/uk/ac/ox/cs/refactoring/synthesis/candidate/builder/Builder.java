package uk.ac.ox.cs.refactoring.synthesis.candidate.builder;

import java.util.List;

import com.pholser.junit.quickcheck.random.SourceOfRandomness;

/**
 * Implementations allow to construct generic components. Construction is guided
 * by implementation-specific, pre-existing settings as well as a random
 * component. An example of an implementation are candidate porgram builders,
 * which are configured using an instruction set and use the source of
 * randomness to select a well-formed candidate from the instruction set.
 * 
 * @param <K> Identifies a category of components. This is used to identify
 *            sub-components of corerct types. In the case of a program builder,
 *            this key would e.g. identify an expression component of type
 *            {@code double}.
 * @param <V> Built output value, either a complete program snippet, statement
 *            or expression in the case of Java program builders.
 */
public interface Builder<K, V> {

  /**
   * Builds a component using the configured settings.
   * 
   * @param sourceOfRandomness {@link SourceOfRandomness} guiding random selection
   *                           of subcomponents.
   * @param resultKey          Allowed output types.
   * @param extraComponents    {@link ComponentDirectory} extra components to
   *                           consider just for this construction. In the case of
   *                           program builders, this would e.g. include temporary
   *                           variables which are only available in one snippet's
   *                           scope.
   * @return Component built according to the configuration.
   */
  V build(SourceOfRandomness sourceOfRandomness, List<K> resultKeys, ComponentDirectory extraComponents);

}
