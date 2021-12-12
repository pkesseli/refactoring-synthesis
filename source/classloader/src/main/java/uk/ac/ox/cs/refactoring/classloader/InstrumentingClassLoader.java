package uk.ac.ox.cs.refactoring.classloader;

import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.net.URL;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.security.PermissionCollection;
import java.security.ProtectionDomain;
import java.util.Arrays;

import org.apache.commons.io.IOUtils;

/**
 * Decorates a wrapped {@link ClassLoader}, applying a
 * {@link ClassFileTransformer} to every class loaded. No classes will be loaded
 * by the wrapped class loader directly, which means different instances of this
 * class are isolated from each other with respect to the classes provided by
 * their respective wrapped {@link ClassLoader}s.
 */
public class InstrumentingClassLoader extends IsolatedClassLoader {

  /**
   * Do not instrument any classes with these prefixes, and instead load them in
   * {@link #wrapped} directly. This breaks isolation e.g. for Mockito, but is a
   * necessary compromise since loading Mockito in isolated class loaders simply
   * does not scale - its class initialisers are too computationally expensive.
   */
  private static final String[] SKIPPED_PREFIXES = { "org.mockito." };

  /**
   * {@link ClassFileTransformer} to apply to all loaded classes.
   */
  private final ClassFileTransformer transformer;

  /**
   * Wrapped {@link ClassLoader} whose classes should be instrumented.
   */
  private final ClassLoader wrapped;

  /**
   * @param transformer {@link #transformer}
   * @param wrapped     {@link #wrapped}
   */
  public InstrumentingClassLoader(final ClassFileTransformer transformer, final ClassLoader wrapped) {
    super(wrapped.getParent());
    this.transformer = transformer;
    this.wrapped = wrapped;
  }

  /**
   * Transforms a fully qualified class name to a file name in the filesystem.
   * 
   * @param name Fully qualified class name.
   * @return Expected class file name from which the respective class can be read.
   */
  private static String getClassFileName(final String name) {
    return name.replace(JavaLanguage.PACKAGE_SEPARATOR, JavaLanguage.RESOURCE_SEPARATOR).concat(".class");
  }

  @Override
  public Class<?> loadClass(final String name) throws ClassNotFoundException {
    if (Arrays.stream(SKIPPED_PREFIXES).anyMatch(name::startsWith))
      return wrapped.loadClass(name);

    return super.loadClass(name);
  }

  @Override
  protected Class<?> findClass(final String name) throws ClassNotFoundException {
    final String classFileName = getClassFileName(name);
    final byte[] content;
    try (final InputStream is = wrapped.getResourceAsStream(classFileName)) {
      content = IOUtils.toByteArray(is);
    } catch (final IOException e) {
      throw new ClassNotFoundException(name, e);
    }
    if (content.length == 0) {
      throw new ClassNotFoundException(name);
    }

    final URL url = wrapped.getResource(classFileName);
    final CodeSource codeSource = new CodeSource(url, (CodeSigner[]) null);
    final PermissionCollection permissions = getPermissions(codeSource);
    final ProtectionDomain protectionDomain = new ProtectionDomain(codeSource, permissions, this, null);
    final byte[] transformed;
    try {
      transformed = transformer.transform(this, name, null, protectionDomain, content);
    } catch (final IllegalClassFormatException e) {
      throw new ClassNotFoundException(name, e);
    }

    final Class<?> cls = defineClass(name, transformed, 0, transformed.length, codeSource);
    loadedClasses.add(name);
    return cls;
  }
}
