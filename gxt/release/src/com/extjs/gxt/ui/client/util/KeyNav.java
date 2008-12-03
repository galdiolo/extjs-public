/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.util;

import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.BaseObservable;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Component;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.KeyboardListener;

/**
 * Provides a convenient wrapper for normalized keyboard navigation. Provides an
 * easy way to implement custom navigation schemes for any UI component.
 */
public class KeyNav<E extends ComponentEvent> extends BaseObservable implements Listener<E> {

  private static int keyEvent;
  private Component component;
  private boolean cancelBubble;
  private boolean preventDefault = false;

  static {
    // mods press up
    // Safari no yes
    // IE yes yes
    // FF yes no
    // Opera yes no

    // nav press down
    // IE no yes
    // FF yes yes
    // Safari no yes
    // Opera yes yes
    if (GXT.isIE || GXT.isSafari || GXT.isGecko) {
      keyEvent = Event.ONKEYDOWN;
    } else {
      keyEvent = Event.ONKEYPRESS;
    }
  }

  /**
   * Creates a new KeyNav without a target component. Events must be passed to
   * the {@link #handleEvent(BaseEvent)} method.
   */
  public KeyNav() {

  }

  /**
   * Creates a new key nav for the specified target. The KeyNav will listen for
   * the key events.
   * 
   * @param target the target component
   */
  public KeyNav(Component target) {
    bind(target);
  }

  public void addKeyNavListener(KeyNavListener listener) {
    addListener(KeyboardListener.KEY_ALT, listener);
    addListener(KeyboardListener.KEY_BACKSPACE, listener);
    addListener(KeyboardListener.KEY_CTRL, listener);
    addListener(KeyboardListener.KEY_DELETE, listener);
    addListener(KeyboardListener.KEY_DOWN, listener);
    addListener(KeyboardListener.KEY_END, listener);
    addListener(KeyboardListener.KEY_ENTER, listener);
    addListener(KeyboardListener.KEY_ESCAPE, listener);
    addListener(KeyboardListener.KEY_HOME, listener);
    addListener(KeyboardListener.KEY_LEFT, listener);
    addListener(KeyboardListener.KEY_PAGEDOWN, listener);
    addListener(KeyboardListener.KEY_PAGEUP, listener);
    addListener(KeyboardListener.KEY_RIGHT, listener);
    addListener(KeyboardListener.KEY_SHIFT, listener);
    addListener(KeyboardListener.KEY_TAB, listener);
    addListener(KeyboardListener.KEY_UP, listener);
  }

  /**
   * Binds the key nav to the component.
   * 
   * @param target the target component
   */
  public void bind(final Component target) {
    if (this.component != null) {
      this.component.removeListener(keyEvent, this);
    }
    if (target != null) {
      target.addListener(keyEvent, this);
      target.sinkEvents(keyEvent);
    }
    this.component = target;
  }

  /**
   * Returns the cancel bubble state.
   * 
   * @return true if bubbling is cancelled
   */
  public boolean getCancelBubble() {
    return cancelBubble;
  }

  /**
   * Returns the target component.
   * 
   * @return the target component
   */
  public Component getComponent() {
    return component;
  }

  /**
   * Returns true if the default event action is being cancelled.
   * 
   * @return true if preventing default
   */
  public boolean getPreventDefault() {
    return preventDefault;
  }

  public void handleEvent(ComponentEvent ce) {
    if (ce.type == keyEvent) {
      if (component.getElement() != ce.event.getCurrentTarget()) {
        return;
      }
      if (cancelBubble) {
        ce.cancelBubble();
      }
      if (preventDefault) {
        ce.preventDefault();
      }

      int code = ce.getKeyCode();

      E e = (E) ce;

      onKeyPress(e);

      switch (code) {
        case KeyboardListener.KEY_ALT:
          onAlt(e);
          break;
        case KeyboardListener.KEY_BACKSPACE:
          onBackspace(e);
          break;
        case KeyboardListener.KEY_CTRL:
          onControl(e);
          break;
        case KeyboardListener.KEY_DELETE:
          onDelete(e);
          break;
        case KeyboardListener.KEY_DOWN:
          onDown(e);
          break;
        case KeyboardListener.KEY_END:
          onEnd(e);
          break;
        case KeyboardListener.KEY_ENTER:
          onEnter(e);
          break;
        case KeyboardListener.KEY_ESCAPE:
          onEsc(e);
          break;
        case KeyboardListener.KEY_HOME:
          onHome(e);
          break;
        case KeyboardListener.KEY_LEFT:
          onLeft(e);
          break;
        case KeyboardListener.KEY_PAGEDOWN:
          onPageDown(e);
          break;
        case KeyboardListener.KEY_PAGEUP:
          onPageUp(e);
          break;
        case KeyboardListener.KEY_SHIFT:
          onShift(e);
          break;
        case KeyboardListener.KEY_TAB:
          onTab(e);
          break;
        case KeyboardListener.KEY_RIGHT:
          onRight(e);
          break;
        case KeyboardListener.KEY_UP:
          onUp(e);
          break;
      }

      fireEvent(code, e);
    }
  }

  public void onKeyPress(E ce) {

  }

  public void onAlt(E ce) {

  }

  public void onBackspace(E ce) {

  }

  public void onControl(E ce) {

  }

  public void onDelete(E ce) {

  }

  public void onDown(E ce) {

  }

  public void onEnd(E ce) {

  }

  public void onEnter(E ce) {

  }

  public void onEsc(E ce) {

  }

  public void onHome(E ce) {

  }

  public void onLeft(E ce) {

  }

  public void onPageDown(E ce) {

  }

  public void onPageUp(E ce) {

  }

  public void onRight(E ce) {

  }

  public void onShift(E ce) {

  }

  public void onTab(E ce) {

  }

  public void onUp(E ce) {

  }

  public void removeKeyNavListener(KeyNavListener listener) {
    removeListener(KeyboardListener.KEY_ALT, listener);
    removeListener(KeyboardListener.KEY_BACKSPACE, listener);
    removeListener(KeyboardListener.KEY_CTRL, listener);
    removeListener(KeyboardListener.KEY_DELETE, listener);
    removeListener(KeyboardListener.KEY_DOWN, listener);
    removeListener(KeyboardListener.KEY_END, listener);
    removeListener(KeyboardListener.KEY_ENTER, listener);
    removeListener(KeyboardListener.KEY_ESCAPE, listener);
    removeListener(KeyboardListener.KEY_HOME, listener);
    removeListener(KeyboardListener.KEY_LEFT, listener);
    removeListener(KeyboardListener.KEY_PAGEDOWN, listener);
    removeListener(KeyboardListener.KEY_PAGEUP, listener);
    removeListener(KeyboardListener.KEY_RIGHT, listener);
    removeListener(KeyboardListener.KEY_SHIFT, listener);
    removeListener(KeyboardListener.KEY_TAB, listener);
    removeListener(KeyboardListener.KEY_UP, listener);
  }

  /**
   * True to stop event bubbling when the key nav intercepts a key (defaults to
   * false).
   * 
   * @param cancelBubble the cancel bubble state
   */
  public void setCancelBubble(boolean cancelBubble) {
    this.cancelBubble = cancelBubble;
  }

  /**
   * True to prevent the default action of the key event when the key nav
   * intercepts a key (defaults to false).
   * 
   * @param preventDefault true to prevent the default
   */
  public void setPreventDefault(boolean preventDefault) {
    this.preventDefault = preventDefault;
  }

}
