
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
assertThat (synthesiseGPT ("this.action(param0, param1);" , "\nthis.getBounds()\n;" , "java.awt.Component" , "action" , "java.awt.Event" , "java.lang.Object") , Matchers . anything ()) ;
  }

  @Test
  void bounds() throws Exception {
assertThat (synthesiseGPT ("this.bounds();" , "\nthis.getBounds()\n;" , "java.awt.Component" , "bounds") , anyOf (contains ("getBounds"))) ;
  }

  @Test
  void deliverEvent() throws Exception {
assertThat (synthesiseGPT ("this.deliverEvent(param0);" , "\nthis.setEnabled(true);\n;" , "java.awt.Component" , "deliverEvent" , "java.awt.Event") , anyOf (contains ("dispatchEvent"))) ;
  }

  @Test
  void disable() throws Exception {
assertThat (synthesiseGPT ("this.disable();" , "\nthis.setEnabled(false);\n;" , "java.awt.Component" , "disable") , anyOf (contains ("setEnabled"))) ;
  }

  @Test
  void enable1() throws Exception {
assertThat (synthesiseGPT ("this.enable();" , "this.setEnabled(true);" , "java.awt.Component" , "enable") , anyOf (contains ("setEnabled"))) ;
  }

  @Test
  void enable2() throws Exception {
assertThat (synthesiseGPT ("this.enable(param0);" , "" , "java.awt.Component" , "enable" , "boolean") , anyOf (contains ("setEnabled"))) ;
  }

  @Test
  void gotFocus() throws Exception {
assertThat (synthesiseGPT ("this.gotFocus(param0, param1);" , "\nthis.processEvent(new FocusEvent(this, FocusEvent.FOCUS_GAINED));\n;" , "java.awt.Component" , "gotFocus" , "java.awt.Event" , "java.lang.Object") , Matchers . anything ()) ;
  }

  @Test
  void handleEvent() throws Exception {
assertThat (synthesiseGPT ("this.handleEvent(param0);" , "\nthis.setVisible(true);\n;" , "java.awt.Component" , "handleEvent" , "java.awt.Event") , Matchers . anything ()) ;
  }

  @Test
  void hide() throws Exception {
assertThat (synthesiseGPT ("this.hide();" , "\nthis.setVisible(false);\n;" , "java.awt.Component" , "hide") , anyOf (contains ("setVisible"))) ;
  }

  @Test
  void inside() throws Exception {
assertThat (synthesiseGPT ("this.inside(param0, param1);" , "this.isFocusable();" , "java.awt.Component" , "inside" , "int" , "int") , Matchers . anything ()) ;
  }

  @Test
  void isFocusTraversable() throws Exception {
assertThat (synthesiseGPT ("this.isFocusTraversable();" , "\nthis.isFocusable()\n;" , "java.awt.Component" , "isFocusTraversable") , anyOf (contains ("isFocusable"))) ;
  }

  @Test
  void keyDown() throws Exception {
assertThat (synthesiseGPT ("this.keyDown(param0, param1);" , "" , "java.awt.Component" , "keyDown" , "java.awt.Event" , "int") , Matchers . anything ()) ;
  }

  @Test
  void keyUp() throws Exception {
assertThat (synthesiseGPT ("this.keyUp(param0, param1);" , "\nKeyEvent keyEvent = new KeyEvent(this, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, param1, KeyEvent.CHAR_UNDEFINED);\ndispatchEvent(keyEvent);\n;" , "java.awt.Component" , "keyUp" , "java.awt.Event" , "int") , Matchers . anything ()) ;
  }

  @Test
  void layout() throws Exception {
assertThat (synthesiseGPT ("this.layout();" , "\nthis.getComponentAt(0, 0);\n;" , "java.awt.Component" , "layout") , anyOf (contains ("doLayout"))) ;
  }

  @Test
  void locate() throws Exception {
assertThat (synthesiseGPT ("this.locate(param0, param1);" , "\nthis.getLocation().x = param0;\nthis.getLocation().y = param1;\n;" , "java.awt.Component" , "locate" , "int" , "int") , Matchers . anything ()) ;
  }

  @Test
  void location() throws Exception {
assertThat (synthesiseGPT ("this.location();" , "\nthis.getLocation()\n;" , "java.awt.Component" , "location") , anyOf (contains ("getLocation"))) ;
  }

  @Test
  void lostFocus() throws Exception {
assertThat (synthesiseGPT ("this.lostFocus(param0, param1);" , "\nthis.getMinimumSize();\n;" , "java.awt.Component" , "lostFocus" , "java.awt.Event" , "java.lang.Object") , Matchers . anything ()) ;
  }

  @Test
  void minimumSize() throws Exception {
assertThat (synthesiseGPT ("this.minimumSize();" , "\nthis.getMinimumSize()\n;" , "java.awt.Component" , "minimumSize") , anyOf (contains ("getMinimumSize"))) ;
  }

  @Test
  void mouseDown() throws Exception {
assertThat (synthesiseGPT ("this.mouseDown(param0, param1, param2);" , "\nMouseEvent event = new MouseEvent(this, MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, param1, param2, 1, false);\nthis.processMouseMotionEvent(event);\n;" , "java.awt.Component" , "mouseDown" , "java.awt.Event" , "int" , "int") , Matchers . anything ()) ;
  }

  @Test
  void mouseDrag() throws Exception {
assertThat (synthesiseGPT ("this.mouseDrag(param0, param1, param2);" , "\nMouseEvent event = new MouseEvent(this, MouseEvent.MOUSE_DRAGGED, System.currentTimeMillis(), 0, param1, param2, 0, false);\nthis.dispatchEvent(event);\n;" , "java.awt.Component" , "mouseDrag" , "java.awt.Event" , "int" , "int") , Matchers . anything ()) ;
  }

  @Test
  void mouseEnter() throws Exception {
assertThat (synthesiseGPT ("this.mouseEnter(param0, param1, param2);" , "\nMouseEvent event = new MouseEvent(this, MouseEvent.MOUSE_ENTERED, System.currentTimeMillis(), 0, param1, param2, 0, false);\nthis.dispatchEvent(event);\n;" , "java.awt.Component" , "mouseEnter" , "java.awt.Event" , "int" , "int") , Matchers . anything ()) ;
  }

  @Test
  void mouseExit() throws Exception {
assertThat (synthesiseGPT ("this.mouseExit(param0, param1, param2);" , "\nMouseEvent event = new MouseEvent(this, MouseEvent.MOUSE_EXITED, System.currentTimeMillis(), 0, param1, param2, 0, false);\nthis.processMouseMotionEvent(event);\n;" , "java.awt.Component" , "mouseExit" , "java.awt.Event" , "int" , "int") , Matchers . anything ()) ;
  }

  @Test
  void mouseMove() throws Exception {
assertThat (synthesiseGPT ("this.mouseMove(param0, param1, param2);" , "\nMouseEvent event = new MouseEvent(this, MouseEvent.MOUSE_MOVED, System.currentTimeMillis(), 0, param1, param2, 0, false);\nthis.dispatchEvent(event);\n;" , "java.awt.Component" , "mouseMove" , "java.awt.Event" , "int" , "int") , Matchers . anything ()) ;
  }

  @Test
  void mouseUp() throws Exception {
assertThat (synthesiseGPT ("this.mouseUp(param0, param1, param2);" , "\nthis.setLocation(param1, param2);\n;" , "java.awt.Component" , "mouseUp" , "java.awt.Event" , "int" , "int") , Matchers . anything ()) ;
  }

  @Test
  void move() throws Exception {
assertThat (synthesiseGPT ("this.move(param0, param1);" , "this.setLocation(param0, param1);" , "java.awt.Component" , "move" , "int" , "int") , anyOf (contains ("setLocation"))) ;
  }

  @Test
  void nextFocus() throws Exception {
assertThat (synthesiseGPT ("this.nextFocus();" , "\nthis.dispatchEvent(new FocusEvent(this, FocusEvent.FOCUS_GAINED));\n;" , "java.awt.Component" , "nextFocus") , Matchers . anything ()) ;
  }

  @Test
  void postEvent() throws Exception {
assertThat (synthesiseGPT ("this.postEvent(param0);" , "\nToolkit.getDefaultToolkit().getSystemEventQueue().postEvent(param0);\n;" , "java.awt.Component" , "postEvent" , "java.awt.Event") , Matchers . anything ()) ;
  }

  @Test
  void preferredSize() throws Exception {
assertThat (synthesiseGPT ("this.preferredSize();" , "" , "java.awt.Component" , "preferredSize") , anyOf (contains ("getPreferredSize"))) ;
  }

  @Test
  void reshape() throws Exception {
assertThat (synthesiseGPT ("this.reshape(param0, param1, param2, param3);" , "" , "java.awt.Component" , "reshape" , "int" , "int" , "int" , "int") , anyOf (contains ("setBounds"))) ;
  }

  @Test
  void resize1() throws Exception {
assertThat (synthesiseGPT ("this.resize(param0, param1);" , "\nthis.setSize(new Dimension(param0, param1));\n;" , "java.awt.Component" , "resize" , "int" , "int") , anyOf (contains ("setSize"))) ;
  }

  @Test
  void resize2() throws Exception {
assertThat (synthesiseGPT ("this.resize(param0);" , "this.setSize(param0);" , "java.awt.Component" , "resize" , "Dimension") , anyOf (contains ("setSize"))) ;
  }

  @Test
  void show1() throws Exception {
assertThat (synthesiseGPT ("this.show();" , "\nthis.setVisible(true);\n;" , "java.awt.Component" , "show") , anyOf (contains ("setVisible"))) ;
  }

  @Test
  void show2() throws Exception {
assertThat (synthesiseGPT ("this.show(param0);" , "" , "java.awt.Component" , "show" , "boolean") , anyOf (contains ("setVisible"))) ;
  }

  @Test
  void size() throws Exception {
assertThat (synthesiseGPT ("this.size();" , "\nthis.getSize()\n;" , "java.awt.Component" , "size") , anyOf (contains ("getSize"))) ;
  }
}
