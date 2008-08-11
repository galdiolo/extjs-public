/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.event;

import com.extjs.gxt.ui.client.state.StateManager;

/**
 * StateManager event type.
 * 
 * @see StateManager
 */
public class StateEvent extends BaseEvent {

  /**
   * The state provider.
   */
  public StateManager manager;

  /**
   * The property value.
   */
  public Object value;

  /**
   * The property name.
   */
  public String name;

  /**
   * Creates a new state event.
   * 
   * @param manager the source manager
   */
  public StateEvent(StateManager manager) {
    this.manager = manager;
  }

  /**
   * Creates a new state event.
   * 
   * @param manager the source manager
   * @param name the property name
   * @param value the property value
   */
  public StateEvent(StateManager manager, String name, Object value) {
    this.manager = manager;
    this.name = name;
    this.value = value;
  }

}
