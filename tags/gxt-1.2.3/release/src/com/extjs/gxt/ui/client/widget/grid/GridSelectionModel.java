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
import com.extjs.gxt.ui.client.event.DomEvent;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.util.KeyNav;
import com.extjs.gxt.ui.client.widget.selection.AbstractStoreSelectionModel;
import com.google.gwt.user.client.ui.KeyboardListener;

/**
 * Grid selection model.
 * 
 * <dl>
 * <dt>Inherited Events:</dt>
 * <dd>AbstractStoreSelectionModel BeforeSelect</dd>
 * <dd>AbstractStoreSelectionModel SelectionChange</dd>
 * </dl>
 */
public class GridSelectionModel<M extends ModelData> extends
    AbstractStoreSelectionModel<M> implements Listener<BaseEvent> {

  public class CellSelection {
    public M model;
    public int row;
    public int cell;

    public CellSelection(M model, int row, int cell) {
      this.model = model;
      this.row = row;
      this.cell = cell;
    }
  }

  class Callback {

    private GridSelectionModel sm;

    public Callback(GridSelectionModel sm) {
      this.sm = sm;
    }

    public boolean isSelectable(int row, int cell, boolean acceptsNav) {
      return sm.isSelectable(row, cell, acceptsNav);
    }
  }
  protected Grid grid;
  protected CellSelection selection;
  protected boolean editmode;

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

  private Callback callback = new Callback(this);

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

  protected void doFocus(int row, int cell) {
    GridView view = grid.getView();
    view.focusRow(row);
  }

  protected void handleMouseDown(GridEvent e) {
    if (isLocked() || e.isRightClick() || editmode) {
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
        view.focusCell(e.rowIndex, e.colIndex, true);
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

  protected boolean hasNext() {
    return lastSelected != null && store.indexOf(lastSelected) < (store.getCount() - 1);
  }

  protected boolean hasPrevious() {
    return lastSelected != null && store.indexOf(lastSelected) > 0;
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

  protected void onEditorKey(DomEvent e) {
    int r = selection.row;
    int c = selection.cell;

    EditorGrid editGrid = (EditorGrid) grid;

    Cell newCell = null;

    switch (e.getKeyCode()) {
      case KeyboardListener.KEY_TAB:
        if (e.isShiftKey()) {
          newCell = grid.walkCells(r, c - 1, -1, callback, true);
        } else {
          newCell = grid.walkCells(r, c + 1, 1, callback, true);
        }
        e.stopEvent();
        break;
      case KeyboardListener.KEY_ESCAPE:
        e.stopEvent();
        editGrid.stopEditing();
        break;
    }

    if (newCell != null) {
      final int rr = newCell.row;
      final int cc = newCell.cell;
      selectCell(newCell.row, newCell.cell);
      ((EditorGrid) grid).startEditing(rr, cc);
    }
  }

  protected void onKeyDown(GridEvent e) {
    if (!e.isShiftKey()) {
      selectNext(false);
    }
    e.preventDefault();
  }

  protected void onKeyPress(GridEvent e) {

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
      grid.getView().onRowSelect(idx);
    } else {
      grid.getView().onRowDeselect(idx);
    }
  }

  protected void selectCell(int row, int cell) {
    select(row);
    M m = store.getAt(row);
    selection = new CellSelection(m, row, cell);
    grid.getView().focusCell(row, cell, true);
  }

  boolean isSelectable(int row, int cell, boolean acceptsNav) {
    if (acceptsNav) {
      return !grid.getColumnModel().isHidden(cell)
          && grid.getColumnModel().isCellEditble(cell);
    } else {
      return !grid.getColumnModel().isHidden(cell);
    }
  }
}

class Cell {
  public int row;
  public int cell;

  public Cell(int row, int cell) {
    this.row = row;
    this.cell = cell;
  }

}