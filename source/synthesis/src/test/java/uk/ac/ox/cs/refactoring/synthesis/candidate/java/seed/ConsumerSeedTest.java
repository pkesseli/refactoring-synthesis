package uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;

import java.util.Collections;
import java.util.Set;

import org.junit.jupiter.api.Test;

import uk.ac.ox.cs.refactoring.classloader.ClassLoaders;
import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.ComponentDirectory;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaLanguageKey;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaLanguageKeys;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.methods.MethodIdentifier;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.type.TypeFactory;

public class ConsumerSeedTest {
  @Test
  void seed() throws Exception {
    final ClassLoader classLoader = ClassLoaders.createIsolated();
    final String fullyQualifiedClassName = "java.util.Date";
    final MethodIdentifier methodToRefactor = new MethodIdentifier(fullyQualifiedClassName, "getHours",
        Collections.emptyList());
    final ComponentDirectory components = new ComponentDirectory();
    final JavaDocSeed javaDoc = new JavaDocSeed(classLoader);
    javaDoc.seed(components);
    final SignatureSeed signature = new SignatureSeed(classLoader, methodToRefactor);
    signature.seed(components);
    final FactorySeed factory = new FactorySeed(classLoader);
    factory.seed(components);

    final ConsumerSeed consumer = new ConsumerSeed(classLoader);
    consumer.seed(components);

    final Set<JavaLanguageKey> actual = components.parameterKeySet(JavaLanguageKey.class);
    final Class<?> cls = classLoader.loadClass(fullyQualifiedClassName);
    assertThat(actual, hasItem(JavaLanguageKeys.expression(TypeFactory.createClassType(cls))));
  }
}
