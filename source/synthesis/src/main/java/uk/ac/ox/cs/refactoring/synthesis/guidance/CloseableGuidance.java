package uk.ac.ox.cs.refactoring.synthesis.guidance;

import java.io.IOException;

import edu.berkeley.cs.jqf.fuzz.guidance.Guidance;

/**
 * Extends {@link Guidance} and {@link AutoCloseable}, for implementations which
 * need to dispose resources.
 */
public interface CloseableGuidance extends Guidance, AutoCloseable {

  @Override
  void close() throws IOException;
}
