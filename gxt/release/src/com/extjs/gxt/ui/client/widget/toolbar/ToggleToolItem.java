/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.toolbar;

import com.extjs.gxt.ui.client.widget.ToggleButton;
import com.google.gwt.user.client.Element;


/**
 * A 2-state tool bar item.
 * 
 * <dl>
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>Toggle</b> : (component, event)<br>
 * <div>Fires when the "pressed" state changes.</div>
 * <ul>
 * <li>component : this</li>
 * </ul>
 * </dd>
 * </dl>
 */
public class ToggleToolItem extends TextToolItem {
  
  /**
   * True to start pressed.
   */
  public boolean pressed;

  /**
   * The wrapped toggle button.
   */
  protected ToggleButton toggleButton;

  /**
   * Creates a new toggle tool item.
   */
  public ToggleToolItem() {
    toggleButton = new ToggleButton();
    initComponent();
  }

  /**
   * Creates a new toggle tool item.
   * 
   * @param text the item's text
   */
  public ToggleToolItem(String text) {
    toggleButton = new ToggleButton(text);
    initComponent();
  }

  protected void initComponent() {
    button = toggleButton;
  }

  /**
   * Returns true if the item is pressed.
   * 
   * @return the pressed state
   */
  public boolean isPressed() {
    return toggleButton.isPressed();
  }

  /**
   * Toggles the current state.
   */
  public void toggle() {
    toggleButton.toggle();
  }

  /**
   * Sets the current pressed state.
   * 
   * @param state true to set pressed state
   */
  public void toggle(boolean state) {
    toggleButton.toggle(state);
  }

  @Override
  protected void onRender(Element target, int index) {
    toggleButton.toggle(pressed);
    super.onRender(target, index);
  }
  
  

}
