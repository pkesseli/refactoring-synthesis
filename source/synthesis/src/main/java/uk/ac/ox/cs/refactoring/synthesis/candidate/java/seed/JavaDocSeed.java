package uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.Type;

import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.ComponentDirectory;
import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.FunctionComponent;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.IExpression;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.BoundInvokableFactory;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.InvokeMethodFactory;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaComponents;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaLanguageKey;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaLanguageKeys;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.InvokeMethod;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.Literal;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.type.TypeFactory;

/**
 * Adds instructions based on hints in JavaDoc.
 * 
 * TODO: Implement!
 */
public class JavaDocSeed implements InstructionSetSeed {

  /**
   * Used to load pre-configured classes.
   */
  private final ClassLoader classLoader;

  /**
   * @param classLoader {@link #classLoader}
   */
  public JavaDocSeed(final ClassLoader classLoader) {
    this.classLoader = classLoader;
  }

  @Override
  public void seed(final ComponentDirectory components)
      throws ClassNotFoundException, IllegalAccessException, NoSuchFieldException, NoSuchMethodException {
    final Class<?> calendar = classLoader.loadClass("java.util.Calendar");
    final Method get = calendar.getDeclaredMethod("get", int.class);
    final InvokeMethodFactory invokeFactory = new InvokeMethodFactory(get);
    final Map<Integer, IExpression> boundArguments = new HashMap<>();
    final Field hours = calendar.getDeclaredField("HOUR_OF_DAY");
    final Object value = hours.get(null);
    final Type type = PrimitiveType.intType();
    boundArguments.put(1, new Literal(value, type));
    final BoundInvokableFactory<InvokeMethod> bound = new BoundInvokableFactory<>(invokeFactory, boundArguments);

    final JavaComponents javaComponents = new JavaComponents(components);
    final Type resultType = TypeFactory.create(get.getReturnType());
    final List<JavaLanguageKey> parameterKeys = Arrays
        .asList(JavaLanguageKeys.nonnull(TypeFactory.createClassType(calendar)));
    javaComponents.nonnull(resultType, new FunctionComponent<>(parameterKeys, bound));
  }

}
