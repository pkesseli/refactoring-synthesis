package uk.ac.ox.cs.refactoring.synthesis.candidate.random;

import com.pholser.junit.quickcheck.random.SourceOfRandomness;

/**
 * Helper to access randomness based on fuzzing input stream, avoiding fast
 * sources of randomness.
 */
public final class RandomnessAccessor {

  /**
   * {@link SourceOfRandomness#nextByte(byte, byte)}
   * 
   * @param sourceOfRandomness {@link SourceOfRandomness#nextByte(byte, byte)}
   * @param min                {@link SourceOfRandomness#nextByte(byte, byte)}
   * @param max                {@link SourceOfRandomness#nextByte(byte, byte)}
   * @return {@link SourceOfRandomness#nextByte(byte, byte)}
   */
  public static byte nextByte(SourceOfRandomness sourceOfRandomness, final int min, final int max) {
    return nextByte(sourceOfRandomness, (byte) min, (byte) max);
  }

  /**
   * {@link SourceOfRandomness#nextByte(byte, byte)}
   * 
   * @param sourceOfRandomness {@link SourceOfRandomness#nextByte(byte, byte)}
   * @param min                {@link SourceOfRandomness#nextByte(byte, byte)}
   * @param max                {@link SourceOfRandomness#nextByte(byte, byte)}
   * @return {@link SourceOfRandomness#nextByte(byte, byte)}
   */
  public static byte nextByte(SourceOfRandomness sourceOfRandomness, final byte min, final byte max) {
    final byte value = (byte) (sourceOfRandomness.nextByte(Byte.MIN_VALUE, Byte.MAX_VALUE) % (max - min + 1));
    return (byte) (min + value);
  }

  /**
   * {@link SourceOfRandomness#nextInt(int)}
   * 
   * @param sourceOfRandomness {@link SourceOfRandomness#nextInt(int)}
   * @param max                {@link SourceOfRandomness#nextInt(int)}
   * @return {@link SourceOfRandomness#nextInt(int)}
   */
  public static int nextInt(final SourceOfRandomness sourceOfRandomness, final int max) {
    return Math.abs(sourceOfRandomness.nextInt()) % (max + 1);
  }

  private RandomnessAccessor() {
  }
}
