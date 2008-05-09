/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.util;

import java.util.EventListener;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Listener;

/**
 * Abstract base class for objects that register listeners and fire events.
 * 
 * @see Listener
 * @see BaseEvent
 */
public class Observable {

  public EventTable eventTable;
  
  protected boolean hasListener;

  /**
   * Adds a listener bound by the given event type.
   * 
   * @param eventType the eventType
   * @param listener the listener to be added
   */
  public void addListener(int eventType, Listener listener) {
    if (listener == null) return;
    if (eventTable == null) {
      eventTable = new EventTable();
    }
    eventTable.hook(eventType, listener);
    hasListener = true;
  }

  /**
   * Removes a listener.
   * 
   * @param eventType the event type
   * @param listener the listener to be removed
   */
  public void removeListener(int eventType, Listener listener) {
    if (eventTable != null) {
      eventTable.unhook(eventType, listener);
    }
    hasListener = eventTable != null && eventTable.size() > 0;

  }
  
  public void removeListener(int eventType, EventListener listener) {
    if (eventTable != null) {
      eventTable.unhook(eventType, listener);
    }
    hasListener = eventTable != null && eventTable.size() > 0;
  }

  /**
   * Removes all listeners.
   */
  public void removeAllListeners() {
    if (eventTable != null) {
      eventTable.removeAllListeners();
    }
    hasListener = false;
  }

  /**
   * Fires an event.
   * 
   * @param eventType the event type
   * @return <code>true</code> if any listeners cancel the event.
   */
  public boolean fireEvent(int eventType) {
    return fireEvent(eventType, new BaseEvent());
  }

  /**
   * Fires an event.
   * 
   * @param eventType eventType the event type
   * @param be the base event
   * @return <code>true</code> if any listeners cancel the event.
   */
  public boolean fireEvent(int eventType, BaseEvent be) {
    be.type = eventType;
    be.source = this;
    if (eventTable != null) {
      return eventTable.sendEvent(be);
    }
    return true;
  }

}
