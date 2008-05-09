/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.event;

import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;

/**
 * Menu event type.
 * 
 * <p/>Note: For a given event, only the fields which are appropriate will be
 * filled in. The appropriate fields for each event are documented by the event
 * source.
 * 
 * @see Menu
 */
public class MenuEvent extends ComponentEvent {

  /**
   * The source menu.
   */
  public Menu menu;

  /**
   * The menu item.
   */
  public MenuItem item;

  /**
   * The insert index.
   */
  public int index;

  public MenuEvent(Menu menu) {
    super(menu);
    this.menu = menu;
  }

}
