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
import com.extjs.gxt.ui.client.widget.selection.AbstractListSelectionModel;

/**
 * Abstract selection model for tables.
 */
public abstract class TableSelectionModel extends
    AbstractListSelectionModel<TableItem, Table, TableEvent> {

  public TableSelectionModel() {
    super();
  }

  public TableSelectionModel(SelectionMode selectionMode) {
    super(selectionMode);
  }

}
