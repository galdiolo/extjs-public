/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.event;

import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.gwt.user.client.Event;

/**
 * Window event type.
 * 
 * @see Window
 * @see Dialog
 */
public class WindowEvent extends BoxComponentEvent {

  /**
   * The source window.
   */
  public Window window;

  /**
   * The button that was clicked.
   */
  public Button buttonClicked;

  public WindowEvent(Window window) {
    super(window);
    this.window = window;
  }
  
  public WindowEvent(Window window, Event event) {
    this(window);
    this.event = event;
  }

  public WindowEvent(Window window, Button buttonClicked) {
    this(window);
    this.buttonClicked = buttonClicked;
  }

}
