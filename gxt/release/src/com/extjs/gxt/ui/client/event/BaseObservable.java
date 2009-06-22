/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseObservable implements Observable {

  private boolean firesEvents = true;
  private boolean hasListeners;
  private Map<Integer, List> listeners;
  private boolean activeEvent;

  /**
   * Adds a listener bound by the given event type.
   * 
   * @param eventType the eventType
   * @param listener the listener to be added
   */
  public void addListener(int eventType, Listener listener) {
    if (listener == null) return;
    if (listeners == null) {
      listeners = new HashMap<Integer, List>();
    }
    List<Listener> list = listeners.get(eventType);
    if (list == null) {
      list = new ArrayList<Listener>();
      listeners.put(eventType, list);
    }

    if (!list.contains(listener)) {
      list.add(listener);
    }
    hasListeners = true;
  }

  /**
   * Fires an event.
   * 
   * @param eventType the event type
   * @return <code>true</code> if any listeners cancel the event.
   */
  public boolean fireEvent(int eventType) {
    return fireEvent(eventType, new BaseEvent(this));
  }

  /**
   * Fires an event.
   * 
   * @param eventType eventType the event type
   * @param be the base event
   * @return <code>true</code> if any listeners cancel the event.
   */
  public boolean fireEvent(int eventType, BaseEvent be) {
    if (firesEvents && listeners != null) {
      activeEvent = true;
      be.type = eventType;
      be.source = be.source;

      List<Listener> list = listeners.get(eventType);
      if (list != null) {
        List<Listener> copy = new ArrayList<Listener>();
        copy.addAll(list);
        for (Listener l : copy) {
          l.handleEvent(be);
        }
      }
      activeEvent = false;
      return be.doit;
    }
    return true;
  }

  /**
   * Returns true if events are being fired.
   * 
   * @return the fire event state
   */
  public boolean getFiresEvents() {
    return firesEvents;
  }

  /**
   * Returns true if there is an active event.
   * 
   * @return the active event state
   */
  public boolean hasActiveEvent() {
    return activeEvent;
  }
  
  /**
   * Returns true if the observable has any listeners.
   * 
   * @return true for listeners
   */
  public boolean hasListeners() {
    return hasListeners;
  }

  /**
   * Returns true if the obsersable has listeners for the given event type.
   * 
   * @param eventType the event type
   * @return true for 1 or more listeners with the given event type
   */
  public boolean hasListeners(int eventType) {
    if (listeners != null && listeners.containsKey(eventType)) {
      List<Listener> list = listeners.get(eventType);
      if (list.size() != 0) {
        return true;
      }

    }
    return false;

  }

  /**
   * Removes all listeners.
   */
  public void removeAllListeners() {
    if (listeners != null) {
      listeners.clear();
    }
    hasListeners = false;
  }

  /**
   * Removes a listener.
   * 
   * @param eventType the event type
   * @param listener the listener to be removed
   */
  public void removeListener(final int eventType, final Listener listener) {
    if (listeners == null) {
      return;
    }
    List<Listener> list = listeners.get(eventType);
    if (list != null) {
      list.remove(listener);
      if (list.isEmpty()) {
        listeners.remove(eventType);
      }
    }
    hasListeners = listeners.size() > 0;
  }

  /**
   * Sets whether events should be fired (defaults to true).
   * 
   * @param firesEvents true to fire events, false to disable events
   */
  public void setFiresEvents(boolean firesEvents) {
    this.firesEvents = firesEvents;
  }
}
