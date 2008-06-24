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
 * Event interface for windows.
 */
public class WindowListener implements Listener<WindowEvent> {

  public void handleEvent(WindowEvent we) {
    switch (we.type) {
      case Events.Activate:
        windowActivate(we);
        break;
      case Events.Deactivate:
        windowDeactivate(we);
        break;
      case Events.Hide:
        windowHide(we);
        break;
      case Events.Maximize:
        windowMaximize(we);
        break;
      case Events.Minimize:
        windowMinimize(we);
        break;
      case Events.Restore:
        windowRestore(we);
        break;
    }
  }

  /**
   * Fires after the window is activated.
   * 
   * @param we the window event
   */
  public void windowActivate(WindowEvent we) {

  }

  /**
   * Fires after the window is hidden.
   * 
   * @param we the window event
   */
  public void windowHide(WindowEvent we) {

  }

  /**
   * Fires after the window is deactivate.
   * 
   * @param we the window event
   */
  public void windowDeactivate(WindowEvent we) {

  }

  /**
   * Fires after the window is maximized.
   * 
   * @param we the window event
   */
  public void windowMaximize(WindowEvent we) {

  }

  /**
   * Fires after the window is minmized.
   * 
   * @param we the window event
   */
  public void windowMinimize(WindowEvent we) {

  }

  /**
   * Fires after the window is restored.
   * 
   * @param we the window event
   */
  public void windowRestore(WindowEvent we) {

  }

}
