/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.event;

import com.extjs.gxt.ui.client.fx.Resizable;
import com.extjs.gxt.ui.client.widget.Component;
import com.google.gwt.user.client.Event;

/**
 * Resizable event type.
 * 
 * @see Resizable
 */
public class ResizeEvent extends BaseEvent {

  /**
   * The resizable instance.
   */
  public Resizable resizable;

  /**
   * The component being resized.
   */
  public Component component;

  /**
   * The DOM event.
   */
  public Event event;

  public ResizeEvent(Resizable resizable) {
    this.resizable = resizable;
  }
  
  public ResizeEvent(Resizable resizable, Component component, Event event) {
    this.resizable = resizable;
    this.component = component;
    this.event = event;
  }

}
