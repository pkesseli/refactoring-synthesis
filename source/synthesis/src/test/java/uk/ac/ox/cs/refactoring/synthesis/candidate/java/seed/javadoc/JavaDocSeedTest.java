package uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.javadoc;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.isA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Rectangle;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.VoidType;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import uk.ac.ox.cs.refactoring.classloader.ClassLoaders;
import uk.ac.ox.cs.refactoring.synthesis.candidate.api.ExecutionContext;
import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.Component;
import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.ComponentDirectory;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaLanguageKey;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaLanguageKeys;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.InvokeMethod;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.Parameter;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.This;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.methods.MethodIdentifier;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.type.TypeFactory;
import uk.ac.ox.cs.refactoring.synthesis.state.State;

public class JavaDocSeedTest {

  private final ClassLoader classLoader = ClassLoaders.createIsolated();

  private final ComponentDirectory components = new ComponentDirectory();

  @ParameterizedTest
  @CsvSource({ "getHours," + Calendar.HOUR_OF_DAY, "getMinutes," + Calendar.MINUTE, "getSeconds," + Calendar.SECOND })
  void javaUtilDateGet(final String methodName, final int field) throws Exception {
    final MethodIdentifier methodIdentifier = new MethodIdentifier("java.util.Date", methodName,
        Collections.emptyList());
    javaDocSeed(methodIdentifier);

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

  @ParameterizedTest
  @CsvSource({ "setHours," + Calendar.HOUR_OF_DAY, "setMinutes," + Calendar.MINUTE, "setSeconds," + Calendar.SECOND })
  void javaUtilDateSet(final String methodName, final int field) throws Exception {
    final MethodIdentifier methodIdentifier = new MethodIdentifier("java.util.Date", methodName, Arrays.asList("int"));
    javaDocSeed(methodIdentifier);

    final List<Component<JavaLanguageKey, Object>> seeded = components
        .get(JavaLanguageKeys.expression(new VoidType()), 1).collect(Collectors.toList());
    MatcherAssert.assertThat(seeded, hasItem(isA(SnippetComponent.class)));
    final Component<JavaLanguageKey, Object> component = seeded.get(0);
    final InvokeMethod invokeMethod = (InvokeMethod) component.construct(
        new Object[] { new Parameter(0, PrimitiveType.intType()), This.create(TypeFactory.create(Calendar.class)) });

    final Calendar calendar = Calendar.getInstance();
    calendar.set(field, 0);
    final int expected = 10;
    final State state = new State(calendar, expected);
    final ExecutionContext context = new ExecutionContext(classLoader, state);
    final Object actual = invokeMethod.evaluate(context);
    assertNull(actual);
    assertEquals(expected, calendar.get(field));
  }

  @Test
  void javaAwtRectangle() throws Exception {
    final MethodIdentifier methodIdentifier = new MethodIdentifier("java.awt.Rectangle", "inside",
        Arrays.asList("int", "int"));
    javaDocSeed(methodIdentifier);
    final List<Component<JavaLanguageKey, Object>> seeded = components
        .get(JavaLanguageKeys.expression(PrimitiveType.booleanType()), 1).collect(Collectors.toList());
    MatcherAssert.assertThat(seeded, hasItem(isA(SnippetComponent.class)));
    final Component<JavaLanguageKey, Object> component = seeded.get(0);
    final InvokeMethod invokeMethod = (InvokeMethod) component
        .construct(new Object[] { new Parameter(0, PrimitiveType.intType()), new Parameter(1, PrimitiveType.intType()),
            This.create(TypeFactory.create(Rectangle.class)) });

    final Rectangle rectangle = new Rectangle(0, 0, 10, 10);
    final State state = new State(rectangle, 5, 5);
    final ExecutionContext context = new ExecutionContext(classLoader, state);
    final boolean actual = (boolean) invokeMethod.evaluate(context);
    assertTrue(actual);
  }

  private void javaDocSeed(final MethodIdentifier methodIdentifier)
      throws ClassNotFoundException, IllegalAccessException, NoSuchFieldException, NoSuchMethodException {
    final JavaDocSeed seed = new JavaDocSeed(classLoader, methodIdentifier);
    seed.seed(components);
  }
}
