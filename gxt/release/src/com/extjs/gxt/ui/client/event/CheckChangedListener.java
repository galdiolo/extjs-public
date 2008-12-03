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
 * Check changed listener.
 */
public class CheckChangedListener implements Listener<CheckChangedEvent> {

  public void handleEvent(CheckChangedEvent ce) {
    switch (ce.type) {
      case Events.CheckChange:
        checkChanged(ce);
        break;
    }
  }

  /**
   * Fires when the check state has changed.
   * 
   * @param event the check changed event
   */
  public void checkChanged(CheckChangedEvent event) {

  }

}
