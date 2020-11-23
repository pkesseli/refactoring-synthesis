package uk.ac.ox.cs.refactoring.instrument.method;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import javassist.ByteArrayClassPath;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CodeConverter;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

/**
 * {@link ClassFileTransformer} replacing the invocation of an original method
 * (instance or static) by an invocation of a static candidate method.
 */
public class ReplaceMethodInvocationTransformer implements ClassFileTransformer {
  /**
   * {@link CodeConverter} replacing the method invocation.
   */
  private final CodeConverter codeConverter = new CodeConverter();

  /**
   * @param origMethod      Original method to be replaced.
   * @param candidateMethod Candidate method to be tested.
   */
  public ReplaceMethodInvocationTransformer(final CtMethod origMethod, final CtMethod candidateMethod) {
    codeConverter.redirectMethodCallToStatic(origMethod, candidateMethod);
  }

  @Override
  public byte[] transform(final ClassLoader loader, final String className, final Class<?> classBeingRedefined,
      final ProtectionDomain protectionDomain, final byte[] classfileBuffer) throws IllegalClassFormatException {
    final ClassPool classPool = new ClassPool();
    classPool.appendClassPath(new ByteArrayClassPath(className, classfileBuffer));
    try {
      final CtClass cls = classPool.get(className);
      cls.instrument(codeConverter);
      return cls.toBytecode();
    } catch (final CannotCompileException | IOException | NotFoundException cause) {
      final IllegalClassFormatException e = new IllegalClassFormatException();
      e.initCause(cause);
      throw e;
    }
  }
}
