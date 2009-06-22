/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.event;

/**
 * Abstract base class for objects that register listeners and fire events.
 * 
 * <pre>
    Observable observable = new BaseObservable();
    observable.addListener(Events.Select, new Listener&lt;BaseEvent>() {
      public void handleEvent(BaseEvent be) {

      }
    });
    observable.fireEvent(Events.Select, new BaseEvent()); 
 * </pre>
 * 
 * @see Listener
 * @see BaseEvent
 */
public interface Observable {

  /**
   * Adds a listener bound by the given event type.
   * 
   * @param eventType the eventType
   * @param listener the listener to be added
   */
  public void addListener(int eventType, Listener listener);

  /**
   * Fires an event.
   * 
   * @param eventType eventType the event type
   * @param be the base event
   * @return <code>true</code> if any listeners cancel the event.
   */
  public boolean fireEvent(int eventType, BaseEvent be);

  /**
   * Removes all listeners.
   */
  public void removeAllListeners();

  /**
   * Removes a listener.
   * 
   * @param eventType the event type
   * @param listener the listener to be removed
   */
  public void removeListener(int eventType, Listener listener);

}
