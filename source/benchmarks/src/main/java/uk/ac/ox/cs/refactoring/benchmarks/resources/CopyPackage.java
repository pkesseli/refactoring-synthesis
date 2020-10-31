package uk.ac.ox.cs.refactoring.benchmarks.resources;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.Resource;
import io.github.classgraph.ResourceList;
import io.github.classgraph.ScanResult;

/**
 * Needs to be added using {@link org.junit.jupiter.api.extension.ExtendWith} to
 * each class which should use the {@link Benchmark} extension:
 *
 * <pre>
 * &#64;ExtendWith(CopyPackage.class)
 * public class SomeTest {
 *
 *   &#64;Benchmark("uk.ac.ox.cs.package")
 *   private static Path sourceRoot1;
 *
 *   &#64;Benchmark("uk.ac.ox.cs.package")
 *   private Path sourceRoot2;
 *
 *   &#64;Test
 *   public void testParameter(
 *       &#64;Benchmark("uk.ac.ox.cs.package") Path sourceRoot) {
 *     // ...
 *   }
 * }
 * </pre>
 *
 * Interprets the {@link Benchmark} runtime annotations and injects the
 * requested packages as a {@link Path}.
 */
public class CopyPackage
    implements BeforeAllCallback, BeforeEachCallback, ParameterResolver {

  /**
   * Associates created temporary directories to be deleted after test methods
   * or classes finish with an appropriate
   * {@link org.junit.jupiter.api.extension.ExtensionContext.Store.CloseableResource}
   * in the {@link ExtensionContext}.
   */
  private final PathDeletionStore pathDeletionStore = new PathDeletionStore();

  /**
   * Prefix used for all temporary directories created.
   */
  private static final String RESOURCE_DIR_PREFIX = "resource";

  private final InjectingParameterExtension<Path, Benchmark> packageExtension = new InjectingParameterExtension<>(
      Benchmark.class, this::copyPackage, Path.class);

  /**
   * Recursively copies a directory in the classpath resources to an existing
   * temporary directory.
   *
   * @param directory
   *          Directory into which to copy the children of the given classpath
   *          resource.
   * @param resourceName
   *          Classpath path of the resource whose children should be copied.
   * @param resources
   *          Resolved children of <code>resourceName</code> to be copied.
   * @return <code>directory</code>
   */
  private static Path copyDirectory(final Path directory,
      final String resourceName, final ResourceList resources) {
    for(final Resource res : resources) {
      final String resource = res.getPathRelativeToClasspathElement();
      final Path resourcePath = Paths.get(resource);
      final Path target = directory.resolve(resourcePath);

      try {
        Files.createDirectories(target.getParent());
      } catch(final IOException e) {
        throw new IllegalStateException(e);
      }

      try(final InputStream is = res.open();
          final OutputStream os = Files.newOutputStream(target)) {
        IOUtils.copy(is, os);
      } catch(final IOException e) {
        throw new IllegalStateException(e);
      }
    }
    return directory;
  }

  /**
   * Copies the resource requested in the given annotation, using a default
   * value of {@link #defaultResourceName(ExtensionContext)}. Allows for single
   * file or directory classpath resources.
   *
   * @param extensionContext
   *          {@link ExtensionContext} to which to link the created temporary
   *          files' lifetime.
   * @param annotation
   *          {@link Resource} annotation indicating which resource(s) to copy.
   * @return {@link Path} to created directory in case of directory resources,
   *         or to the single copied file in case of single file resources.
   */
  private Path copyPackage(final ExtensionContext extensionContext,
      final Benchmark annotation) {
    final Path directory;
    try {
      directory = Files.createTempDirectory(RESOURCE_DIR_PREFIX);
    } catch(final IOException e) {
      throw new IllegalStateException(e);
    }
    pathDeletionStore.registerForDeletion(extensionContext, directory);

    final String resourceName = annotation.value().replace('.', '/');
    final ClassGraph classGraph = new ClassGraph();
    classGraph.acceptPaths(resourceName);
    try(final ScanResult scanResult = classGraph.scan();
        final ResourceList resources = scanResult.getAllResources()) {
      return copyDirectory(directory, resourceName, resources);
    }
  }

  @Override
  public boolean supportsParameter(final ParameterContext parameterContext,
      final ExtensionContext extensionContext)
      throws ParameterResolutionException {
    return packageExtension.supportsParameter(parameterContext,
        extensionContext);
  }

  @Override
  public Object resolveParameter(final ParameterContext parameterContext,
      final ExtensionContext extensionContext)
      throws ParameterResolutionException {
    return packageExtension.resolveParameter(parameterContext,
        extensionContext);
  }

  @Override
  public void beforeEach(final ExtensionContext context) throws Exception {
    packageExtension.beforeEach(context);
  }

  @Override
  public void beforeAll(final ExtensionContext context) throws Exception {
    packageExtension.beforeAll(context);
  }
}
