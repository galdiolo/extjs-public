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
import com.extjs.gxt.ui.client.event.ContainerEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.util.KeyNav;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.Items;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;

/**
 * Concrete selection model. 3 selection models are supported:
 * <ul>
 * <li>Single-select - single select</li>
 * <li>Multi-select - multiple selections using control and shift keys</li>
 * <li>Simple-select - multiple selections without control and shift keys</li>
 * </ul>
 * 
 * @param <C> the container type
 * @param <T> the container item type
 */
public abstract class AbstractSelectionModel<C extends Container<T>, T extends Component>
    implements SelectionModel<C, T>, Listener<ContainerEvent> {

  protected T selectedItem;
  protected List<T> selected;
  protected List<T> selectedPreRender;
  protected C container;
  protected KeyNav keyNav;
  protected SelectionMode mode;
  protected boolean singleSelect, simpleSelect, multiSelect;
  protected boolean locked;

  /**
   * Creates a new single-select selection model.
   */
  public AbstractSelectionModel() {
    this(SelectionMode.SINGLE);
  }

  /**
   * Creates a new selection model.
   * 
   * @param mode the selection mode
   */
  public AbstractSelectionModel(SelectionMode mode) {
    this.mode = mode;
    selected = new ArrayList<T>();
    singleSelect = mode == SelectionMode.SINGLE;
    simpleSelect = mode == SelectionMode.SIMPLE;
    multiSelect = mode == SelectionMode.MULTI;
  }

  public void bind(C container) {
    if (keyNav == null) {
      createKeyNav(container);
    }
    if (this.container != null) {
      this.container.removeListener(Event.ONMOUSEDOWN, this);
      this.container.removeListener(Events.OnDoubleClick, this);
      this.container.removeListener(Events.Remove, this);
      this.container.removeListener(Events.ContextMenu, this);
      keyNav.bind(null);
    }
    this.container = container;
    if (container != null) {
      container.addListener(Event.ONMOUSEDOWN, this);
      container.addListener(Events.OnDoubleClick, this);
      container.addListener(Events.Remove, this);
      container.addListener(Events.ContextMenu, this);
      keyNav.bind(container);
    }
  }

  public void deselect(int index) {
    T item = container.getItem(index);
    if (item != null) {
      deselect(item);
    }
  }

  public void deselect(int start, int end) {
    doDeselect(new Items(start, end));
  }

  public void deselect(List<T> items) {
    doDeselect(new Items(items));
  }

  public void deselect(T... items) {
    doDeselect(new Items(items));
  }

  public void deselect(T item) {
    if (locked) return;
    if (singleSelect) {
      if (selectedItem == item) {
        deselectAll();
      }
    } else {
      doDeselect(new Items(item));
    }
  }

  public void deselectAll() {
    if (singleSelect) {
      if (selectedItem != null) {
        doDeselect(new Items(selectedItem));
      }
    } else {
      deselectAll(false);
    }
  }

  public T getSelectedItem() {
    return selectedItem;
  }

  public List<T> getSelectedItems() {
    return new ArrayList<T>(selected);
  }

  /**
   * Returns the selection mode.
   * 
   * @return the selection mode
   */
  public SelectionMode getSelectionMode() {
    return mode;
  }

  public void handleEvent(ContainerEvent ce) {
    switch (ce.type) {
      case Event.ONMOUSEDOWN:
        onMouseDown(ce);
        break;
      case Events.OnDoubleClick:
        onDoubleClick(ce);
        break;
      case Events.ContextMenu:
        onContextMenu(ce);
        break;
      case Events.Remove:
        onRemove(ce);
        break;
    }
  }

  /**
   * Returns true if selections are locked.
   * 
   * @return the locked state
   */
  public boolean isLocked() {
    return locked;
  }

  public boolean isSelected(T item) {
    return selected.contains(item);
  }

  public void refresh() {
    for (T item : container.getItems()) {
      onSelectChange(item, selected.contains(item));
    }
  }

  public void select(int index) {
    T item = container.getItem(index);
    if (item != null) {
      select(item);
    }
  }

  public void select(int start, int end) {
    if (singleSelect) {
      doSelect(new Items(start), false, false);
    } else {
      doSelect(new Items(start, end), false, false);
    }
  }

  public void select(List<T> items) {
    if (singleSelect) {
      if (items.size() > 0) {
        select(items.get(0));
      } else if (selected.size() > 0) {
        deselectAll();
      }
    } else {
      doSelect(new Items(items), false, false);
    }
  }

  public void select(T... items) {
    if (singleSelect) {
      if (items.length > 0) {
        select(items[0]);
      }
    } else {
      doSelect(new Items(items), false, false);
    }
  }

  public void select(T item) {
    doSelect(new Items(item), false, false);
  }

  public void selectAll() {
    if (singleSelect) {
      doSelect(new Items(0), false, false);
    } else {
      doSelect(new Items(0, container.getItemCount() - 1), false, false);
    }
  }

  /**
   * Sets whether selections are locked.
   * 
   * @param locked true to lock
   */
  public void setLocked(boolean locked) {
    this.locked = locked;
  }

  protected native ContainerEvent createContainerEvent(Container container) /*-{
    return container.@com.extjs.gxt.ui.client.widget.Container::createContainerEvent(Lcom/extjs/gxt/ui/client/widget/Component;)(null);
    }-*/;

  protected void createKeyNav(Container tree) {
    keyNav = new KeyNav<ContainerEvent>() {

      @Override
      public void onDown(ContainerEvent ce) {
        onKeyDown(ce);
      }

      @Override
      public void onLeft(ContainerEvent ce) {
        onKeyLeft(ce);
      }

      @Override
      public void onRight(ContainerEvent ce) {
        onKeyRight(ce);
      }

      @Override
      public void onUp(ContainerEvent ce) {
        onKeyUp(ce);
      }

    };
  }

  protected void deselectAll(boolean supressEvent) {
    if (locked) return;
    boolean change = selected.size() > 0;
    for (T item : selected) {
      onSelectChange(item, false);
    }
    selected.clear();
    if (!supressEvent && change) {
      fireSelectionChanged();
    }
  }

  protected void doDeselect(Items<T> items) {
    if (locked) return;
    boolean change = false;
    for (T item : items.getItems(container)) {
      if (isSelected(item)) {
        change = true;
        onSelectChange(item, false);
        selected.remove(item);
        if (selectedItem == item) {
          selectedItem = null;
        }
      }
    }
    if (change) {
      fireSelectionChanged();
    }
  }

  protected void doMultiSelect(T item, ContainerEvent ce) {
    if (locked) return;
    int index = container.indexOf(item);
    if (ce.isShiftKey() && selectedItem != null) {
      int last = container.indexOf(selectedItem);
      doSelect(new Items(last, index), false, false);
      selectedItem = container.getItem(last);
    } else {
      if (ce.isControlKey() && isSelected(item)) {
        deselect(container.getItem(index));
      } else {
        doSelect(new Items(index), ce.isControlKey(), false);
      }
    }
  }

  protected void doSelect(final Items<T> items, boolean keepExisting, boolean supressEvent) {
    if (locked) return;
    List<T> previous = new ArrayList<T>(selected);
    if (!items.isSingle()) {
      if (!keepExisting) {
        deselectAll(true);
      }
      for (Object item : items.getItems(container)) {
        doSelect(new Items((Component) item), true, true);
      }
      if (!supressEvent && hasSelectionChanged(previous, selected)) {
        fireSelectionChanged();
      }
    } else {
      T item = items.getItem(container);
      if (item != null) {
        ContainerEvent ce = createContainerEvent(container);
        ce.item = item;
        if (container.fireEvent(Events.BeforeSelect, ce)) {
          if (!keepExisting) {
            deselectAll(true);
          }
          onSelectChange(item, true);
          selected.add(item);
          selectedItem = item;
          if (!supressEvent && hasSelectionChanged(previous, selected)) {
            fireSelectionChanged();
          }
        }
      }
    }
  }

  protected void doSelectChange(T item, boolean select) {
    if (locked) return;
    if (container instanceof Selectable) {
      ((Selectable) container).onSelectChange(item, select);
    }
  }

  protected void doSingleSelect(T item, int index, ContainerEvent ce) {
    if (simpleSelect) {
      if (isSelected(item)) {
        deselect(item);
      } else {
        select(item);
      }
      return;
    }
    if (ce.isControlKey() && isSelected(item)) {
      deselect(container.getItem(index));
    } else {
      doSelect(new Items(item), false, false);
    }
  }

  protected void fireSelectionChanged() {
    ContainerEvent event = createContainerEvent(container);
    event.selected = getSelectedItems();
    event.event = DOM.eventGetCurrentEvent();
    container.fireEvent(Events.SelectionChange, event);
  }

  protected void hookPreRender(T item, boolean select) {
    if (selectedPreRender == null) {
      selectedPreRender = new ArrayList<T>();
      container.addListener(Events.Render, new Listener<ContainerEvent>() {
        public void handleEvent(ContainerEvent be) {
          container.removeListener(Events.Render, this);
          onRender();
        }
      });
    }
    if (select && !selectedPreRender.contains(item)) {
      selectedPreRender.add(item);
    } else if (!select) {
      selectedPreRender.remove(item);
    }
  }

  protected T next() {
    if (selectedItem != null) {
      int index = container.indexOf(selectedItem);
      if (index < (container.getItemCount() - 1)) {
        return container.getItem(++index);
      }
    }
    return null;
  }

  protected void onMouseDown(ContainerEvent ce) {
    if (locked) return;
    T item = (T) ce.item;
    if (item != null) {
      if (simpleSelect) {
        if (isSelected(item)) {
          deselect(item);
        } else {
          doSelect(new Items(item), true, false);
        }
      } else if (singleSelect) {
        doSingleSelect(item, container.indexOf(item), ce);
      } else {
        doMultiSelect(item, ce);
      }
    }
  }

  protected void onContextMenu(ContainerEvent ce) {
    if (locked) return;
    T item = container.findItem(ce.getTarget());
    if (item != null) {
      if (selected.size() > 1 && selected.contains(item)) {
        return;
      }
      select(item);
    }
  }

  protected void onDoubleClick(ContainerEvent ce) {

  }

  protected void onKeyDown(ContainerEvent ce) {
    T item = next();
    if (item != null) {
      doSelect(new Items(item), false, false);
      container.scrollIntoView(item);
      ce.stopEvent();
    }
  }

  protected void onKeyLeft(ContainerEvent ce) {

  }

  protected void onKeyRight(ContainerEvent ce) {

  }

  protected void onKeyUp(ContainerEvent ce) {
    T item = previous();
    if (item != null) {
      doSelect(new Items(item), false, false);
      container.scrollIntoView(item);
      ce.stopEvent();
    }
  }

  protected void onRemove(ContainerEvent ce) {
    T item = (T) ce.item;
    if (isSelected(item)) {
      deselect(item);
    }
  }

  protected void onRender() {
    if (selectedPreRender != null) {
      for (T item : selectedPreRender) {
        doSelectChange(item, true);
      }
      selectedPreRender = null;
    }
  }

  protected void onSelectChange(T item, boolean select) {
    if (!container.isRendered()) {
      hookPreRender(item, select);
      return;
    }
    doSelectChange(item, select);
  }

  protected T previous() {
    if (selectedItem != null) {
      int index = container.indexOf(selectedItem);
      if (index > 0) {
        return container.getItem(--index);
      }
    }
    return null;
  }

  private boolean hasSelectionChanged(List<T> prevSel, List<T> newSel) {
    if (prevSel.size() != newSel.size()) {
      return true;
    } else {
      for (T sel : prevSel) {
        if (!newSel.contains(sel)) {
          return true;
        }
      }
    }
    return false;
  }

}
