/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.binder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelStringProvider;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.DataListItem;
import com.extjs.gxt.ui.client.widget.DataView;
import com.extjs.gxt.ui.client.widget.DataViewItem;

/**
 * Binds a data view and a store.
 *
 * @param <M> the model type
 */
public class DataViewBinder<M extends ModelData> extends StoreBinder<ListStore<M>, DataView, M> {

  private DataView view;
  private ListStore<M> store;

  /**
   * Creates a new data view binder.
   *
   * @param view the data view
   * @param store the list store
   */
  public DataViewBinder(DataView view, ListStore store) {
    super(store, view);
    this.view = view;
    this.store = store;
  }

  @Override
  public Component findItem(M model) {
    for (DataViewItem item : view.getItems()) {
      if (store.getModelComparer().equals((M) item.getModel(), model)) {
        return item;
      }
    }
    return null;
  }

  /**
   * Returns the binder's store.
   *
   * @return the store
   */
  public ListStore getStore() {
    return store;
  }

  /**
   * Returns the data view.
   *
   * @return this view
   */
  public DataView getView() {
    return view;
  }

  @Override
  protected void createAll() {
    view.removeAll();
    for (M m : store.getModels()) {
      view.add(createItem(m));
    }
  }

  protected DataViewItem createItem(M model) {
    Map<String, Object> values = model.getProperties();
    if (view.getTemplate() != null && view.getTemplate().getStringProvider() != null) {
      ModelStringProvider provider = view.getTemplate().getStringProvider();
      for (String s : values.keySet()) {
        String val = provider.getStringValue(model, s);
        if (val != null) {
          values.put(s, val);
        }
      }
    }
    DataViewItem item = new DataViewItem(values);
    setModel(item, model);
    return item;
  }

  @Override
  protected List<M> getSelectionFromComponent() {
    List<M> selection = new ArrayList<M>();

    List<DataViewItem> sel = view.getSelectedItems();
    for (DataViewItem item : sel) {
      selection.add((M) item.getModel());
    }
    return selection;
  }

  @Override
  protected void onAdd(StoreEvent se) {
    List<M> models = se.models;
    for (int i = models.size() - 1; i >= 0; i--) {
      view.insert(createItem(models.get(i)), se.index);
    }
  }

  @Override
  protected void onDataChanged(StoreEvent se) {
    createAll();
  }

  @Override
  protected void onFilter(StoreEvent se) {
    if (store.isFiltered()) {
      for (DataViewItem item : view.getItems()) {
        M m = (M) item.getModel();
        item.setVisible(store.contains(m));
      }
    } else {
      for (DataViewItem item : view.getItems()) {
        item.setVisible(true);
      }
    }
  }

  @Override
  protected void onRemove(StoreEvent se) {
    DataViewItem item = (DataViewItem) findItem((M) se.model);
    if (item != null) {
      view.remove(item);
    }
  }

  @Override
  protected void onSort(StoreEvent se) {
    if (view.getItemCount() > 0) {
      El parent = view.getItem(0).el().getParent();
      int count = store.getCount();
      for (int i = 0; i < count; i++) {
        M m = store.getAt(i);
        DataListItem item = (DataListItem) findItem(m);
        parent.dom.appendChild(item.getElement());
      }
    }
  }

  @Override
  protected void onUpdate(StoreEvent se) {
    if (se.model != null) {
      update((M) se.model);
    }
  }

  @Override
  protected void removeAll() {
    view.removeAll();
  }

  @Override
  protected void setSelectionFromProvider(List<M> selection) {
    List<DataViewItem> sel = new ArrayList<DataViewItem>();
    for (M m : selection) {
      DataViewItem item = (DataViewItem) findItem(m);
      if (item != null) {
        sel.add(item);
      }
    }
    view.setSelectedItems(sel);
  }

  @Override
  protected void update(M model) {
    DataViewItem item = (DataViewItem) findItem(model);
    if (item != null) {
      setModel(item, model);
    }
  }

}