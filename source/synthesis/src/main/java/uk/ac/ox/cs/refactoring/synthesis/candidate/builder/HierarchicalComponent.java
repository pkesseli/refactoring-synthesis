package uk.ac.ox.cs.refactoring.synthesis.candidate.builder;

/**
 * 
 */
public interface HierarchicalComponent<T> extends Component<T> {

  /**
   * 
   */
  final int HAS_ARGUMENTS = 1;

  /**
   * 
   */
  final int NO_ARGUMENTS = 0;

  @Override
  default int size() {
    return getArgumentTypes().size() > 0 ? HAS_ARGUMENTS : NO_ARGUMENTS;
  }
}
