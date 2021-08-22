package uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;

import java.util.Calendar;
import java.util.Set;

import org.junit.jupiter.api.Test;

import uk.ac.ox.cs.refactoring.classloader.ClassLoaders;
import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.ComponentDirectory;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaLanguageKey;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaLanguageKeys;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.type.TypeFactory;

public class FactorySeedTest {
  @Test
  void seed() throws Exception {
    final ClassLoader classLoader = ClassLoaders.createIsolated();
    final JavaDocSeed javaDoc = new JavaDocSeed(classLoader);
    final ComponentDirectory components = new ComponentDirectory();
    javaDoc.seed(components);

    final FactorySeed factory = new FactorySeed(classLoader);
    factory.seed(components);

    final Set<JavaLanguageKey> actual = components.keySet(JavaLanguageKey.class);
    assertThat(actual, hasItem(JavaLanguageKeys.nonnull(TypeFactory.createClassType(Calendar.class))));
  }
}
