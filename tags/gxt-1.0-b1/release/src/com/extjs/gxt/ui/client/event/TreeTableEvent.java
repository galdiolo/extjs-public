/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.event;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.widget.treetable.TreeTable;

/**
 * TreeTable event type.
 * 
 * <p/>Note: For a given event, only the fields which are appropriate will be
 * filled in. The appropriate fields for each event are documented by the event
 * source.
 * 
 * @see TreeTable
 */
public class TreeTableEvent extends TreeEvent {

  /**
   * The source tree table.
   */
  public TreeTable treeTable;

  /**
   * The index.
   */
  public int index;

  /**
   * The column index.
   */
  public int columnIndex;

  /**
   * The cell index.
   */
  public int cellIndex;

  /**
   * The row index.
   */
  public int rowIndex;

  /**
   * The sort direction.
   */
  public SortDir sortDir = SortDir.NONE;

  public TreeTableEvent(TreeTable treeTable) {
    super(treeTable);
    this.treeTable = treeTable;
  }
}
