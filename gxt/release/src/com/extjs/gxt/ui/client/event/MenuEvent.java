/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.event;

import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.Item;

/**
 * Menu event type.
 * 
 * <p/>Note: For a given event, only the fields which are appropriate will be
 * filled in. The appropriate fields for each event are documented by the event
 * source.
 * 
 * @see Menu
 */
public class MenuEvent extends ContainerEvent<Menu, Item> {

  public MenuEvent(Menu menu) {
    super(menu);
  }

  public MenuEvent(Menu container, Item component) {
    super(container, component);
  }

}
