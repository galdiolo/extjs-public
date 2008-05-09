/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.event;

import java.util.EventListener;

public interface WindowListener extends EventListener {

  public void windowActivate(WindowEvent we);
  
  public void windowDeactivate(WindowEvent we);
  
  public void windowMinimize(WindowEvent we);
  
  public void windowMaximize(WindowEvent we);
  
  public void windowRestore(WindowEvent we);
  
}
