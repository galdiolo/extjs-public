/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.event;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.widget.ScrollContainer;

/**
 * Event interface for scroll events.
 * 
 * @see ScrollContainer
 */
public class ScrollListener implements Listener<ComponentEvent> {

  public void handleEvent(ComponentEvent ce) {
    if (ce.type == Events.Scroll) {
      widgetScrolled(ce);
    }
  }

  /**
   * Fires when a component is scrolled.
   * 
   * @param ce the component event
   */
  public void widgetScrolled(ComponentEvent ce) {
    
  }

}
