/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.grid;

import java.util.List;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.KeyboardListener;

/**
 * Cell based selection model for a grid.
 * 
 * @param <M> the model type
 */
public class CellSelectionModel<M extends ModelData> extends GridSelectionModel<M> {

  private Callback callback = new Callback(this);
  private EditorGrid editGrid;

  @Override
  public void bindGrid(Grid grid) {
    if (this.grid != null) {
      this.grid.removeListener(Events.CellMouseDown, this);
      this.grid.getView().removeListener(Events.Refresh, this);
      keyNav.bind(null);
      bind(null);
    }
    this.grid = grid;
    if (grid != null) {
      grid.setTrackMouseOver(false);
      grid.addListener(Events.CellMouseDown, this);
      grid.getView().addListener(Events.Refresh, this);
      editGrid = grid instanceof EditorGrid ? ((EditorGrid) grid) : null;
      keyNav.bind(grid);
      bind(grid.getStore());
    }
    bind(grid != null ? grid.getStore() : null);
  }

  @Override
  public void deselectAll() {
    if (selection != null) {
      grid.getView().onCellDeselect(selection.row, selection.cell);
      selection = null;
    }
  }

  /**
   * Returns the selected cell.
   * 
   * @return the selection cell
   */
  public CellSelection getSelectCell() {
    return selection;
  }

  @Override
  public void handleEvent(BaseEvent e) {
    switch (e.type) {
      case Events.CellMouseDown:
        handleMouseDown((GridEvent) e);
        break;
      case Events.Refresh:
        refresh();
        break;
    }
  }

  /**
   * Selects the cell.
   * 
   * @param row the row index
   * @param cell the cell index
   */
  @Override
  public void selectCell(int row, int cell) {
    deselectAll();
    M m = store.getAt(row);
    selection = new CellSelection(m, row, cell);
    grid.getView().onCellSelect(row, cell);
    grid.getView().focusCell(row, cell, true);
  }

  @Override
  protected void handleMouseDown(GridEvent e) {
    if (e.event.getButton() != Event.BUTTON_LEFT || isLocked()) {
      return;
    }
    selectCell(e.rowIndex, e.colIndex);
  }

  @Override
  protected void onAdd(List<M> models) {
    deselectAll();
  }

  @Override
  protected void onClear(StoreEvent<M> se) {
    super.onClear(se);
    selection = null;
  }

  @Override
  protected void onKeyPress(GridEvent e) {
    if (editGrid != null) {
      // ignore events whose source is an input element
      String tag = e.getTarget().getTagName();
      if (tag.equals("INPUT") && !e.getTarget().getClassName().equals("_focus")) {
        return;
      }
    }
    if (selection == null) {
      e.stopEvent();
      Cell cell = grid.walkCells(0, 0, 1, callback, false);
      if (cell != null) {
        selectCell(cell.row, cell.cell);
      }
      return;
    }

    int r = selection.row;
    int c = selection.cell;

    Cell newCell = null;

    switch (e.getKeyCode()) {
      case KeyboardListener.KEY_TAB:
        if (e.isShiftKey()) {
          newCell = grid.walkCells(r, c - 1, -1, callback, false);
        } else {
          newCell = grid.walkCells(r, c + 1, 1, callback, false);
        }
        break;
      case KeyboardListener.KEY_DOWN: {
        newCell = grid.walkCells(r + 1, c, 1, callback, false);
        break;
      }
      case KeyboardListener.KEY_UP: {
        newCell = grid.walkCells(r - 1, c, -1, callback, false);
        break;
      }
      case KeyboardListener.KEY_LEFT:
        newCell = grid.walkCells(r, c - 1, -1, callback, false);
        break;
      case KeyboardListener.KEY_RIGHT:
        newCell = grid.walkCells(r, c + 1, 1, callback, false);
        break;
      case KeyboardListener.KEY_ENTER:
        if (editGrid != null) {
          if (!editGrid.isEditing()) {
            editGrid.startEditing(r, c);
            e.stopEvent();
            return;
          }
        }
        break;

    }
    if (newCell != null) {
      selectCell(newCell.row, newCell.cell);
      e.stopEvent();
    }
  }

  @Override
  protected void onRemove(M model) {
    super.onRemove(model);
    if (selection != null && selection.model == model) {
      selection = null;
    }
  }

}
