/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.event;

import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.menu.Menu;

/**
 * Button event type.
 * 
 * <p/>Note: For a given event, only the fields which are appropriate will be
 * filled in. The appropriate fields for each event are documented by the event
 * source.
 * 
 * @see Button
 */
public class ButtonEvent extends ComponentEvent {

  /**
   * The source button.
   */
  public Button button;

  /**
   * The button's menu.
   */
  public Menu menu;

  /**
   * Creates a new button event.
   * 
   * @param button the source button
   */
  public ButtonEvent(Button button) {
    super(button);
    this.button = button;
  }

}
