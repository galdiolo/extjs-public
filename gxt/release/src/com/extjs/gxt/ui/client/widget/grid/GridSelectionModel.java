/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.grid;

import java.util.Arrays;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.util.KeyNav;
import com.extjs.gxt.ui.client.widget.selection.AbstractStoreSelectionModel;

/**
 * Grid selection model.
 * 
 * <dl>
 * <dt>Inherited Events:</dt>
 * <dd>AbstractStoreSelectionModel BeforeSelect</dd>
 * <dd>AbstractStoreSelectionModel SelectionChange</dd>
 * </dl>
 */
public class GridSelectionModel<M extends ModelData> extends AbstractStoreSelectionModel<M>
    implements Listener<BaseEvent> {

  protected Grid grid;
  protected KeyNav<GridEvent> keyNav = new KeyNav<GridEvent>() {

    @Override
    public void onDown(GridEvent e) {
      onKeyDown(e);
    }

    @Override
    public void onKeyPress(GridEvent ce) {
      GridSelectionModel.this.onKeyPress(ce);
    }

    @Override
    public void onUp(GridEvent e) {
      onKeyUp(e);
    }

  };

  public void bindGrid(Grid grid) {
    if (this.grid != null) {
      this.grid.removeListener(Events.RowMouseDown, this);
      this.grid.removeListener(Events.ContextMenu, this);
      this.grid.getView().removeListener(Events.RowUpdated, this);
      this.grid.getView().removeListener(Events.Refresh, this);
      keyNav.bind(null);
      bind(null);
    }
    this.grid = grid;
    if (grid != null) {
      grid.addListener(Events.RowMouseDown, this);
      grid.addListener(Events.ContextMenu, this);
      grid.getView().addListener(Events.RowUpdated, this);
      grid.getView().addListener(Events.Refresh, this);
      keyNav.bind(grid);
      bind(grid.getStore());
    }
    bind(grid != null ? grid.getStore() : null);
  }

  public void handleEvent(BaseEvent e) {
    switch (e.type) {
      case Events.RowMouseDown:
        handleMouseDown((GridEvent) e);
        break;
      case Events.RowUpdated:
        onRowUpdated((GridEvent) e);
        break;
      case Events.Refresh:
        refresh();
        break;
      case Events.ContextMenu:
        onContextMenu((GridEvent) e);
        break;
    }
  }

  public boolean isLocked() {
    return false;
  }

  /**
   * Selects the next row.
   * 
   * @param keepexisting true to keep existing selections
   */
  public void selectNext(boolean keepexisting) {
    if (hasNext()) {
      int idx = store.indexOf(lastSelected) + 1;
      select(idx);
      grid.getView().focusRow(idx);
    }
  }

  /**
   * Selectes the previous row.
   * 
   * @param keepexisting true to keep existing selections
   */
  public void selectPrevious(boolean keepexisting) {
    if (hasPrevious()) {
      int idx = store.indexOf(lastSelected) - 1;
      select(idx);
      grid.getView().focusRow(idx);
    }
  }

  protected void handleMouseDown(GridEvent e) {
    if (isLocked() || e.isRightClick()) {
      return;
    }
    GridView view = grid.getView();
    M sel = store.getAt(e.rowIndex);

    if (isSelected(sel) && !e.isControlKey()) {
      return;
    }

    if (selectionMode == SelectionMode.SINGLE) {
      if (isSelected(sel) && e.isControlKey()) {
        deselect(sel);
      } else if (!isSelected(sel)) {
        select(sel);
      }
    } else {
      if (e.isShiftKey() && lastSelected != null) {
        int last = store.indexOf(lastSelected);
        int index = e.rowIndex;
        int a = (last > index) ? index : last;
        int b = (last < index) ? index : last;
        select(a, b);
        lastSelected = store.getAt(last);
        view.focusCell(index, e.colIndex, true);
      } else if (isSelected(sel) && e.isControlKey()) {
        doDeselect(Arrays.asList(sel), false);
      } else {
        doSelect(Arrays.asList(sel), e.isControlKey(), false);
        view.focusCell(e.rowIndex, e.colIndex, true);
      }
    }
  }

  protected void doFocus(int row, int cell) {
    GridView view = grid.getView();
    view.focusRow(row);
  }

  protected boolean hasNext() {
    return lastSelected != null && store.indexOf(lastSelected) < (store.getCount() - 1);
  }

  protected boolean hasPrevious() {
    return lastSelected != null && store.indexOf(lastSelected) > 0;
  }

  protected void onKeyDown(GridEvent e) {
    if (!e.isShiftKey()) {
      selectNext(false);
    }
    e.preventDefault();
  }

  protected void onKeyPress(GridEvent e) {

  }

  protected void onContextMenu(GridEvent e) {
    if (locked) return;
    if (e.rowIndex != -1) {
      if (isSelected(store.getAt(e.rowIndex)) && selectionMode != SelectionMode.SINGLE) {
        return;
      }
      select(e.rowIndex);
    }
  }

  protected void onKeyUp(GridEvent e) {
    if (!e.isShiftKey()) {
      selectPrevious(false);
    }
    e.preventDefault();
  }

  protected void onRowUpdated(GridEvent ge) {
    if (isSelected((M) ge.model)) {
      grid.getView().onRowSelect(ge.rowIndex);
    }
  }

  @Override
  protected void onSelectChange(M model, boolean select) {
    int idx = store.indexOf(model);
    if (idx == -1) {
      return;
    }
    if (select) {
      grid.getView().onRowSelect(store.indexOf(model));
    } else {
      grid.getView().onRowDeselect(store.indexOf(model));
    }
  }

}