package uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.context;

import java.util.List;

import com.github.javaparser.ast.type.Type;

import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.ComponentDirectory;
import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.NullaryComponent;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaComponents;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.Parameter;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.This;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.methods.MethodIdentifier;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.methods.Methods;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.type.TypeFactory;
import uk.ac.ox.cs.refactoring.synthesis.invocation.Invokable;

/**
 * Adds instructions refering to parameters and instances available to the
 * original method, i.e. the {@link This} and {@link Parameter} nullary
 * components.
 */
public class SignatureSeed implements InstructionSetSeed {

  /**
   * Used to analyse class types reflectively.
   */
  private final ClassLoader classLoader;

  /**
   * Method to be replaced.
   */
  private final MethodIdentifier methodToRefactor;

  /**
   * @param classLoader      {@link #classLoader}
   * @param methodToRefactor {@link #methodToRefactor}
   */
  public SignatureSeed(final ClassLoader classLoader, final MethodIdentifier methodToRefactor) {
    this.classLoader = classLoader;
    this.methodToRefactor = methodToRefactor;
  }

  @Override
  public void seed(final ComponentDirectory components) throws ClassNotFoundException, NoSuchMethodException {
    final Invokable invokable = Methods.create(classLoader, methodToRefactor);
    final JavaComponents javaComponents = new JavaComponents(components);
    if (Invokable.hasInstance(invokable)) {
      Type instanceType = TypeFactory.createClassType(invokable.getInstanceType());
      javaComponents.nonnull(instanceType, new NullaryComponent<>(This.create(instanceType)));
    }
    final List<Class<?>> parameterTypes = invokable.getParameterTypes();
    for (int i = 0; i < parameterTypes.size(); ++i) {
      final Type type = TypeFactory.create(parameterTypes.get(i));
      final Parameter parameter = new Parameter(i, type);
      javaComponents.nonnull(type, new NullaryComponent<>(parameter));
    }
  }

}
