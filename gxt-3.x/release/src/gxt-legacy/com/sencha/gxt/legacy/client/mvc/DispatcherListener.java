/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.sencha.gxt.legacy.client.mvc;

import com.sencha.gxt.core.shared.event.CancellableEvent;
import com.sencha.gxt.legacy.client.mvc.AfterAppEvent.AfterAppEventHandler;
import com.sencha.gxt.legacy.client.mvc.BeforeAppEvent.BeforeAppEventHandler;

/**
 * Sample abstract Event handler for before/after events. If only one event is
 * to be handled, implement only that interface. This class is retained for ease
 * of migration to GXT 3.
 * 
 * @see BeforeAppEventHandler
 * @see AfterAppEventHandler
 */
public abstract class DispatcherListener implements BeforeAppEventHandler, AfterAppEventHandler {

  /**
   * Fires after an event has been dispatched.
   * 
   * @param event an event containing the event that was dispatched
   */
  public void afterDispatch(AfterAppEvent event) {

  }

  /**
   * Fires before an event is dispatched. Listeners can cancel the action by
   * calling {@link CancellableEvent#setCancelled(boolean)}.
   * 
   * @param event an event containing the application event to be dispatched
   */
  @Override
  public void beforeDispatch(BeforeAppEvent event) {

  }

}
