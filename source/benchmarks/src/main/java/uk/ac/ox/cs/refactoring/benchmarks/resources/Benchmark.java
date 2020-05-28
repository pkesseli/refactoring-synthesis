package uk.ac.ox.cs.refactoring.benchmarks.resources;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Fields or parameters of type {@link java.nio.file.Path Path} annotated with
 * this annotation get initialised automatically, pointing to a temporary
 * directory or file into which the sources contained in the configured package
 * are copied into. The directory is automatically deleted once the test or test
 * class is finished, depending on whether it is injected as a parameter or
 * instance field vs. as a static field. The package is copied recursively.
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
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
public @interface Benchmark {
  /**
   * Package name to copy.
   *
   * @return Name of the package to be provided.
   */
  String value() default "";
}
