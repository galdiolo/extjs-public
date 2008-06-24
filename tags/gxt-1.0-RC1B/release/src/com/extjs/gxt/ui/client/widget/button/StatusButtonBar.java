/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.button;

import com.extjs.gxt.ui.client.widget.StatusBar;

/**
 * A <code>ButtonBar</code> with a <code>StatusBar</code> that aligns its
 * button to the right.
 */
public class StatusButtonBar extends ButtonBar {

  protected StatusBar statusBar;

  public StatusButtonBar() {
    statusBar = new StatusBar();
    add(new ButtonAdapter(statusBar));
    add(new FillButton());
  }

  /**
   * Returns the status bar.
   * 
   * @return the status bar.
   */
  public StatusBar getStatusBar() {
    return statusBar;
  }

}
