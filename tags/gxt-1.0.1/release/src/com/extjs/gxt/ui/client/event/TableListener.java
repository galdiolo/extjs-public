/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.event;

import com.extjs.gxt.ui.client.Events;

/**
 * Event listener for tabe events.
 */
public class TableListener implements Listener<TableEvent> {

  public void handleEvent(TableEvent te) {
    switch (te.type) {
      case Events.CellClick:
        tableCellClick(te);
        break;
      case Events.CellDoubleClick:
        tableCellDoubleClick(te);
        break;
      case Events.ColumnClick:
        tableColumnClick(te);
        break;
      case Events.RowClick:
        tableRowClick(te);
        break;
      case Events.RowDoubleClick:
        tableRowDoubleClick(te);
        break;
      case Events.SortChange:
        tableSortChange(te);
        break;
    }
  }

  /**
   * Fired after a cell is clicked
   * 
   * @param te the table event
   */
  public void tableCellClick(TableEvent te) {

  }

  /**
   * Fires after a cell is double clicked.
   * 
   * @param te the table event
   */
  public void tableCellDoubleClick(TableEvent te) {

  }

  /**
   * Fired after a column is clicked.
   * 
   * @param te the table event
   */
  public void tableColumnClick(TableEvent te) {

  }

  /**
   * Fired after a row is clicked
   * 
   * @param te the table event
   */
  public void tableRowClick(TableEvent te) {

  }

  /**
   * Fires after a row is double clicked.
   * 
   * @param te the table event
   */
  public void tableRowDoubleClick(TableEvent te) {

  }

  /**
   * Fires before the table is sorted.
   * 
   * @param te the table event
   */
  public void tableSortChange(TableEvent te) {

  }

}
