/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.toolbar;

import com.extjs.gxt.ui.client.widget.Component;

/**
 * Base class for items contained in a tool bar.
 * 
 * @see ToolBar
 */
public class ToolItem extends Component {

  /**
   * The item's container tool bar.
   */
  protected ToolBar toolBar;

  /**
   * Creates a new toolitem.
   */
  public ToolItem() {

  }

  /**
   * Returns the item's parent tool bar.
   * 
   * @return the toolbar
   */
  public ToolBar getToolBar() {
    return toolBar;
  }

}
