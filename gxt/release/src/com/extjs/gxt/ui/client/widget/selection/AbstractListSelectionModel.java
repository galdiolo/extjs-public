/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.selection;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.AbstractContainer;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Items;
import com.google.gwt.user.client.ui.KeyboardListener;

/**
 * Abstract base class for list based containers.
 * 
 * @param <T> the container sub type
 * @param <C> the container's child type
 * @param <L> the component event subtype
 */
public abstract class AbstractListSelectionModel<C extends Component, T extends AbstractContainer, L extends ComponentEvent>
    implements SelectionModel<T>, Listener<L> {

  protected T list;
  protected List<C> selectedItems;
  protected C lastSelected;
  protected SelectionMode selectionMode;

  /**
   * Creates a new single select selection model.
   */
  public AbstractListSelectionModel() {
    this(SelectionMode.SINGLE);
  }

  /**
   * Creates a new list selection model.
   * 
   * @param selectionMode the selection mode
   */
  public AbstractListSelectionModel(SelectionMode selectionMode) {
    selectedItems = new ArrayList<C>();
    this.selectionMode = selectionMode;
  }

  public void bind(T component) {
    if (this.list != null) {
      list.removeListener(Events.OnClick, this);
      list.removeListener(Events.OnDoubleClick, this);
      list.removeListener(Events.KeyPress, this);
      list.removeListener(Events.Remove, this);
    }
    this.list = component;
    if (this.list != null) {
      list.addListener(Events.OnClick, this);
      list.addListener(Events.OnDoubleClick, this);
      list.addListener(Events.KeyPress, this);
      list.addListener(Events.Remove, this);
    }
  }

  /**
   * Deselects the item(s).
   * 
   * @param items the item(s) to deselect
   */
  public void deselect(C... items) {
    doSeselect(new Items(items));
  }

  /**
   * Deselects the item(s).
   * 
   * @param start the start index
   * @param end the end index
   */
  public void deselect(int start, int end) {
    doSeselect(new Items(start, end));
  }

  /**
   * Deselects the item(s).
   * 
   * @param items the item(s)
   */
  public void deselect(List<C> items) {
    doSeselect(new Items(items));
  }

  /**
   * Deselects all selections.
   */
  public void deselectAll() {
    deselectAll(false);
  }

  /**
   * Deselects all selections.
   * 
   * @param supressEvent true to supress the selection change event
   */
  public void deselectAll(boolean supressEvent) {
    for (C item : selectedItems) {
      onSelectChange(item, false);
    }
    selectedItems.clear();
    if (!supressEvent) {
      list.fireEvent(Events.SelectionChange);
    }
  }

  /**
   * Returns the number of selected items.
   * 
   * @return the selected count
   */
  public int getSelectedCount() {
    return selectedItems.size();
  }

  /**
   * Returns the selected item.
   * 
   * @return the selected item
   */
  public C getSelectedItem() {
    if (selectedItems.size() > 0) {
      return selectedItems.get(0);
    }
    return null;
  }

  /**
   * Returns the selected items.
   * 
   * @return the item
   */
  public List<C> getSelectedItems() {
    return new ArrayList<C>(selectedItems);
  }

  /**
   * Returns the selection mode.
   * 
   * @return the selection mode
   */
  public SelectionMode getSelectionMode() {
    return selectionMode;
  }

  public void handleEvent(L e) {
    switch (e.type) {
      case Events.OnClick:
        onClick(e);
        break;
      case Events.OnDoubleClick:
        onDoubleClick(e);
        break;
      case Events.KeyPress:
        onKeyPress(e);
        break;
    }
  }

  /**
   * Returns <code>true</code> if the row is selected.
   * 
   * @param item the item
   * @return the select state
   */
  public boolean isSelected(C item) {
    return selectedItems.contains(item);
  }

  /**
   * Changes the select state for the item.
   * 
   * @param item the item
   * @param select the select state
   */
  public abstract void onSelectChange(C item, boolean select);

  public void refresh() {
    for (C item : selectedItems) {
      onSelectChange(item, true);
    }
  }

  /**
   * Selects the item(s).
   * 
   * @param items the item(s)
   */
  public void select(C... items) {
    select(new Items(items), false);
  }

  /**
   * Selects a range of item(s).
   * 
   * @param start the start index
   * @param end the end index
   */
  public void select(int start, int end) {
    select(new Items(start, end), false);
  }

  /**
   * Selects the item(s).
   * 
   * @param items the item(s)
   */
  public void select(List<C> items) {
    select(items);
  }

  /**
   * Selects all items.
   */
  public void selectAll() {
    select(new Items(0, list.getItemCount()), false);
  }

  /**
   * Sets the selection mode. Any existing selections are cleared.
   * 
   * @param selectionMode the selection mode
   */
  public void setSelectionMode(SelectionMode selectionMode) {
    this.selectionMode = selectionMode;
    deselectAll();
  }

  protected abstract L createEvent(T list, C item);

  protected void doSeselect(Items<C> items) {
    boolean change = false;
    for (C item : items.getItems(list)) {
      if (isSelected(item)) {
        change = true;
        onSelectChange(item, false);
        selectedItems.remove(item);
        if (lastSelected == item) {
          lastSelected = null;
        }
      }
    }
    if (change) {
      list.fireEvent(Events.SelectionChange);
    }
  }

  protected void doMultiSelect(C item, int index, L ce) {
    if (ce.isShiftKey() && lastSelected != null) {
      int last = list.indexOf((C) lastSelected);
      select(new Items(last, index), ce.isControlKey());
      lastSelected = (C) list.getItem(last);
    } else {
      if (ce.isControlKey() && isSelected(item)) {
        deselect((C) list.getItem(index));
      } else {
        select(new Items(index), ce.isControlKey());
      }
    }
  }

  protected void doSelect(Items<C> items, boolean keepExisting, boolean supressEvent) {
    if (!items.isSingle()) {
      if (!keepExisting) {
        deselectAll();
      }
      for (Object item : items.getItems(list)) {
        doSelect(new Items((Component) item), true, true);
      }
      if (!supressEvent) {
        list.fireEvent(Events.SelectionChange, createEvent(list, null));
      }
    } else {
      if (!keepExisting) {
        deselectAll(true);
      }
      C item = items.getItem(list);
      if (item != null) {
        L event = createEvent(list, item);
        if (list.fireEvent(Events.BeforeSelect, event)) {
          onSelectChange(item, true);
          selectedItems.add(item);
          lastSelected = item;
          if (!supressEvent) {
            list.fireEvent(Events.SelectionChange, event);
          }
        }
      }
    }
  }

  protected void doSingleSelect(C item, int index, L ce) {
    if (ce.isControlKey() && isSelected(item)) {
      deselect((C) list.getItem(index));
    } else {
      select(new Items(item), false);
    }
  }

  protected C next() {
    if (lastSelected != null) {
      int index = list.indexOf(lastSelected);
      if (index < (list.getItemCount() - 1)) {
        return (C) list.getItem(++index);
      }
    }
    return null;
  }

  protected void onClick(L e) {
    C item = (C) list.findItem(e.getTarget());
    if (item != null) {
      if (selectionMode == SelectionMode.SINGLE) {
        doSingleSelect(item, list.indexOf(item), e);
        return;
      } else {
        doMultiSelect(item, list.indexOf(item), e);
      }
    }

    // if (e) {
    // System.out.println("right");
    // TableItem item = table.findItem(te.getTarget());
    // if (selectedItems.contains(item)) {
    // return;
    // }
    // }

    // safari needs focus put on table of click unless
    // the source is a widget - determined by sunk events
    // if (GXT.isSafari) {
    // Element elem = e.getTarget();
    // if (DOM.getEventsSunk(elem) == 0) {
    // list.focus();
    // }
    //
    // }
  }

  protected void onDoubleClick(L e) {

  }

  protected void onKeyPress(L e) {
    int code = e.getKeyCode();
    switch (code) {
      case KeyboardListener.KEY_UP: {
        C item = previous();
        if (item != null) {
          doSelect(new Items(item), false, false);
          list.scrollIntoView(item);
          e.stopEvent();
        }
        break;
      }
      case KeyboardListener.KEY_DOWN: {
        C item = next();
        if (item != null) {
          doSelect(new Items(item), false, false);
          list.scrollIntoView(item);
          e.stopEvent();
        }
        break;
      }

    }
  }

  protected void onRemove(C item) {

  }

  protected C previous() {
    if (lastSelected != null) {
      int index = list.indexOf(lastSelected);
      if (index > 0) {
        return (C) list.getItem(--index);
      }
    }
    return null;
  }

  protected void select(Items items) {
    select(items, false);
  }

  protected void select(Items items, boolean keepSelected) {
    doSelect(items, keepSelected, false);
  }

}
