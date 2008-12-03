/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.grid;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.ListStore;

/**
 * A <code>ColumnConfig</code> that provides an automatic row numbering column.
 * 
 * <p>
 * Note: The row numbers will not be updated when models are added and removed
 * from the store after the grid has been rendered. The row numbers can be
 * recalculated by calling refresh(false) on the GridView.
 * </p>
 */
public class RowNumberer extends ColumnConfig {

  public RowNumberer() {
    setHeader("");
    setWidth(23);
    setSortable(false);
    setResizable(false);
    setFixed(true);
    setMenuDisabled(true);
    setDataIndex("");
    setId("numberer");

    setRenderer(new GridCellRenderer() {
      public String render(ModelData model, String property, ColumnData d, int rowIndex,
          int colIndex, ListStore store) {
        return "" + (rowIndex + 1);
      }
    });
  }

}
