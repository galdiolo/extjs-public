/*
 * Ext GWT 2.2.0 - Ext for GWT
 * Copyright(c) 2007-2010, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.grid.filters;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.menu.CheckMenuItem;
import com.extjs.gxt.ui.client.widget.menu.Menu;

public class ListMenu extends Menu {

  private ListFilter filter;
  private ListStore<ModelData> store;
  private List<ModelData> selected;
  private Listener<MenuEvent> checkListener = new Listener<MenuEvent>() {
    public void handleEvent(MenuEvent be) {
      onCheckChange(be);
    }
  };

  @SuppressWarnings({"unchecked", "rawtypes"})
  public ListMenu(ListFilter filter, ListStore store) {
    this.filter = filter;
    this.store = store;

    selected = new ArrayList<ModelData>();
  }

  public ListFilter getFilter() {
    return filter;
  }

  public List<ModelData> getSelected() {
    return selected;
  }

  public List<String> getValue() {
    List<String> values = new ArrayList<String>();
    for (ModelData m : selected) {
      values.add((String) m.get(filter.getDisplayProperty()));
    }
    return values;
  }

  public void setSelected(List<ModelData> selected) {
    for (int i = 0; i < getItemCount(); i++) {
      CheckMenuItem item = (CheckMenuItem) getItem(i);
      item.setChecked(store.findModel((ModelData) item.getData("model")) != null, true);
    }
  }

  protected void onCheckChange(MenuEvent be) {
    CheckMenuItem item = (CheckMenuItem) be.getItem();
    ModelData m = (ModelData) item.getData("model");
    if (be.isChecked()) {
      selected.add(m);
    } else {
      selected.remove(m);
    }
    filter.onCheckChange(be);
  }

  @Override
  protected void onShow() {
    super.onShow();

    removeAll(true);
    for (int i = 0; i < store.getCount(); i++) {
      ModelData m = store.getAt(i);
      CheckMenuItem item = new CheckMenuItem();
      Object v = m.get(filter.getDisplayProperty());
      item.setText(v == null ? "" : v.toString());
      item.setChecked(selected.contains(m));
      item.setHideOnClick(false);
      item.setData("model", m);
      item.addListener(Events.CheckChange, checkListener);
      add(item);
    }

    layout();
  }

}
