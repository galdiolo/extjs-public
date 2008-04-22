/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.util;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.TypedListener;
import com.extjs.gxt.ui.client.widget.Component;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.KeyboardListener;

/**
 * Convenient helper class to process a component's key events.
 */
public class KeyNav extends Observable implements Listener<ComponentEvent> {

  private static int keyEvent;
  private Component component;
  private boolean cancelBubble = true, preventDefault;

  static {
    if (GXT.isIE) {
      keyEvent = Event.ONKEYDOWN;
    } else if (GXT.isSafari) {
      keyEvent = Event.ONKEYUP;
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
    TypedListener tl = new TypedListener(listener);
    addListener(KeyboardListener.KEY_ALT, tl);
    addListener(KeyboardListener.KEY_BACKSPACE, tl);
    addListener(KeyboardListener.KEY_CTRL, tl);
    addListener(KeyboardListener.KEY_DELETE, tl);
    addListener(KeyboardListener.KEY_DOWN, tl);
    addListener(KeyboardListener.KEY_END, tl);
    addListener(KeyboardListener.KEY_ENTER, tl);
    addListener(KeyboardListener.KEY_ESCAPE, tl);
    addListener(KeyboardListener.KEY_HOME, tl);
    addListener(KeyboardListener.KEY_LEFT, tl);
    addListener(KeyboardListener.KEY_PAGEDOWN, tl);
    addListener(KeyboardListener.KEY_PAGEUP, tl);
    addListener(KeyboardListener.KEY_RIGHT, tl);
    addListener(KeyboardListener.KEY_SHIFT, tl);
    addListener(KeyboardListener.KEY_TAB, tl);
    addListener(KeyboardListener.KEY_UP, tl);
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
      if (target.isRendered()) {
        target.el.addEventsSunk(Event.KEYEVENTS);
      } else {
        target.addListener(Events.Render, new Listener<ComponentEvent>() {
          public void handleEvent(ComponentEvent be) {
            removeListener(Events.Render, this);
            target.el.addEventsSunk(Event.KEYEVENTS);
          }
        });
      }
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

  public boolean getPreventDefault() {
    return preventDefault;
  }

  public void handleEvent(ComponentEvent ce) {
    if (cancelBubble) {
      ce.cancelBubble();
    }
    if (preventDefault) {
      ce.preventDefault();
    }
    if (ce.type == keyEvent) {
      int code = ce.getKeyCode();
      ComponentEvent c = new ComponentEvent(component, ce.event);
      c.component = component;
      c.event = ce.event;

      switch (code) {
        case KeyboardListener.KEY_ALT:
          onAlt(c);
          break;
        case KeyboardListener.KEY_BACKSPACE:
          onBackspace(c);
          break;
        case KeyboardListener.KEY_CTRL:
          onControl(c);
          break;
        case KeyboardListener.KEY_DELETE:
          onDelete(c);
          break;
        case KeyboardListener.KEY_DOWN:
          onDown(c);
          break;
        case KeyboardListener.KEY_END:
          onEnd(c);
          break;
        case KeyboardListener.KEY_ENTER:
          onEnter(c);
          break;
        case KeyboardListener.KEY_ESCAPE:
          onEsc(c);
          break;
        case KeyboardListener.KEY_HOME:
          onHome(c);
          break;
        case KeyboardListener.KEY_LEFT:
          onLeft(c);
          break;
        case KeyboardListener.KEY_PAGEDOWN:
          onPageDown(c);
          break;
        case KeyboardListener.KEY_PAGEUP:
          onPageUp(c);
          break;
        case KeyboardListener.KEY_SHIFT:
          onShift(c);
          break;
        case KeyboardListener.KEY_TAB:
          onTab(c);
          break;
        case KeyboardListener.KEY_RIGHT:
          onRight(c);
          break;
        case KeyboardListener.KEY_UP:
          onUp(c);
          break;
      }

      fireEvent(code, c);
    }
  }

  public void onAlt(ComponentEvent ce) {

  }

  public void onBackspace(ComponentEvent ce) {

  }

  public void onControl(ComponentEvent ce) {

  }

  public void onDelete(ComponentEvent ce) {

  }

  public void onDown(ComponentEvent ce) {

  }

  public void onEnd(ComponentEvent ce) {

  }

  public void onEnter(ComponentEvent ce) {

  }

  public void onEsc(ComponentEvent ce) {

  }

  public void onHome(ComponentEvent ce) {

  }

  public void onLeft(ComponentEvent ce) {

  }

  public void onPageDown(ComponentEvent ce) {

  }

  public void onPageUp(ComponentEvent ce) {

  }

  public void onRight(ComponentEvent ce) {

  }

  public void onShift(ComponentEvent ce) {

  }

  public void onTab(ComponentEvent ce) {

  }

  public void onUp(ComponentEvent ce) {

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
   * True to stop event bubbling (defaults to true).
   * 
   * @param cancelBubble the cancel bubble state
   */
  public void setCancelBubble(boolean cancelBubble) {
    this.cancelBubble = cancelBubble;
  }

  public void setPreventDefault(boolean preventDefault) {
    this.preventDefault = preventDefault;
  }

}
