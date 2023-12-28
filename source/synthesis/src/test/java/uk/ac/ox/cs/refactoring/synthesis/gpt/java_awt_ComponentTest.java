
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
assertThat (synthesiseGPT ("action" , "this.action(param0, param1);" , "" , "java.awt.Component" , "action" , "java.awt.Event" , "java.lang.Object") , Matchers . anything ()) ;
  }

  @Test
  void bounds() throws Exception {
assertThat (synthesiseGPT ("bounds" , "this.bounds();" , "\nthis.getBounds();\n" , "java.awt.Component" , "bounds") , anyOf (contains ("getBounds"))) ;
  }

  @Test
  void deliverEvent() throws Exception {
assertThat (synthesiseGPT ("deliverEvent" , "this.deliverEvent(param0);" , "\nthis.dispatchEvent(param0);\n" , "java.awt.Component" , "deliverEvent" , "java.awt.Event") , anyOf (contains ("dispatchEvent"))) ;
  }

  @Test
  void disable() throws Exception {
assertThat (synthesiseGPT ("disable" , "this.disable();" , "\nthis.setEnabled(false);\n" , "java.awt.Component" , "disable") , anyOf (contains ("setEnabled"))) ;
  }

  @Test
  void enable1() throws Exception {
assertThat (synthesiseGPT ("enable1" , "this.enable();" , "\nthis.setEnabled(true);\n" , "java.awt.Component" , "enable") , anyOf (contains ("setEnabled"))) ;
  }

  @Test
  void enable2() throws Exception {
assertThat (synthesiseGPT ("enable2" , "this.enable(param0);" , "\nthis.setEnabled(param0);\n" , "java.awt.Component" , "enable" , "boolean") , anyOf (contains ("setEnabled"))) ;
  }

  @Test
  void gotFocus() throws Exception {
assertThat (synthesiseGPT ("gotFocus" , "this.gotFocus(param0, param1);" , "\nfinal FocusEvent focusEvent = new FocusEvent(this, FocusEvent.FOCUS_GAINED);\nthis.processFocusEvent(focusEvent);\n" , "java.awt.Component" , "gotFocus" , "java.awt.Event" , "java.lang.Object") , anyOf (contains ("processFocusEvent"))) ;
  }

  @Test
  void handleEvent() throws Exception {
assertThat (synthesiseGPT ("handleEvent" , "this.handleEvent(param0);" , "\nfinal AWTEvent event = param0;\nthis.processEvent(event);\n" , "java.awt.Component" , "handleEvent" , "java.awt.Event") , anyOf (contains ("processEvent"))) ;
  }

  @Test
  void hide() throws Exception {
assertThat (synthesiseGPT ("hide" , "this.hide();" , "\nthis.setVisible(false);\n" , "java.awt.Component" , "hide") , anyOf (contains ("setVisible"))) ;
  }

  @Test
  void inside() throws Exception {
assertThat (synthesiseGPT ("inside" , "this.inside(param0, param1);" , "\nthis.contains(param0, param1);\n" , "java.awt.Component" , "inside" , "int" , "int") , anyOf (contains ("contains"))) ;
  }

  @Test
  void isFocusTraversable() throws Exception {
assertThat (synthesiseGPT ("isFocusTraversable" , "this.isFocusTraversable();" , "\nthis.isFocusable();\n" , "java.awt.Component" , "isFocusTraversable") , anyOf (contains ("isFocusable"))) ;
  }

  @Test
  void keyDown() throws Exception {
assertThat (synthesiseGPT ("keyDown" , "this.keyDown(param0, param1);" , "\nfinal KeyEvent keyEvent = new KeyEvent(this, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, param1, param0.getKeyChar(), KeyEvent.KEY_LOCATION_STANDARD);\nthis.processKeyEvent(keyEvent);\n" , "java.awt.Component" , "keyDown" , "java.awt.Event" , "int") , anyOf (contains ("processKeyEvent"))) ;
  }

  @Test
  void keyUp() throws Exception {
assertThat (synthesiseGPT ("keyUp" , "this.keyUp(param0, param1);" , "\nfinal KeyEvent keyEvent = new KeyEvent(this, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, param1, (char)param1);\nthis.processKeyEvent(keyEvent);\n" , "java.awt.Component" , "keyUp" , "java.awt.Event" , "int") , anyOf (contains ("processKeyEvent"))) ;
  }

  @Test
  void layout() throws Exception {
assertThat (synthesiseGPT ("layout" , "this.layout();" , "\nthis.doLayout();\n" , "java.awt.Component" , "layout") , anyOf (contains ("doLayout"))) ;
  }

  @Test
  void locate() throws Exception {
assertThat (synthesiseGPT ("locate" , "this.locate(param0, param1);" , "\nthis.getComponentAt(param0, param1);\n" , "java.awt.Component" , "locate" , "int" , "int") , anyOf (contains ("getComponentAt"))) ;
  }

  @Test
  void location() throws Exception {
assertThat (synthesiseGPT ("location" , "this.location();" , "\nthis.getLocation();\n" , "java.awt.Component" , "location") , anyOf (contains ("getLocation"))) ;
  }

  @Test
  void lostFocus() throws Exception {
assertThat (synthesiseGPT ("lostFocus" , "this.lostFocus(param0, param1);" , "\nfinal FocusEvent focusEvent = new FocusEvent(this, FocusEvent.FOCUS_LOST);\nthis.processFocusEvent(focusEvent);\n" , "java.awt.Component" , "lostFocus" , "java.awt.Event" , "java.lang.Object") , anyOf (contains ("processFocusEvent"))) ;
  }

  @Test
  void minimumSize() throws Exception {
assertThat (synthesiseGPT ("minimumSize" , "this.minimumSize();" , "\nthis.getMinimumSize();\n" , "java.awt.Component" , "minimumSize") , anyOf (contains ("getMinimumSize"))) ;
  }

  @Test
  void mouseDown() throws Exception {
assertThat (synthesiseGPT ("mouseDown" , "this.mouseDown(param0, param1, param2);" , "\nfinal MouseEvent mouseEvent = new MouseEvent(this, MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, param1, param2, 1, false);\nthis.processMouseEvent(mouseEvent);\n" , "java.awt.Component" , "mouseDown" , "java.awt.Event" , "int" , "int") , anyOf (contains ("processMouseEvent"))) ;
  }

  @Test
  void mouseDrag() throws Exception {
assertThat (synthesiseGPT ("mouseDrag" , "this.mouseDrag(param0, param1, param2);" , "\nfinal MouseEvent mouseEvent = new MouseEvent(this, MouseEvent.MOUSE_DRAGGED, System.currentTimeMillis(), 0, param1, param2, 0, false);\nthis.processMouseMotionEvent(mouseEvent);\n" , "java.awt.Component" , "mouseDrag" , "java.awt.Event" , "int" , "int") , anyOf (contains ("processMouseMotionEvent"))) ;
  }

  @Test
  void mouseEnter() throws Exception {
assertThat (synthesiseGPT ("mouseEnter" , "this.mouseEnter(param0, param1, param2);" , "\nfinal MouseEvent mouseEvent = new MouseEvent(this, MouseEvent.MOUSE_ENTERED, System.currentTimeMillis(), 0, param1, param2, 0, false);\nthis.processMouseEvent(mouseEvent);\n" , "java.awt.Component" , "mouseEnter" , "java.awt.Event" , "int" , "int") , anyOf (contains ("processMouseEvent"))) ;
  }

  @Test
  void mouseExit() throws Exception {
assertThat (synthesiseGPT ("mouseExit" , "this.mouseExit(param0, param1, param2);" , "\nfinal MouseEvent mouseEvent = new MouseEvent(this, MouseEvent.MOUSE_EXITED, System.currentTimeMillis(), 0, param1, param2, 0, false);\nthis.processMouseEvent(mouseEvent);\n" , "java.awt.Component" , "mouseExit" , "java.awt.Event" , "int" , "int") , anyOf (contains ("processMouseEvent"))) ;
  }

  @Test
  void mouseMove() throws Exception {
assertThat (synthesiseGPT ("mouseMove" , "this.mouseMove(param0, param1, param2);" , "\nfinal MouseEvent mouseEvent = new MouseEvent(this, MouseEvent.MOUSE_MOVED, System.currentTimeMillis(), 0, param1, param2, 0, false);\nthis.processMouseMotionEvent(mouseEvent);\n" , "java.awt.Component" , "mouseMove" , "java.awt.Event" , "int" , "int") , anyOf (contains ("processMouseMotionEvent"))) ;
  }

  @Test
  void mouseUp() throws Exception {
assertThat (synthesiseGPT ("mouseUp" , "this.mouseUp(param0, param1, param2);" , "\nfinal MouseEvent mouseEvent = new MouseEvent(this, MouseEvent.MOUSE_RELEASED, System.currentTimeMillis(), 0, param1, param2, 1, false);\nthis.processMouseEvent(mouseEvent);\n" , "java.awt.Component" , "mouseUp" , "java.awt.Event" , "int" , "int") , anyOf (contains ("processMouseEvent"))) ;
  }

  @Test
  void move() throws Exception {
assertThat (synthesiseGPT ("move" , "this.move(param0, param1);" , "\nthis.setLocation(param0, param1);\n" , "java.awt.Component" , "move" , "int" , "int") , anyOf (contains ("setLocation"))) ;
  }

  @Test
  void nextFocus() throws Exception {
assertThat (synthesiseGPT ("nextFocus" , "this.nextFocus();" , "\nthis.transferFocus();\n" , "java.awt.Component" , "nextFocus") , anyOf (contains ("transferFocus"))) ;
  }

  @Test
  void postEvent() throws Exception {
assertThat (synthesiseGPT ("postEvent" , "this.postEvent(param0);" , "\nfinal AWTEvent event = param0;\nthis.dispatchEvent(event);\n" , "java.awt.Component" , "postEvent" , "java.awt.Event") , anyOf (contains ("dispatchEvent"))) ;
  }

  @Test
  void preferredSize() throws Exception {
assertThat (synthesiseGPT ("preferredSize" , "this.preferredSize();" , "\nthis.getPreferredSize();\n" , "java.awt.Component" , "preferredSize") , anyOf (contains ("getPreferredSize"))) ;
  }

  @Test
  void reshape() throws Exception {
assertThat (synthesiseGPT ("reshape" , "this.reshape(param0, param1, param2, param3);" , "\nthis.setBounds(param0, param1, param2, param3);\n" , "java.awt.Component" , "reshape" , "int" , "int" , "int" , "int") , anyOf (contains ("setBounds"))) ;
  }

  @Test
  void resize1() throws Exception {
assertThat (synthesiseGPT ("resize1" , "this.resize(param0, param1);" , "\nthis.setSize(param0, param1);\n" , "java.awt.Component" , "resize" , "int" , "int") , anyOf (contains ("setSize"))) ;
  }

  @Test
  void resize2() throws Exception {
assertThat (synthesiseGPT ("resize2" , "this.resize(param0);" , "\nthis.setSize(param0);\n" , "java.awt.Component" , "resize" , "java.awt.Dimension") , anyOf (contains ("setSize"))) ;
  }

  @Test
  void show1() throws Exception {
assertThat (synthesiseGPT ("show1" , "this.show();" , "\nthis.setVisible(true);\n" , "java.awt.Component" , "show") , anyOf (contains ("setVisible"))) ;
  }

  @Test
  void show2() throws Exception {
assertThat (synthesiseGPT ("show2" , "this.show(param0);" , "\nthis.setVisible(param0);\n" , "java.awt.Component" , "show" , "boolean") , anyOf (contains ("setVisible"))) ;
  }

  @Test
  void size() throws Exception {
assertThat (synthesiseGPT ("size" , "this.size();" , "\nthis.getSize();\n" , "java.awt.Component" , "size") , anyOf (contains ("getSize"))) ;
  }
}
