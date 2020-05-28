package uk.ac.ox.cs.refactoring.benchmarks.resources;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.util.AnnotationUtils;
import org.junit.platform.commons.util.ReflectionUtils;

/**
 * Configurable JUnit 5 parameter injection helper. Implements
 * {@link BeforeAllCallback}, {@link BeforeEachCallback} and
 * {@link ParameterResolver} by injecting values provided by a configurable
 * value factory if a parameter or field is annotated with a configurable
 * annotation type.
 *
 * @param <AnnotationType>
 *          Fields or parameters with an annotation of this type will have a
 *          value injected.
 */
public class InjectingParameterExtension<ValueType, AnnotationType extends Annotation>
    implements BeforeAllCallback, BeforeEachCallback, ParameterResolver {

  /**
   * Type of the annotation for which to inject values.
   */
  private final Class<AnnotationType> annotationType;

  /**
   * For a given {@link ExtensionContext} (i.e. lifetime) and
   * {@link #annotationType AnnotationType} this provided value factory produces
   * values to inject.
   */
  private final BiFunction<ExtensionContext, AnnotationType, ValueType> valueFactory;

  /**
   * Type of the values injected into fields and parameters.
   */
  private final Class<ValueType> valueType;

  /**
   * @param annotationType
   *          {@link #annotationType}
   * @param valueFactory
   *          {@link #valueFactory}
   * @param valueType
   *          {@link #valueType}
   */
  public InjectingParameterExtension(final Class<AnnotationType> annotationType,
      final BiFunction<ExtensionContext, AnnotationType, ValueType> valueFactory,
      final Class<ValueType> valueType) {
    this.annotationType = annotationType;
    this.valueFactory = valueFactory;
    this.valueType = valueType;
  }

  @Override
  public void beforeAll(final ExtensionContext context)
      throws IllegalAccessException {
    inject(context, true);
  }

  @Override
  public void beforeEach(final ExtensionContext context)
      throws IllegalAccessException {
    inject(context, false);
  }

  /**
   * Reflectively checks for each field in the test class if it is annotated
   * with {@link #annotationType} and if so constructs a value using
   * {{@link #valueFactory} and reflectively assignes it to the field.
   *
   * @param extensionContext
   *          Used to look up test class and test instance.
   * @param isStatic
   *          <code>true</code> if only static fields should be considered,
   *          <code>false</code> if only instance fields should be considered.
   * @throws IllegalAccessException
   *           if a reflective access fails.
   */
  private void inject(final ExtensionContext extensionContext,
      final boolean isStatic) throws IllegalAccessException {
    final Predicate<Field> predicate = isStatic ? ReflectionUtils::isStatic
        : ReflectionUtils::isNotStatic;
    final Object testInstance = isStatic ? null
        : extensionContext.getRequiredTestInstance();

    final List<Field> fields = AnnotationUtils.findAnnotatedFields(
        extensionContext.getRequiredTestClass(), annotationType, predicate);
    for(final Field field : fields) {
      if (!field.getType().isAssignableFrom(valueType))
        continue;

      final AnnotationType annotation = field.getAnnotation(annotationType);
      if (annotation == null)
        continue;

      final Object value = valueFactory.apply(extensionContext, annotation);
      field.setAccessible(true);
      field.set(testInstance, value);
    }
  }

  @Override
  public Object resolveParameter(final ParameterContext parameterContext,
      final ExtensionContext extensionContext)
      throws ParameterResolutionException {
    final AnnotationType annotation = parameterContext.getParameter()
        .getAnnotation(annotationType);
    return valueFactory.apply(extensionContext, annotation);
  }

  @Override
  public boolean supportsParameter(final ParameterContext parameterContext,
      final ExtensionContext extensionContext) {
    return parameterContext.getParameter().getType().isAssignableFrom(valueType)
        && parameterContext.isAnnotated(annotationType);
  }
}
