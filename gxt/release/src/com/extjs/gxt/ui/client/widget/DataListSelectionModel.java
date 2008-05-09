/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget;

import com.extjs.gxt.ui.client.event.DataListEvent;
import com.extjs.gxt.ui.client.widget.selection.AbstractListSelectionModel;

public class DataListSelectionModel extends
    AbstractListSelectionModel<DataListItem, DataList, DataListEvent> {

  protected DataList dataList;

  public DataListSelectionModel(DataList dataList) {
    this.dataList = dataList;
  }

  @Override
  protected DataListEvent createEvent(DataList list, DataListItem item) {
    return new DataListEvent(list, item);
  }

  @Override
  public void onSelectChange(DataListItem item, boolean select) {
    dataList.onSelectChange(item, select);
  }

}
