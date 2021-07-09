package uk.ac.ox.cs.refactoring.synthesis.invocation;

import java.lang.reflect.Executable;
import java.util.Arrays;
import java.util.List;

/**
 * Implements common operations of {@link Invokable} for all
 * {@link Executable}s.
 */
public abstract class ExecutableInvokable implements Invokable {

  /**
   * Executable meta information.
   */
  private final Executable executable;

  /**
   * @param executable {@link #executable}
   */
  public ExecutableInvokable(final Executable executable) {
    this.executable = executable;
  }

  @Override
  public List<Class<?>> getParameterTypes() {
    return Arrays.asList(executable.getParameterTypes());
  }
}
