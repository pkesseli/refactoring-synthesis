package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.mapsTo;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import java.awt.Dialog;
import java.awt.Dialog.ModalityType;
import java.awt.Frame;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.NoSuchElementException;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.SnippetCandidate;

class java_awt_DialogTest {

  private final Dialog visible = fromVisible(true);

  private final Dialog hidden = fromVisible(false);

  @AfterEach
  void afterEach() {
    visible.dispose();
    hidden.dispose();
  }

  @Test
  void hide() throws Exception {
    assertThat(synthesise("hide"),
        allOf(
            contains(".setVisible("),
            mapsTo(Components::isHidden, visible),
            mapsTo(Components::isHidden, hidden)));
  }

  @Test
  void show() throws Exception {
    assertThat(synthesise("show"),
        allOf(
            contains(".setVisible("),
            mapsTo(Components::isVisible, visible),
            mapsTo(Components::isVisible, hidden)));
  }

  private static SnippetCandidate synthesise(final String methodName) throws ClassNotFoundException,
      IllegalAccessException, NoSuchElementException, NoSuchFieldException, NoSuchMethodException, IOException {
    final SnippetCandidate candidate = synthesiseAlias("java.awt.Dialog", methodName);
    cleanup();
    return candidate;
  }

  /**
   * {@link java.awt.Dialog} maintains a static state which gets mutated during
   * fuzzing. This does not prevent us from finding the correct solution, but
   * can lead to exceptions when checking the solution. We do not load
   * {@link java.awt.Dialog} in isolation, as we do user classes, since security
   * maangers on most VMs would prohibit us from doing so. However, in a
   * practical scenario it would be advisable to turn off this security
   * restriction and improve completeness.
   * 
   * This method cleans up the invalid static state for the purpose of the
   * experiments.
   * 
   * @throws IllegalAccessException if reflective access fails.
   */
  private static void cleanup() throws IllegalAccessException {
    final Field field = FieldUtils.getDeclaredField(java.awt.Dialog.class, "modalDialogs", true);
    final Collection<?> modalDialogs = (Collection<?>) field.get(null);
    for (final Object modalDialog : modalDialogs) {
      final Dialog dialog = (Dialog) modalDialog;
      dialog.dispose();
    }
    modalDialogs.clear();
  }

  private static Dialog fromVisible(final boolean isVisible) {
    final Frame frame = new Frame("Frame");
    final Dialog dialog = new Dialog(frame, "dialog");
    dialog.setModalityType(ModalityType.MODELESS);
    dialog.setVisible(isVisible);
    return dialog;
  }
}
