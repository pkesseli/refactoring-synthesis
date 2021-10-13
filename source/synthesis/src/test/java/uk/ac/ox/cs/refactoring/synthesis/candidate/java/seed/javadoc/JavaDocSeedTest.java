package uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.javadoc;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.isA;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.github.javaparser.ast.type.PrimitiveType;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import uk.ac.ox.cs.refactoring.classloader.ClassLoaders;
import uk.ac.ox.cs.refactoring.synthesis.candidate.api.ExecutionContext;
import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.Component;
import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.ComponentDirectory;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaLanguageKey;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaLanguageKeys;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.InvokeMethod;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.This;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.methods.MethodIdentifier;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.type.TypeFactory;
import uk.ac.ox.cs.refactoring.synthesis.state.State;

public class JavaDocSeedTest {

  private final ClassLoader classLoader = ClassLoaders.createIsolated();

  @ParameterizedTest
  @CsvSource({ "getHours," + Calendar.HOUR_OF_DAY, "getMinutes," + Calendar.MINUTE, "getSeconds," + Calendar.SECOND })
  void javaUtilDateGet(final String methodName, final int field) throws Exception {
    final MethodIdentifier methodIdentifier = new MethodIdentifier("java.util.Date", methodName,
        Collections.emptyList());
    final JavaDocSeed seed = new JavaDocSeed(classLoader, methodIdentifier);
    final ComponentDirectory components = new ComponentDirectory();
    seed.seed(components);

    final List<Component<JavaLanguageKey, Object>> seeded = components
        .get(JavaLanguageKeys.expression(PrimitiveType.intType()), 1).collect(Collectors.toList());
    MatcherAssert.assertThat(seeded, hasItem(isA(SnippetComponent.class)));
    final Component<JavaLanguageKey, Object> component = seeded.get(0);
    final InvokeMethod invokeMethod = (InvokeMethod) component
        .construct(new Object[] { This.create(TypeFactory.create(Calendar.class)) });

    final Calendar calendar = Calendar.getInstance();
    final int expected = 10;
    calendar.set(field, expected);
    final State state = new State(calendar);
    final ExecutionContext context = new ExecutionContext(classLoader, state);
    final Object actual = invokeMethod.evaluate(context);
    assertEquals(expected, actual);
  }
}
