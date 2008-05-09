/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.table;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.TableEvent;

/**
 * Row selection model for tables.
 */
public class RowSelectionModel extends TableSelectionModel {

  public RowSelectionModel() {
    super();
  }

  public RowSelectionModel(SelectionMode selectionMode) {
    super(selectionMode);
  }

  @Override
  protected TableEvent createEvent(Table list, TableItem item) {
    return new TableEvent(list, item);
  }

  @Override
  public void onSelectChange(TableItem item, boolean select) {
    item.onSelectChange(select);
  }

}
