/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.mvc;

import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MvcEvent;

/**
 * Event listener for dispatcher events.
 */
public class DispatcherListener implements Listener<MvcEvent> {

  public void handleEvent(MvcEvent me) {
    switch (me.type) {
      case Dispatcher.BeforeDispatch:
        beforeDispatch(me);
        break;
      case Dispatcher.AfterDispatch:
        afterDispatch(me);
        break;
    }

  }

  /**
   * Fires before an event is dispatched. Listeners can set the
   * <code>doit</code> field to <code>false</code> to cancel the action.
   * 
   * @param mvce the app event to be dispatched
   */
  public void beforeDispatch(MvcEvent mvce) {

  }

  /**
   * Fires after an event has been dispatched.
   * 
   * @param mvce the event that was dispatched
   */
  public void afterDispatch(MvcEvent mvce) {

  }

}
