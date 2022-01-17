package uk.ac.ox.cs.refactoring.synthesis.experiment;

import java.awt.Component;

class Components {

  static boolean isVisible(final Component component, final Void result) {
    return component.isVisible();
  }

  static boolean isHidden(final Component component, final Void result) {
    return !component.isVisible();
  }

  private Components() {
  }
}
