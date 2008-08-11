/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.event;

import com.extjs.gxt.ui.client.Events;

/**
 * Event interface for widget events.
 */
public class WidgetListener implements Listener<ComponentEvent> {

  public void handleEvent(ComponentEvent ce) {
    switch (ce.type) {
      case Events.Resize:
        widgetResized(ce);
        break;
      case Events.Attach:
        widgetAttached(ce);
        break;
      case Events.Detach:
        widgetDetached(ce);
        break;
    }
  }

  /**
   * Fires after a widget is moved or resized.
   * 
   * @param ce an event containing information about the event
   */
  public void widgetResized(ComponentEvent ce) {

  }

  /**
   * Fires after a widget is attached.
   * 
   * @param ce an event containing information about the event
   */
  public void widgetAttached(ComponentEvent ce) {

  }

  /**
   * Fires after a widget is detached.
   * 
   * @param ce an event containing information about the event
   */
  public void widgetDetached(ComponentEvent ce) {

  }

}
