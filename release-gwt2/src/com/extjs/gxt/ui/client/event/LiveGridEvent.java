/*
 * Ext GWT 2.2.0 - Ext for GWT
 * Copyright(c) 2007-2010, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.event;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.google.gwt.user.client.Event;

public class LiveGridEvent<M extends ModelData> extends GridEvent<M> {
  private int viewIndex;

  private int pageSize;

  private int totalCount;

  public LiveGridEvent(Grid<M> grid) {
    super(grid);
  }

  public LiveGridEvent(Grid<M> grid, Event event) {
    super(grid, event);
  }

  public int getPageSize() {
    return pageSize;
  }

  public int getTotalCount() {
    return totalCount;
  }

  public int getViewIndex() {
    return viewIndex;
  }

  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }
  public void setTotalCount(int totalCount) {
    this.totalCount = totalCount;
  }
  public void setViewIndex(int viewIndex) {
    this.viewIndex = viewIndex;
  }
}
