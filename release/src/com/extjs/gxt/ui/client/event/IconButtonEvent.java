/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.event;

import com.extjs.gxt.ui.client.widget.button.IconButton;
import com.google.gwt.user.client.Event;

/**
 * <code>IconButton</code> event type.
 */
public class IconButtonEvent extends BoxComponentEvent {

  public IconButton iconButton;

  public IconButtonEvent(IconButton iconButton) {
    super(iconButton);
    this.iconButton = iconButton;
  }

  public IconButtonEvent(IconButton iconButton, Event event) {
    this(iconButton);
    this.event = event;
  }

}
