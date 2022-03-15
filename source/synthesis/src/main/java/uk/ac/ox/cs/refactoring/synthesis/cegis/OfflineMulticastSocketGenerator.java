package uk.ac.ox.cs.refactoring.synthesis.cegis;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.DatagramSocket;
import java.net.MulticastSocket;
import java.net.SocketAddress;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

/** Provides offline {@link MulticastSocket} instances for testing. */
public class OfflineMulticastSocketGenerator extends Generator<MulticastSocket> {

  /**
   * Used to instantiate {@link OfflineMulticastSocket} in isolated class
   * loaders.
   */
  private static final Constructor<? extends MulticastSocket> MULTICAST_SOCKET_FACTORY;

  static {
    final ClassLoader cl = OfflineMulticastSocketGenerator.class.getClassLoader();
    final Class<?> cls;
    try {
      cls = cl.loadClass(OfflineMulticastSocketGenerator.class.getName() + "$OfflineMulticastSocket");
    } catch (final ClassNotFoundException e) {
      throw new IllegalStateException(e);
    }

    @SuppressWarnings("unchecked")
    final Class<? extends MulticastSocket> socketClass = (Class<? extends MulticastSocket>) cls;
    try {
      MULTICAST_SOCKET_FACTORY = socketClass.getConstructor(byte.class);
    } catch (final NoSuchMethodException e) {
      throw new IllegalStateException(e);
    }
  }

  /** {@link Generator#Generator(java.lang.Class)} */
  public OfflineMulticastSocketGenerator() {
    super(MulticastSocket.class);
  }

  /** Offline implementation of {@link MulticastSocket}. */
  public static class OfflineMulticastSocket extends MulticastSocket {

    /** Offline address to use. */
    private static final SocketAddress NO_DELEGATE;

    static {
      final Field field;
      try {
        field = DatagramSocket.class.getDeclaredField("NO_DELEGATE");
      } catch (final NoSuchFieldException e) {
        throw new IllegalStateException(e);
      }
      field.setAccessible(true);
      try {
        NO_DELEGATE = (SocketAddress) field.get(null);
      } catch (final IllegalAccessException e) {
        throw new IllegalStateException(e);
      }
    }

    /** {@link #getTTL()} */
    byte ttl;

    /**
     * @param ttl {@link #ttl}
     * @throws IOException {@link MulticastSocket#MulticastSocket()}
     */
    public OfflineMulticastSocket(final byte ttl) throws IOException {
      super(NO_DELEGATE);
      this.ttl = ttl;
    }

    @Override
    public int getTimeToLive() throws IOException {
      return ttl;
    }

    @Override
    public byte getTTL() throws IOException {
      return ttl;
    }

    @Override
    public void setTimeToLive(final int ttl) throws IOException {
      this.ttl = (byte) ttl;
    }

    @Override
    public void setTTL(byte ttl) throws IOException {
      this.ttl = ttl;
    }
  }

  @Override
  public MulticastSocket generate(final SourceOfRandomness random, final GenerationStatus status) {
    final byte ttl = random.nextByte((byte) 0, Byte.MAX_VALUE);
    final MulticastSocket socket;
    try {
      socket = MULTICAST_SOCKET_FACTORY.newInstance(ttl);
    } catch (final InstantiationException | IllegalAccessException | IllegalArgumentException
        | InvocationTargetException e) {
      throw new IllegalStateException(e);
    }
    return socket;
  }
}
