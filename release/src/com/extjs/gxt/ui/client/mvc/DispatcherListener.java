/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.mvc;

import java.util.EventListener;

import com.extjs.gxt.ui.client.event.MvcEvent;

/**
 * Event interface for dispatcher events.
 */
public interface DispatcherListener extends EventListener {

  /**
   * Fires before an event is dispatched. Listeners can set the
   * <code>doit</code> field to <code>false</code> to cancel the action.
   * 
   * @param mvce the app event to be dispatched
   */
  public void beforeDispatch(MvcEvent mvce);

  /**
   * Fires after an event has been dispatched.
   * 
   * @param mvce the event that was dispatched
   */
  public void afterDispatch(MvcEvent mvce);

}
