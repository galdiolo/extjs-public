/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.event;

import java.util.EventListener;

/**
 * Event interface for widget events.
 */
public interface WidgetListener extends EventListener {

  /**
   * Fires after a widget is moved or resized.
   * 
   * @param ce an event containing information about the event
   */
  public void widgetResized(ComponentEvent ce);

  /**
   * Fires after a widget is attached.
   * 
   * @param ce an event containing information about the event
   */
  public void widgetAttached(ComponentEvent ce);

  /**
   * Fires after a widget is detached.
   * 
   * @param ce an event containing information about the event
   */
  public void widgetDetached(ComponentEvent ce);

}
