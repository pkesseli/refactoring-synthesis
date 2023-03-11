package uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.context;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;

import java.util.Calendar;
import java.util.Collections;
import java.util.Set;

import org.junit.jupiter.api.Test;

import uk.ac.ox.cs.refactoring.classloader.ClassLoaders;
import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.ComponentDirectory;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaLanguageKey;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaLanguageKeys;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.methods.MethodIdentifier;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.parser.ParserContext;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.parser.ParserFactory;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.javadoc.JavaDocSeed;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.type.TypeFactory;

public class FactorySeedTest {

  @Test
  void seed() throws Exception {
    final ClassLoader classLoader = ClassLoaders.createIsolated();
    final String fullyQualifiedClassName = "java.util.Date";
    final MethodIdentifier methodToRefactor = new MethodIdentifier(fullyQualifiedClassName, "getHours",
        Collections.emptyList());
    final ParserContext parserContext = ParserFactory.create(classLoader);
    final JavaDocSeed javaDoc = new JavaDocSeed(parserContext, classLoader, methodToRefactor);
    final ComponentDirectory components = new ComponentDirectory();
    javaDoc.seed(components);

    final FactorySeed factory = new FactorySeed(classLoader, new ConstantSeed());
    factory.seed(components);

    final Set<JavaLanguageKey> actual = components.keySet(JavaLanguageKey.class);
    assertThat(actual, hasItem(JavaLanguageKeys.nonnull(TypeFactory.createClassType(Calendar.class))));
  }
}
