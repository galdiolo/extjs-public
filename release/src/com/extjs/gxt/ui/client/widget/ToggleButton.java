/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.google.gwt.user.client.Element;

/**
 * A 2-state toggle button.
 * 
 * <dt><b>Events:</b></dt>
 * <dd><b>Toggle</b> : ButtonEvent(button)<br>
 * <div>Fires when the "pressed" state of this button changes.</div>
 * <ul>
 * <li>button : this</li>
 * </ul>
 * </dd>
 * </dt>
 */
public class ToggleButton extends Button {

  /**
   * True to start pressed.
   */
  public boolean pressed;

  /**
   * Creates a new toggle button.
   */
  public ToggleButton() {
    super();
  }

  /**
   * Creates a new toggle button.
   * 
   * @param text the button text
   */
  public ToggleButton(String text) {
    super(text);
  }

  /**
   * Creates a new toggle button.
   * 
   * @param text the button text
   * @param listener a selection listener
   */
  public ToggleButton(String text, SelectionListener listener) {
    super(text, listener);
  }

  /**
   * Returns true if the button is pressed.
   * 
   * @return the pressed state
   */
  public boolean isPressed() {
    return pressed;
  }

  /**
   * Toggles the current state.
   */
  public void toggle() {
    toggle(!pressed);
  }

  /**
   * Sets the current pressed state.
   * 
   * @param state true to set pressed state
   */
  public void toggle(boolean state) {
    this.pressed = state;
    if (rendered) {
      ButtonEvent be = new ButtonEvent(this);
      el.setStyleName("x-btn-pressed", state);
      if (state) {
        onBlur(null);
        removeStyleName(baseStyle + "-over");
      } else {
        onFocus(null);
      }
      fireEvent(Events.Toggle, be);
    }
  }

  @Override
  protected void onClick(ComponentEvent ce) {
    ce.stopEvent();
    if (!disabled) {
      toggle();
      if (menu != null && !menu.isVisible()) {
        showMenu();
      }
      ButtonEvent be = new ButtonEvent(this);
      be.event = ce.event;
      fireEvent(Events.Select, be);
    }
  }

  @Override
  protected void onFocus(ComponentEvent ce) {
    if (!pressed) {
      super.onFocus(ce);
    }
  }

  @Override
  protected void onMouseDown(ComponentEvent ce) {
    // do nothing
  }

  @Override
  protected void onMouseOver(ComponentEvent ce) {
    if (!pressed) {
      super.onMouseOver(ce);
    }
  }

  @Override
  protected void onMouseUp(ComponentEvent ce) {
    // do nothing
  }

  @Override
  protected void onRender(Element parent, int pos) {
    super.onRender(parent, pos);
    if (pressed) {
      toggle(pressed);
    }
  }
}
