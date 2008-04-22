/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.event;

import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.ToolItem;

/**
 * ToolBar event type.
 * 
 * @see ToolBar
 */
public class ToolBarEvent extends ComponentEvent {

  public ToolBar toolBar;

  public ToolItem item;

  public int index;

  public ToolBarEvent(ToolBar toolBar) {
    super(toolBar);
    this.toolBar = toolBar;
  }

}
