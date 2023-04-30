
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_awt_ComponentTest {
  @Test
  void action() throws Exception {
    assertThat(synthesiseGPT("this.action(a, b);\n\n", "this.addActionListener(new ActionListener() {\n    public void actionPerformed(ActionEvent e) {\n        // Code to handle action event\n    }\n});\n", "java.awt.Component", "action", "java.awt.Event", "java.lang.Object"), Matchers.anything());
  }

  @Test
  void bounds() throws Exception {
    assertThat(synthesiseGPT("Rectangle bounds = this.bounds();\n\n", "Rectangle bounds = this.getBounds();\n", "java.awt.Component", "bounds"), anyOf(contains("getBounds")));
  }

  @Test
  void deliverEvent() throws Exception {
    assertThat(synthesiseGPT("this.deliverEvent(a);\n", "this.dispatchEvent(a);\n", "java.awt.Component", "deliverEvent", "java.awt.Event"), anyOf(contains("dispatchEvent")));
  }

  @Test
  void disable() throws Exception {
    assertThat(synthesiseGPT("this.disable();\n\n", "this.setEnabled(false);\n", "java.awt.Component", "disable"), anyOf(contains("setEnabled")));
  }

  @Test
  void enable1() throws Exception {
    assertThat(synthesiseGPT("this.enable();\n\n", "this.setEnabled(true);\n", "java.awt.Component", "enable"), anyOf(contains("setEnabled")));
  }

  @Test
  void enable2() throws Exception {
    assertThat(synthesiseGPT("this.enable(a);\n\n", "this.setEnabled(a);\n", "java.awt.Component", "enable", "boolean"), anyOf(contains("setEnabled")));
  }

  @Test
  void gotFocus() throws Exception {
    assertThat(synthesiseGPT("this.gotFocus(a, b);\n", "this.processFocusEvent(new FocusEvent(this, FocusEvent.FOCUS_GAINED));\n", "java.awt.Component", "gotFocus", "java.awt.Event", "java.lang.Object"), Matchers.anything());
  }

  @Test
  void handleEvent() throws Exception {
    assertThat(synthesiseGPT("public void someMethod(Event a) {\n    this.handleEvent(a);\n}\n", "public void someMethod(Event a) {\n    this.processEvent(a);\n}\n", "java.awt.Component", "handleEvent", "java.awt.Event"), Matchers.anything());
  }

  @Test
  void hide() throws Exception {
    assertThat(synthesiseGPT("this.hide();\n\n", "this.setVisible(false);\n", "java.awt.Component", "hide"), anyOf(contains("setVisible")));
  }

  @Test
  void inside() throws Exception {
    assertThat(synthesiseGPT("if (this.inside(a, b)) {\n    // do something\n}\n", "if (this.contains(a, b)) {\n    // do something\n}\n", "java.awt.Component", "inside", "int", "int"), Matchers.anything());
  }

  @Test
  void isFocusTraversable() throws Exception {
    assertThat(synthesiseGPT("this.isFocusTraversable();\n\n", "this.isFocusable();\n", "java.awt.Component", "isFocusTraversable"), anyOf(contains("isFocusable")));
  }

  @Test
  void keyDown() throws Exception {
    assertThat(synthesiseGPT("this.keyDown(a, b);\n", "KeyEvent keyEvent = new KeyEvent(this, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_UNDEFINED, (char)KeyEvent.VK_UNDEFINED, KeyEvent.KEY_LOCATION_UNKNOWN, false);\nthis.processKeyEvent(keyEvent);\n", "java.awt.Component", "keyDown", "java.awt.Event", "int"), Matchers.anything());
  }

  @Test
  void keyUp() throws Exception {
    assertThat(synthesiseGPT("this.keyUp(a, b);\n", "KeyEvent keyEvent = new KeyEvent(this, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, b, KeyEvent.CHAR_UNDEFINED);\nthis.processKeyEvent(keyEvent);\n", "java.awt.Component", "keyUp", "java.awt.Event", "int"), Matchers.anything());
  }

  @Test
  void layout() throws Exception {
    assertThat(synthesiseGPT("this.layout();\n\n", "this.doLayout();\n", "java.awt.Component", "layout"), anyOf(contains("doLayout")));
  }

  @Test
  void locate() throws Exception {
    assertThat(synthesiseGPT("Point location = this.locate(a, b);\n\n", "Point location = this.getComponentAt(a, b).getLocation();\n", "java.awt.Component", "locate", "int", "int"), Matchers.anything());
  }

  @Test
  void location() throws Exception {
    assertThat(synthesiseGPT("Point location = this.location();\n\n", "Point location = this.getLocation();\n", "java.awt.Component", "location"), anyOf(contains("getLocation")));
  }

  @Test
  void lostFocus() throws Exception {
    assertThat(synthesiseGPT("this.lostFocus(a, b);\n\n", "this.processFocusEvent(new FocusEvent(this, FocusEvent.FOCUS_LOST));\n", "java.awt.Component", "lostFocus", "java.awt.Event", "java.lang.Object"), Matchers.anything());
  }

  @Test
  void minimumSize() throws Exception {
    assertThat(synthesiseGPT("Dimension minSize = this.minimumSize();\n\n", "Dimension minSize = this.getMinimumSize();\n", "java.awt.Component", "minimumSize"), anyOf(contains("getMinimumSize")));
  }

  @Test
  void mouseDown() throws Exception {
    assertThat(synthesiseGPT("this.mouseDown(a, b, c);\n", "MouseEvent event = new MouseEvent(this, MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, a, b, c, false);\nthis.processMouseEvent(event);\n", "java.awt.Component", "mouseDown", "java.awt.Event", "int", "int"), Matchers.anything());
  }

  @Test
  void mouseDrag() throws Exception {
    assertThat(synthesiseGPT("this.mouseDrag(a, b, c);\n\n", "MouseEvent event = new MouseEvent(this, MouseEvent.MOUSE_DRAGGED, System.currentTimeMillis(), 0, a, b, c, false);\nthis.processMouseMotionEvent(event);\n", "java.awt.Component", "mouseDrag", "java.awt.Event", "int", "int"), Matchers.anything());
  }

  @Test
  void mouseEnter() throws Exception {
    assertThat(synthesiseGPT("this.mouseEnter(a, b, c);\n\n", "MouseEvent event = new MouseEvent(this, MouseEvent.MOUSE_ENTERED, System.currentTimeMillis(), 0, a, b, 0, false);\nthis.processMouseEvent(event);\n", "java.awt.Component", "mouseEnter", "java.awt.Event", "int", "int"), Matchers.anything());
  }

  @Test
  void mouseExit() throws Exception {
    assertThat(synthesiseGPT("this.mouseExit(a, b, c);\n", "MouseEvent event = new MouseEvent(this, MouseEvent.MOUSE_EXITED, System.currentTimeMillis(), 0, b, c, 0, false);\nthis.processMouseEvent(event);\n", "java.awt.Component", "mouseExit", "java.awt.Event", "int", "int"), Matchers.anything());
  }

  @Test
  void mouseMove() throws Exception {
    assertThat(synthesiseGPT("this.mouseMove(a, b, c);\n", "MouseEvent event = new MouseEvent(this, MouseEvent.MOUSE_MOVED, System.currentTimeMillis(), 0, a, b, c, false);\nthis.processMouseMotionEvent(event);\n", "java.awt.Component", "mouseMove", "java.awt.Event", "int", "int"), Matchers.anything());
  }

  @Test
  void mouseUp() throws Exception {
    assertThat(synthesiseGPT("this.mouseUp(a, b, c);\n\n", "MouseEvent event = new MouseEvent(this, MouseEvent.MOUSE_RELEASED, System.currentTimeMillis(), 0, a, b, c, false);\nthis.processMouseEvent(event);\n", "java.awt.Component", "mouseUp", "java.awt.Event", "int", "int"), Matchers.anything());
  }

  @Test
  void move() throws Exception {
    assertThat(synthesiseGPT("this.move(a, b);\n", "this.setLocation(a, b);\n", "java.awt.Component", "move", "int", "int"), anyOf(contains("setLocation")));
  }

  @Test
  void nextFocus() throws Exception {
    assertThat(synthesiseGPT("this.nextFocus();\n\n", "this.transferFocus();\n", "java.awt.Component", "nextFocus"), Matchers.anything());
  }

  @Test
  void postEvent() throws Exception {
    assertThat(synthesiseGPT("this.postEvent(a);\n", "this.dispatchEvent(a);\n", "java.awt.Component", "postEvent", "java.awt.Event"), Matchers.anything());
  }

  @Test
  void preferredSize() throws Exception {
    assertThat(synthesiseGPT("Dimension preferredSize = this.preferredSize();\n\n", "Dimension preferredSize = this.getPreferredSize();\n", "java.awt.Component", "preferredSize"), anyOf(contains("getPreferredSize")));
  }

  @Test
  void reshape() throws Exception {
    assertThat(synthesiseGPT("this.reshape(a, b, c, d);\n\n", "this.setBounds(a, b, c, d);\n", "java.awt.Component", "reshape", "int", "int", "int", "int"), anyOf(contains("setBounds")));
  }

  @Test
  void resize1() throws Exception {
    assertThat(synthesiseGPT("this.resize(a, b);\n", "this.setSize(a, b);\n", "java.awt.Component", "resize", "int", "int"), anyOf(contains("setSize")));
  }

  @Test
  void resize2() throws Exception {
    assertThat(synthesiseGPT("this.resize(a);\n", "this.setSize(a);\n", "java.awt.Component", "resize", "Dimension"), anyOf(contains("setSize")));
  }

  @Test
  void show1() throws Exception {
    assertThat(synthesiseGPT("this.show();\n\n", "this.setVisible(true);\n", "java.awt.Component", "show"), anyOf(contains("setVisible")));
  }

  @Test
  void show2() throws Exception {
    assertThat(synthesiseGPT("this.show(a);\n\n", "this.setVisible(a);\n", "java.awt.Component", "show", "boolean"), anyOf(contains("setVisible")));
  }

  @Test
  void size() throws Exception {
    assertThat(synthesiseGPT("this.size();\n\n", "this.getSize();\n", "java.awt.Component", "size"), anyOf(contains("getSize")));
  }
}