/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.mvc;

import java.util.HashMap;

import com.extjs.gxt.ui.client.event.BaseEvent;

/**
 * <code>AppEvents</code> are used to pass messages between
 * <code>Controllers</code> and <code>Views</code>. All events have a specific
 * type which are used to identify the event. Typically, applications will
 * define all application events in a constants class.
 */
public class AppEvent<Data> extends BaseEvent {

  /**
   * Application specific data such as the model.
   */
  public Data data;

  /**
   * The optional history token (defaults to null). If null, a token will be
   * generated for the event.
   */
  public String token;

  /**
   * True to create a history item for this event when passed through the
   * dispatcher.
   */
  public boolean historyEvent;

  private HashMap dataMap;

  /**
   * Creates a new app event.
   * 
   * @param type the event type
   */
  public AppEvent(int type) {
    this.type = type;
  }

  /**
   * Creates a new app event.
   * 
   * @param type the event type
   * @param data the data
   */
  public AppEvent(int type, Data data) {
    this.type = type;
    this.data = data;
  }

  /**
   * Creates a new app event.
   * 
   * @param type the event type
   * @param data the event data
   * @param token the history token
   */
  public AppEvent(int type, Data data, String token) {
    this(type, data);
    this.token = token;
    historyEvent = true;
  }

  /**
   * Returns the application defined property for the given name, or
   * <code>null</code> if it has not been set.
   * 
   * @param key the name of the property
   * @return the value or <code>null</code> if it has not been set
   */
  public <X> X getData(String key) {
    if (dataMap == null) return null;
    return (X) dataMap.get(key);
  }

  /**
   * Sets the application defined property with the given name.
   * 
   * @param key the name of the property
   * @param data the new value for the property
   */
  public void setData(String key, Object data) {
    if (dataMap == null) dataMap = new HashMap();
    dataMap.put(key, data);
  }

  public String toString() {
    return "Event Type: " + type;
  }
}
