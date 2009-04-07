/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.event;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.SortInfo;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.google.gwt.user.client.Event;

/**
 * Grid event type.
 * 
 * @see Grid
 */
public class GridEvent extends BoxComponentEvent {

  /**
   * The column index.
   */
  public int colIndex = -1;

  /**
   * The row index.
   */
  public int rowIndex = -1;

  /**
   * The source grid.
   */
  public Grid grid;

  /**
   * The scroll left value.
   */
  public int scrollLeft;

  /**
   * The scroll top value.
   */
  public int scrollTop;

  /**
   * SortInfo value.
   */
  public SortInfo sortInfo;

  /**
   * The width.
   */
  public int width;

  /**
   * The model.
   */
  public ModelData model;

  /**
   * The property name.
   */
  public String property;

  /**
   * The context menu.
   */
  public Menu menu;

  /**
   * The record.
   */
  public Record record;

  /**
   * The value.
   */
  public Object value;

  /**
   * The start value.
   */
  public Object startValue;

  public GridEvent(Grid grid) {
    super(grid);
    this.grid = grid;
  }

  public GridEvent(Grid grid, Event event) {
    this(grid);
    this.event = event;
  }

}
