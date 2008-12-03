/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget;

import java.util.Arrays;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.ListViewEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.util.KeyNav;
import com.extjs.gxt.ui.client.widget.selection.AbstractStoreSelectionModel;

/**
 * ListView selection model.
 */
public class ListViewSelectionModel<M extends ModelData> extends AbstractStoreSelectionModel<M>
    implements Listener<ListViewEvent> {

  protected ListView listView;
  protected KeyNav<ComponentEvent> keyNav = new KeyNav<ComponentEvent>() {

    @Override
    public void onDown(ComponentEvent e) {
      onKeyDown(e);
    }

    @Override
    public void onKeyPress(ComponentEvent ce) {
      ListViewSelectionModel.this.onKeyPress(ce);
    }

    @Override
    public void onUp(ComponentEvent e) {
      onKeyUp(e);
    }

  };

  public void bindList(ListView listView) {
    if (this.listView != null) {
      this.listView.removeListener(Events.Select, this);
      this.listView.removeListener(Events.RowUpdated, this);
      this.listView.removeListener(Events.Refresh, this);
      keyNav.bind(null);
      bind(null);
    }
    this.listView = listView;
    if (listView != null) {
      listView.addListener(Events.Select, this);
      listView.addListener(Events.Refresh, this);
      listView.addListener(Events.RowUpdated, this);
      keyNav.bind(listView);
      bind(listView.getStore());
    }
    bind(listView != null ? listView.getStore() : null);
  }

  public void handleEvent(ListViewEvent e) {
    switch (e.type) {
      case Events.Select:
        onSelect(e);
        break;
      case Events.RowUpdated:
        onRowUpdated(e);
        break;
      case Events.Refresh:
        refresh();
        break;
    }
  }

  protected void handleMouseDown(ListViewEvent e) {
    if (locked) return;
    M sel = store.getAt(e.index);
    
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
        int index = e.index;
        int a = (last > index) ? index : last;
        int b = (last < index) ? index : last;
        select(a, b);
        lastSelected = store.getAt(last);
        listView.focusItem(index);
        // view.focusRow(index);
      } else if (isSelected(sel) && e.isControlKey()) {
        doDeselect(Arrays.asList(sel), false);
      } else {
        doSelect(Arrays.asList(sel), e.isControlKey(), false);
        listView.focusItem(e.index);
        // view.focusRow(e.rowIndex);
      }
    }
  }

  protected boolean hasNext() {
    return lastSelected != null && store.indexOf(lastSelected) < (store.getCount() - 1);
  }

  protected boolean hasPrevious() {
    return lastSelected != null && store.indexOf(lastSelected) > 0;
  }

  protected void onKeyDown(ComponentEvent e) {
    if (!e.isShiftKey()) {
      selectNext(false);
    }
    e.preventDefault();
  }

  protected void onKeyPress(ComponentEvent e) {

  }

  protected void onKeyUp(ComponentEvent e) {
    if (!e.isShiftKey()) {
      selectPrevious(false);
    }
    e.preventDefault();
  }

  protected void onRowUpdated(ListViewEvent ge) {
    if (isSelected((M) ge.model)) {
      onSelectChange((M) ge.model, true);
    }
  }

  protected void onSelect(ListViewEvent e) {
    handleMouseDown(e);
  }

  @Override
  protected void onSelectChange(M model, boolean select) {
    listView.onSelectChange(model, select);
  }

  protected void selectNext(boolean keepexisting) {
    if (hasNext()) {
      int idx = store.indexOf(lastSelected) + 1;
      select(idx);
      listView.focusItem(idx);
    }
  }

  protected void selectPrevious(boolean keepexisting) {
    if (hasPrevious()) {
      int idx = store.indexOf(lastSelected) - 1;
      select(idx);
      listView.focusItem(idx);
    }
  }

}
