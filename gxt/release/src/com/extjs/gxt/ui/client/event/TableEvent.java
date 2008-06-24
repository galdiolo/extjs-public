/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.event;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.widget.table.Table;
import com.extjs.gxt.ui.client.widget.table.TableItem;

/**
 * Table event type.
 * 
 * @see Table
 */
public class TableEvent extends ContainerEvent<Table, TableItem> {

  /**
   * The width.
   */
  public float width;

  /**
   * The column index.
   */
  public int columnIndex;

  /**
   * The row index.
   */
  public int rowIndex;

  /**
   * The cell index.
   */
  public int cellIndex;

  /**
   * The sort direction.
   */
  public SortDir sortDir = SortDir.NONE;

  /**
   * Creates a new table event.
   * 
   * @param table the event source
   */
  public TableEvent(Table table) {
    super(table);
  }
  
  public TableEvent(Table table, TableItem item) {
    super(table, item);
  }

}
