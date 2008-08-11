/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.widget.selection.AbstractSelectionModel;

/**
 * Single-select selection model for DataList.
 */
public class DataListSelectionModel extends AbstractSelectionModel<DataList, DataListItem> {

  public DataListSelectionModel(SelectionMode mode) {
    super(mode);
  }

}
