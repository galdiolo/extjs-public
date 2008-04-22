/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.event;

import com.extjs.gxt.ui.client.widget.DataList;
import com.extjs.gxt.ui.client.widget.DataListItem;
import com.extjs.gxt.ui.client.widget.menu.Menu;

/**
 * DataList event type.
 * 
 * <p/>Note: For a given event, only the fields which are appropriate will be
 * filled in. The appropriate fields for each event are documented by the event
 * source.
 * 
 * @see DataList
 */
public class DataListEvent extends ComponentEvent {

  public DataList dataList;

  public DataListItem item;

  public int index;

  public Menu menu;

  public DataListEvent(DataList list) {
    super(list);
    this.dataList = list;
  }

}
