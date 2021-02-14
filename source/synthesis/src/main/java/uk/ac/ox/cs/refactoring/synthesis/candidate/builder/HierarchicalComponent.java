package uk.ac.ox.cs.refactoring.synthesis.candidate.builder;

/**
 * Implements a size measure for components where every additional level of
 * hierarchy increases the size by one.
 */
public interface HierarchicalComponent<K, V> extends Component<K, V> {

  /**
   * Non-nullary component increase the size by 1.
   */
  final int HAS_ARGUMENTS = 1;

  /**
   * Nullary components do not increase the size.
   */
  final int NO_ARGUMENTS = 0;

  @Override
  default int size() {
    return getParameterKeys().size() > 0 ? HAS_ARGUMENTS : NO_ARGUMENTS;
  }
}
