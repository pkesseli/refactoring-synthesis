package uk.ac.ox.cs.refactoring.synthesis.counterexample;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;

import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;

import javax.swing.JPanel;

import org.junit.jupiter.api.Test;

import uk.ac.ox.cs.refactoring.classloader.ClassLoaders;

class ClassLoaderClonerTest {

  private final ClassLoader classLoader = ClassLoaders.createIsolated();

  private final ClassLoaderCloner cloner = new ClassLoaderCloner(classLoader);

  @Test
  void literal() throws Exception {
    final String literal = "asdf";
    assertSame(literal, cloner.clone(literal));
    final Integer number = 1291;
    assertSame(number, cloner.clone(number));
  }

  @Test
  void object() throws Exception {
    final Object original = new Object();
    final Object clone = cloner.clone(original);
    assertNotNull(clone);
    assertNotSame(original, clone);
  }

  @Test
  void jpanel() throws Exception {
    final JPanel original = new JPanel();
    final Object clone = cloner.clone(original);
    assertNotNull(clone);
    assertNotSame(original, clone);
  }

  @Test
  void primitiveArray() throws Exception {
    final int[] original = { 1, 2, 3, 4 };
    final Object clone = cloner.clone(original);
    assertNotNull(clone);
    assertNotSame(original, clone);
    final int[] cloneArray = (int[]) clone;
    assertArrayEquals(original, cloneArray);
  }

  @Test
  void primitiveMultidimensionalArray() throws Exception {
    final int[][][] original = { { { 1 }, { 2, 3 } }, { { 4 }, { 5, 6 } } };
    final Object clone = cloner.clone(original);
    assertNotNull(clone);
    assertNotSame(original, clone);
    final int[][][] cloneArray = (int[][][]) clone;
    assertArrayEquals(original, cloneArray);
  }

  @Test
  void interfaceMock() throws Exception {
    final ItemListener original = mock(ItemListener.class);
    final Object clone = cloner.clone(original);
    assertNotNull(clone);
    assertNotSame(original, clone);

    final Class<?> cloneClass = clone.getClass();
    assertSame(ItemListener.class, Polymorphism.getNonMockitoInterface(cloneClass));
  }

  @Test
  void classMock() throws Exception {
    final MouseAdapter original = mock(MouseAdapter.class);
    final Object clone = cloner.clone(original);
    assertNotNull(clone);
    assertNotSame(original, clone);

    final Class<?> cloneClass = clone.getClass();
    assertSame(MouseAdapter.class, cloneClass.getSuperclass());
  }
}
