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
 * Key listener.
 */
public class KeyListener implements Listener<ComponentEvent> {

  /**
   * Fires on key down.
   * 
   * @param event the component event
   */
  public void componentKeyDown(ComponentEvent event) {
    
  }
  
  /**
   * Fires on key press.
   * 
   * @param event the component event
   */
  public void componentKeyPress(ComponentEvent event) {
    
  }

  /**
   * Fires on key up.
   * 
   * @param event the component event
   */
  public void componentKeyUp(ComponentEvent event) {
    
  }

  public void handleEvent(ComponentEvent ce) {
    switch (ce.type) {
      case Events.KeyPress:
        componentKeyPress(ce);
        break;
      case Events.KeyUp:
        componentKeyUp(ce);
        break;
      case Events.KeyDown:
        componentKeyDown(ce);
        break;
    }
  }

}
