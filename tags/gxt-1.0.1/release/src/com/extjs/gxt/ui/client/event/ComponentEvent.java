/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.event;

import java.util.Map;

import com.extjs.gxt.ui.client.widget.Component;
import com.google.gwt.user.client.Event;

/**
 * Component event type.
 * 
 * <p/>Note: For a given event, only the fields which are appropriate will be
 * filled in. The appropriate fields for each event are documented by the event
 * source.
 * 
 * @see Component
 */
public class ComponentEvent extends DomEvent {

  /**
   * The component that issued the event.
   */
  public Component component;

  /**
   * The component state.
   */
  public Map<String, Object> state;

  /**
   * Creates a new base event.
   * 
   * @param component the source component
   */
  public ComponentEvent(Component component) {
    this.component = component;
  }

  /**
   * Creates a new base event.
   * 
   * @param component the source component
   */
  public ComponentEvent(Component component, Event event) {
    super(event);
    this.component = component;
  }

}
