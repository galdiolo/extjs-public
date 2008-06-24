/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.binder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.TableEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.table.Table;
import com.extjs.gxt.ui.client.widget.table.TableColumn;
import com.extjs.gxt.ui.client.widget.table.TableItem;

/**
 * A store binder for tables.
 * 
 * @param <M> the model type
 */
public class TableBinder<M extends ModelData> extends StoreBinder<ListStore<M>, Table, M> {

  private Table table;
  private ListStore<M> store;

  /**
   * Creates a new table binder. {@link #init()} must be called after
   * configuring the binder.
   * 
   * @param table
   * @param store
   */
  public TableBinder(final Table table, ListStore<M> store) {
    super(store, table);
    this.table = table;
    this.store = store;

    setMask(true);

    table.addListener(Events.SortChange, new Listener<TableEvent>() {
      public void handleEvent(TableEvent te) {
        handleTableSort(te);
      }
    });
  }

  @Override
  public Component findItem(M model) {
    for (TableItem item : table.getItems()) {
      if (store.getModelComparer().equals((M) item.getData(), model)) {
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
  public ListStore<M> getStore() {
    return store;
  }

  /**
   * Returns the binder's table.
   * 
   * @return the table
   */
  public Table getTable() {
    return table;
  }

  @Override
  protected void removeAll() {
    table.removeAll();
  }

  @Override
  protected void update(M model) {
    TableItem item = (TableItem) findItem(model);
    if (item != null) {
      item.setData(model);
      updateItemStyles(item);
      updateItemValues(item);
    }
  }

  @Override
  protected void createAll() {
    table.removeAll();
    for (M m : store.getModels()) {
      table.add(createItem(m));
    }

    if (isAutoSelect() && store.getCount() > 0) {
      setSelection(store.getAt(0));
    }
  }

  protected TableItem createItem(M model) {
    TableItem item = new TableItem(new Object[table.getColumnCount()]);
    item.setData(model);
    updateItemValues(item);
    updateItemStyles(item);
    return item;
  }

  protected String getColumnId(int column) {
    return table.getColumn(column).getId();
  }

  @Override
  protected List<M> getSelectionFromComponent() {
    List<M> selection = new ArrayList<M>();
    List<TableItem> sel = table.getSelectedItems();
    for (TableItem item : sel) {
      selection.add((M) item.getData());
    }
    return selection;
  }

  protected void handleTableSort(TableEvent te) {
    TableColumn col = table.getColumn(te.columnIndex);
    SortDir dir = col.getSortDir();
    dir = dir == SortDir.ASC ? SortDir.DESC : SortDir.ASC;
    store.sort(col.getId(), dir);
    te.doit = false;
  }

  @Override
  protected void onAdd(StoreEvent be) {
    List<M> models = be.models;
    for (int i = models.size() - 1; i >= 0; i--) {
      table.insert(createItem(models.get(i)), be.index);
    }
  }

  @Override
  protected void onClear(StoreEvent se) {
    table.removeAll();
  }

  @Override
  protected void onDataChanged(StoreEvent se) {
    super.onDataChanged(se);
    createAll();

    if (store.getSortField() != null) {
      TableColumn col = table.getColumnModel().getColumn(store.getSortField());
      table.getTableHeader().onSortChange(col, store.getSortDir());
    } else {
      table.getTableHeader().clearSort();
    }

    table.getView().resize();
  }

  @Override
  protected void onFilter(StoreEvent se) {
    if (store.isFiltered()) {
      for (TableItem item : table.getItems()) {
        M model = (M) item.getData();
        item.setVisible(store.contains(model));
      }
    } else {
      for (TableItem item : table.getItems()) {
        item.setVisible(true);
      }
    }
  }

  @Override
  protected void onRemove(StoreEvent se) {
    TableItem item = (TableItem) findItem((M) se.model);
    if (item != null) {
      table.remove(item);
    }
  }

  @Override
  protected void onSort(StoreEvent se) {
    String id = store.getSortField();
    table.getTableHeader().onSortChange(table.getColumn(id), store.getSortDir());
    
    Collections.sort(table.getItems(), new Comparator<TableItem>() {
      public int compare(TableItem o1, TableItem o2) {
        int idx1 = store.indexOf((M)o1.getData());
        int idx2 = store.indexOf((M)o2.getData());
        return idx1 < idx2 ? -1 : 1;
      }
    });
    int rows = table.getItemCount();
    for (int i = 0; i < rows; i++) {
      TableItem item = table.getItem(i);
      table.getView().getDataEl().dom.appendChild(item.getElement());
    }
  }

  @Override
  protected void onUpdate(StoreEvent se) {
    if (se.model != null) {
      update((M) se.model);
    }
  }

  @Override
  protected void setSelectionFromProvider(List<M> selection) {
    List<TableItem> sel = new ArrayList<TableItem>();
    for (M m : selection) {
      TableItem item = (TableItem) findItem(m);
      if (item != null) {
        sel.add(item);
      }
    }
    table.setSelectedItems(sel);
  }

  private void updateItemStyles(TableItem item) {
    M model = (M) item.getData();
    int cols = table.getColumnCount();
    for (int i = 0; i < cols; i++) {
      String id = getColumnId(i);
      String style = (styleProvider == null) ? null : styleProvider.getStringValue(model, id);
      item.setCellStyle(i, style == null ? "" : style);
    }
  }

  private void updateItemValues(TableItem item) {
    M model = (M) item.getData();
    int cols = table.getColumnCount();
    for (int j = 0; j < cols; j++) {
      String id = getColumnId(j);
      Object val = getTextValue(model, id);
      if (val == null) val = model.get(id);
      item.setValue(j, val);
    }
  }

}
