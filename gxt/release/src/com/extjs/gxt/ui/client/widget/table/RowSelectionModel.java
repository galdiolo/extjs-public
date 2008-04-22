/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.table;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.TableEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.KeyboardListener;

/**
 * Row selection model.
 */
public class RowSelectionModel implements Listener<TableEvent> {

  protected SelectionMode selectionMode;
  protected Table table;
  protected List<TableItem> selectedItems;
  protected TableItem lastSelected;
  private TableItem scrollIntoViewItem;

  /**
   * Creates a new selection model.
   * 
   * @param selectionMode the style information
   */
  public RowSelectionModel(SelectionMode selectionMode) {
    this.selectionMode = selectionMode;
    selectedItems = new ArrayList<TableItem>();
  }

  /**
   * Deselects the item at the given index. If the item at the index was already
   * deselected, it remains deselected.
   * 
   * @param index the item to deselect
   */
  public void deselect(int index) {
    selectItems(index, index, false, true);
  }

  /**
   * Deselects the items at the given indices. If the item at the given index is
   * selected, it is deselected. If the item at the index was not selected, it
   * remains deselected.
   * 
   * @param begin the start index
   * @param end the end index
   */
  public void deselect(int begin, int end) {
    selectItems(begin, end, false, true);
  }

  /**
   * Deselects all selections.
   */
  public void deselectAll() {
    while (selectedItems.size() > 0) {
      TableItem item = selectedItems.remove(0);
      deselect(table.indexOf(item));

    }
    lastSelected = null;
  }

  public void handleEvent(TableEvent te) {
    switch (te.type) {
      case Events.CellClick:
        onCellClick(te);
        break;
      case Events.CellDoubleClick:
        onCellDoubleClick(te);
        break;
      case Events.KeyDown:
        onKeyPress(te);
        break;
      case Events.Remove:
        TableItem item = (TableItem) te.item;
        if (lastSelected == item) {
          lastSelected = null;
        }
        selectedItems.remove(item);
    }
  }

  /**
   * Returns <code>true</code> if the row is selected.
   * 
   * @param index the row index
   * @return the selected state
   */
  public boolean isSelected(int index) {
    TableItem item = table.getItem(index);
    return selectedItems.contains(item);
  }

  /**
   * Refreshes the selections.
   */
  public void refresh() {
    int rows = table.getItemCount();
    for (int i = 0; i < rows; i++) {
      TableItem item = table.getItem(i);
      item.setSelected(isSelected(i));
    }
  }

  /**
   * Selects the item at the given index. If the item at the index was already
   * selected, it remains selected.
   * 
   * @param index the row to select
   */
  public void select(int index) {
    selectItems(index, index, true, true);
  }

  /**
   * Selects the item in the range specified by the given indices. The current
   * selection is not cleared before the new rows are selected.
   * 
   * @param start the start of the range
   * @param end the end of the range
   */
  public void select(int start, int end) {
    selectItems(start, end, true, true);
  }

  /**
   * Selects all items.
   */
  public void selectAll() {
    selectItems(0, table.getItemCount() - 1, true, true);
  }

  protected void init(Table table) {
    this.table = table;
    table.addListener(Events.CellClick, this);
    table.addListener(Events.CellDoubleClick, this);
    table.addListener(Events.KeyDown, this);
    table.addListener(Events.Remove, this);
  }

  protected void unbind(Table table) {
    this.table = null;
    table.removeListener(Events.CellClick, this);
    table.removeListener(Events.CellDoubleClick, this);
    table.removeListener(Events.KeyDown, this);
    table.removeListener(Events.Remove, this);
  }

  protected void onCellClick(TableEvent te) {
    int row = te.rowIndex;
    boolean shiftkey = te.isShiftKey();
    boolean controlKey = te.isControlKey();

    boolean single = selectionMode == SelectionMode.SINGLE;
    if (single) {
      selectItems(row, row, !(isSelected(row) && controlKey), false);
      return;
    }

    if (te.isRightClick()) {
      TableItem item = table.findItem(te.getTarget());
      if (selectedItems.contains(item)) {
        return;
      }
    }

    if (shiftkey) {
      if (lastSelected != null) {
        selectItems(table.indexOf(lastSelected), row, true, false);
      } else {
        selectItems(0, row, true, false);
      }
    } else if (controlKey) {
      selectItems(row, row, !isSelected(row), true);
    } else {
      selectItems(row, row, true, false);
    }

    // safari needs focus put on table of click unless
    // the source is a widget - determined by sunk events
    if (GXT.isSafari) {
      Element elem = te.getTarget();
      if (DOM.getEventsSunk(elem) == 0) {
        table.focus();
      }

    }
  }

  protected void onCellDoubleClick(ComponentEvent ce) {

  }

  protected void onKeyPress(ComponentEvent ce) {
    int code = ce.getKeyCode();
    switch (code) {
      case KeyboardListener.KEY_UP: {
        int index = table.indexOf(lastSelected) - 1;
        selectItems(index, index, true, false);
        table.scrollIntoView(table.getItem(index));
        ce.stopEvent();
        break;
      }
      case KeyboardListener.KEY_DOWN: {
        int index = table.indexOf(lastSelected) + 1;
        selectItems(index, index, true, false);
        table.scrollIntoView(table.getItem(index));
        ce.stopEvent();
        break;
      }

    }
  }

  protected void remove(TableItem item) {
    if (lastSelected == item) {
      lastSelected = null;
    }
    if (selectedItems.remove(item)) {
      TableEvent be = new TableEvent(table);
      table.fireEvent(Events.SelectionChange, be);
    }
  }
  
  protected void selectItems(final int start, final int end, final boolean state,
      final boolean keepSelected) {
    if (!table.isRendered()) {
      table.addListener(Events.Render, new Listener<ComponentEvent>() {
        public void handleEvent(ComponentEvent ce) {
          selectItems(start, end, state, keepSelected);
        }
      });
      return;
    }
    if (start < 0 || end >= table.getItemCount()) return;
    if (!keepSelected) {
      for (TableItem item : selectedItems) {
        item.setSelected(false);
      }
      selectedItems.clear();
    }
    lastSelected = table.getItem(end);
    int beginIndex = start < end ? start : end;
    int endIndex = start < end ? end : start;
    for (int i = beginIndex; i <= endIndex; i++) {
      boolean sel = isSelected(i);
      TableItem item = table.getItem(i);
      if (state) {
        if (!sel) {
          selectedItems.add(item);
        }
        scrollIntoViewItem = item;
      } else {
        selectedItems.remove(item);
      }
      item.setSelected(state);
    }
    
    TableEvent ce = new TableEvent(table);
    table.fireEvent(Events.SelectionChange, ce);
  }

  /**
   * scroll the last selected item into view
   */
  public void scrollIntoView() {
    if (scrollIntoViewItem != null) {
      table.scrollIntoView(scrollIntoViewItem);
    }
  }
  List getSelection() {
    return selectedItems;
  }

}
