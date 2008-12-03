/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.event;

import com.extjs.gxt.ui.client.widget.grid.ColumnModel;

/**
 * ColumnModel event.
 * 
 * @see ColumnModel
 */
public class ColumnModelEvent extends BaseEvent {

  /**
   * The source column model.
   */
  public ColumnModel cm;

  /**
   * The column index.
   */
  public int colIndex;

  /**
   * The column header.
   */
  public String header;

  /**
   * The column width.
   */
  public int width;

  /**
   * The column hidden state.
   */
  public boolean hidden;

  public ColumnModelEvent(ColumnModel cm) {
    this.cm = cm;
  }

  public ColumnModelEvent(ColumnModel cm, int colIndex) {
    this.colIndex = colIndex;
    this.cm = cm;
  }

}
