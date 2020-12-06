package uk.ac.ox.cs.refactoring.instrument.method;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.instrument.ClassFileTransformer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import uk.ac.ox.cs.refactoring.instrument.benchmark.replace.Candidate;
import uk.ac.ox.cs.refactoring.instrument.benchmark.replace.Original;
import uk.ac.ox.cs.refactoring.instrument.benchmark.replace.ToRefactor;
import uk.ac.ox.cs.refactoring.classloader.InstrumentingClassLoader;

public class ReplaceMethodInvocationTransformerTest {
  @Test
  void instrument() throws Exception {
    final ClassPool classPool = new ClassPool();
    classPool.appendClassPath(new ClassClassPath(Candidate.class));
    classPool.appendClassPath(new ClassClassPath(Original.class));
    final CtClass candidate = classPool.get(Candidate.class.getName());
    final CtClass original = classPool.get(Original.class.getName());
    final CtMethod candidateMethod = candidate.getDeclaredMethod("foo");
    final CtMethod originalMethod = original.getDeclaredMethod("foo");

    final ClassLoader wrapped = ReplaceMethodInvocationTransformerTest.class.getClassLoader();
    final ClassFileTransformer transformer = new ReplaceMethodInvocationTransformer(originalMethod, candidateMethod);
    final ClassLoader instrument = new InstrumentingClassLoader(transformer, wrapped);
    final Class<?> toRefactor = instrument.loadClass(ToRefactor.class.getName());
    final Method refactored = toRefactor.getDeclaredMethod("foo");
    assertThrows(InvocationTargetException.class, () -> refactored.invoke(null));
  }
}
