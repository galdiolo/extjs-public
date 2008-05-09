/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.mvc;

import java.util.EventListener;

import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MvcEvent;

/**
 * Provides a mapping between the typed and untyped listener mechanisms that for
 * <code>Dispatchers</code>.
 */
class DispatcherTypedListener implements Listener<MvcEvent> {

  protected EventListener eventListener;

  /**
   * Creates a new typed listener.
   * 
   * @param listener the typed listener
   */
  public DispatcherTypedListener(EventListener listener) {
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

  public void handleEvent(MvcEvent mvce) {
    switch (mvce.type) {
      case Dispatcher.BeforeDispatch:
        ((DispatcherListener) eventListener).beforeDispatch(mvce);
        break;
      case Dispatcher.AfterDispatch:
        ((DispatcherListener) eventListener).afterDispatch(mvce);
        break;
    }
  }
}
