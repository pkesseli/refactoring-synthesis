package uk.ac.ox.cs.refactoring.classloader;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

/**
 * Performs no operation and returns the class bytes as passed in.
 */
public class NullClassFileTransformer implements ClassFileTransformer {
  /**
   * Single shareable instance to be used by all clients.
   */
  public static final ClassFileTransformer NULL = new NullClassFileTransformer();

  private NullClassFileTransformer() {
  }

  @Override
  public byte[] transform(final ClassLoader loader, final String className, final Class<?> classBeingRedefined,
      final ProtectionDomain protectionDomain, final byte[] classfileBuffer) {
    return classfileBuffer;
  }
}
