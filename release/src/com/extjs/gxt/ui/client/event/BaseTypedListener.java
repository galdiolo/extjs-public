/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.event;

import java.util.EventListener;

public abstract class BaseTypedListener implements Listener {

  protected EventListener eventListener;

  /**
   * Creates a new typed listener.
   * 
   * @param listener the typed listener
   */
  public BaseTypedListener(EventListener listener) {
    eventListener = listener;
  }

  /**
   * Returns the event listener.
   * 
   * @return the event listener
   */
  public EventListener getEventListener() {
    return eventListener;
  }

  public abstract void handleEvent(BaseEvent be);
}
