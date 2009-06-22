/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.event;

import com.extjs.gxt.ui.client.fx.Draggable;
import com.extjs.gxt.ui.client.widget.Component;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * Draggable event type.
 * 
 * <p/>Note: For a given event, only the fields which are appropriate will be
 * filled in. The appropriate fields for each event are documented by the event
 * source.
 * 
 * @see Draggable
 */
public class DragEvent extends DomEvent {

  /**
   * The draggable instance.
   */
  public Draggable draggable;

  /**
   * The component being dragged.
   */
  public Component component;

  /**
   * The current height.
   */
  public int height;

  /**
   * The current width.
   */
  public int width;

  /**
   * The current x-coordinate value.
   */
  public int x;

  /**
   * The current y-coordinate value.
   */
  public int y;

  /**
   * Returns the event target.
   * 
   * @return the target
   */
  public Element getTarget() {
    return DOM.eventGetTarget(event);
  }

  public DragEvent(Draggable draggable) {
    super(draggable);
    this.draggable = draggable;
  }

}
