/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.mvc;

import com.extjs.gxt.ui.client.event.BaseEvent;

/**
 * <code>AppEvents</code> are used to pass messages between
 * <code>Controllers</code> and <code>Views</code>. All events have a
 * specific type which are used to identify the event. Typically, applications
 * will define all application events in a constants class.
 */
public class AppEvent extends BaseEvent {

  /**
   * Application specific data such as the model.
   */
  public Object data;

  /**
   * The optional history token (defaults to null). If null, a token will be
   * generated for the event.
   */
  public String token;

  /**
   * True to create a history item for this event when passed to through the
   * dispatcher.
   */
  public boolean historyEvent;

  /**
   * Creates a new event with the given type.
   * 
   * @param type the event type
   */
  public AppEvent(int type) {
    this.type = type;
  }

  /**
   * Creates a new event with the given type and data.
   * 
   * @param type the event type
   * @param data the data
   */
  public AppEvent(int type, Object data) {
    this.type = type;
    this.data = data;
  }

  public String toString() {
    return "Event Type: " + type;
  }
}
