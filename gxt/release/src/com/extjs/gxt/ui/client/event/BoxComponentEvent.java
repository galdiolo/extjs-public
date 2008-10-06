/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.event;

import com.extjs.gxt.ui.client.widget.BoxComponent;
import com.google.gwt.user.client.Event;

/**
 * BoxComponent event type.
 * 
 * <p/>Note: For a given event, only the fields which are appropriate will be
 * filled in. The appropriate fields for each event are documented by the event
 * source.
 */
public class BoxComponentEvent extends ComponentEvent {

  /**
   * The event source.
   */
  public BoxComponent boxComponent;

  /**
   * X coordinate
   */
  public int x;

  /**
   * Y coordinate
   */
  public int y;

  /**
   * The width.
   */
  public int width;

  /**
   * The height.
   */
  public int height;

  /**
   * The size.
   */
  public int size;

  /**
   * Creates a new event.
   * 
   * @param component the event source
   */
  public BoxComponentEvent(BoxComponent component) {
    super(component);
    this.boxComponent = component;
  }

  /**
   * Creates a new event.
   * 
   * @param component the event source
   * @param event the event
   */
  public BoxComponentEvent(BoxComponent component, Event event) {
    super(component, event);
    this.boxComponent = component;
  }

  /**
   * Creates a new event.
   * 
   * @param component the box component
   * @param width the width
   * @param height the height
   */
  public BoxComponentEvent(BoxComponent component, int width, int height) {
    super(component);
    this.boxComponent = component;
    this.width = width;
    this.height = height;
  }
}
