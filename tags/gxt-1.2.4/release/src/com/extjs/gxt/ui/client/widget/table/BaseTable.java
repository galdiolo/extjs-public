/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.table;

import com.extjs.gxt.ui.client.Style.SortDir;

/**
 * Defines common methods for Table based components.
 */
public interface BaseTable {

  /**
   * Returns the table's column model.
   * 
   * @return the column model
   */
  TableColumnModel getColumnModel();

  /**
   * Returns the column at the specified index.
   * 
   * @param index the column index
   * @return the column
   */
  TableColumn getColumn(int index);

  /**
   * Returns the table's header.
   * 
   * @return the table header
   */
  TableHeader getTableHeader();

  /**
   * Sorts the table using the specified column index.
   * 
   * @param index the column index
   * @param direction the direction to sort (NONE, ASC, DESC)
   */
  void sort(int index, SortDir direction);

  /**
   * Returns the column context menu enabled state.
   * 
   * @return <code>true</code> if enabled, <code>false</code> otherwise.
   */
  boolean getColumnContextMenu();

}
