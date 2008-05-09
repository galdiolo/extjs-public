/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.store;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.BaseLoadConfig;
import com.extjs.gxt.ui.client.data.DataCallback;
import com.extjs.gxt.ui.client.data.DataProxy;
import com.extjs.gxt.ui.client.data.DataReader;
import com.extjs.gxt.ui.client.data.LoadConfig;
import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.data.LoadResult;
import com.extjs.gxt.ui.client.data.Loader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.TypedListener;
import com.extjs.gxt.ui.client.util.Observable;
import com.extjs.gxt.ui.client.widget.DataView;
import com.extjs.gxt.ui.client.widget.form.ComboBox;

/**
 * The Store class encapsulates a client side cache of {@link Record Record}
 * objects which provide input data for Components such as the {@link ComboBox}
 * and {@link DataView DataView}
 * 
 * </p>
 * This class is not fully implemented. Please review the source code.
 * 
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>DataChange</b> : StoreEvent(store)<br>
 * <div>Fires when the data cache has changed, and a widget which is using this
 * Store as a Record cache should refresh its view.</div>
 * <ul>
 * <li>store : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Add</b> : StoreEvent(store, records, index)<br>
 * <div>Fires when Records have been added to the Store.</div>
 * <ul>
 * <li>store : this</li>
 * <li>records : the added records</li>
 * <li>index : the index at which the record(s) were added</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Remove</b> : StoreEvent(store, record)<br>
 * <div>Fires when a Record has been removed from the Store.</div>
 * <ul>
 * <li>store : this</li>
 * <li>record : the record that was removed</li>
 * <li>index : the index at which the record was removed</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Update</b> : StoreEvent(store, record)<br>
 * <div>Fires when a Record has been updated.</div>
 * <ul>
 * <li>store : this</li>
 * <li>record : the record that was updated</li>
 * <li>operation : the update operation being performed. Value may be one of:
 * EDIT, REJECT, COMMIT.</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Clear</b> : StoreEvent(store)<br>
 * <div>Fires when the data cache has been cleared.</div>
 * <ul>
 * <li>store : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeLoad</b> : StoreEvent(store, options)<br>
 * <div>Fires before a request is made for a new data object.</div>
 * <ul>
 * <li>store : this</li>
 * <li>option : the loading options that were specified (see {@link #load} for
 * details)</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Load</b> : StoreEvent(store, records, options)<br>
 * <div>Fires after a new set of Records has been loaded.</div>
 * <ul>
 * <li>store : this</li>
 * <li>records : the records that were loaded</li>
 * <li>options : the loading options that were specified (see {@link #load} for
 * details)</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>LoadException</b> : StoreEvent(store)<br>
 * <div>Fires after a new set of Records has been loaded.</div>
 * <ul>
 * <li>store : this</li>
 * </ul>
 * </dd>
 * </dt>
 */
public class Store<C extends LoadConfig> extends Observable implements Loader {

  /**
   * DataChange event type.
   */
  public static final int DataChanged = 1100;

  /**
   * Add event type.
   */
  public static final int Add = 1110;

  /**
   * Remove event type.
   */
  public static final int Remove = 1120;

  /**
   * Update event type.
   */
  public static final int Update = 1130;

  /**
   * Clear event type.
   */
  public static final int Clear = 1140;

  /**
   * Convenient method that unwraps the wrapped Model from each Record
   * 
   * Useful in conjunction with {@link #getRecords()} and
   * {@link #getModifiedRecords()}
   * 
   */
  public static List<? extends ModelData> unwrap(List<Record> records) {
    List<ModelData> models = new ArrayList<ModelData>();
    for (Record record : records) {
      models.add(record.getModel());
    }
    return models;
  }

  protected DataProxy<C> dataProxy;
  protected DataReader reader;
  protected int offset = 0;
  protected int limit = 50;
  C lastLoadConfig;

  private boolean reuseLoadConfig = false;
  private boolean remoteSort;
  private SortInfo sortInfo;
  private int totalCount;
  private List<Record> items = new ArrayList<Record>();
  private List<Record> modified = new ArrayList<Record>();
  private List<StoreFilter> filters;

  /**
   * Creates a new store.
   */
  public Store() {

  }

  public Store(DataProxy<C> dataProxy, DataReader reader) {
    this.dataProxy = dataProxy;
    this.reader = reader;
  }

  /**
   * Adds a the items to the store and fires the <i>Add</i> event.
   * 
   * @param items the items to add
   * @return the Record that represents wach item in Items.
   */
  public List<Record> add(List<? extends ModelData> items) {
    return insert(items, getCount());
  }

  /**
   * Adds the item to the store and fires the <i>Add</i> event.
   * 
   * @param item the item to add
   */
  public Record add(ModelData item) {
    return insert(item, getCount());
  }

  /**
   * Adds a filter to the store.
   * 
   * @param filter the store filter to add
   */
  public void addFilter(StoreFilter filter) {
    if (filters == null) {
      filters = new ArrayList<StoreFilter>();
    }
    filters.add(filter);
  }

  /**
   * Adds a store listener.
   * 
   * @param listener the listener to add
   */
  public void addStoreListener(StoreListener listener) {
    TypedListener tl = new TypedListener(listener);
    addListener(Store.BeforeLoad, tl);
    addListener(Store.DataChanged, tl);
    addListener(Store.Add, tl);
    addListener(Store.Remove, tl);
    addListener(Store.Update, tl);
    addListener(Store.Clear, tl);
  }

  /**
   * Revert to a view of this store with no filtering applied.
   * 
   * @param supressEvent if true the filter is cleared silently without
   *            notifying listeners
   */
  public void clearFilters(boolean supressEvent) {
    if (isFiltered()) {

    }
  }

  /**
   * Commit all Records with outstanding changes. To handle updates for changes,
   * subscribe to the Store's <i>Update</i> event, and perform updating when
   * the operation parameter is {@link Record#COMMIT}.
   */
  public void commitChanges() {
    for (Record r : modified) {
      r.commit(false);
    }
    modified = new ArrayList<Record>();
  }

  /**
   * Get the Record at the specified index.
   * 
   * @param index the index of the record to find
   * @return the record at the passed index
   */
  public Record getAt(int index) {
    return items.get(index);
  }

  /**
   * Gets the number of cached records.
   * <p>
   * If using paging, this may not be the total size of the dataset. If the data
   * object used by the Reader contains the dataset size, then the
   * {@link #getTotalCount} function returns the dataset size.
   * </p>
   * 
   * @return the number of Records in the Store's cache.
   */
  public int getCount() {
    return items.size();
  }

  /**
   * Gets all records modified since the last commit. Modified records are
   * persisted across load operations (e.g., during paging).
   * 
   * @return a list of modified records
   */
  public List<Record> getModifiedRecords() {
    return new ArrayList<Record>(modified);
  }

  public int getPageSize() {
    // TODO Auto-generated method stub
    return 0;
  }

  /**
   * Returns a range of Records between specified indices.
   * 
   * @param start the starting index
   * @param end the ending index
   * @return the list of records
   */
  public List<Record> getRange(int start, int end) {
    return items.subList(start, end);
  }

  public List<Record> getRecords() {
    return new ArrayList<Record>(items);
  }

  public boolean getRemoteSort() {
    return remoteSort;
  }

  public SortDir getSortDir() {
    return sortInfo.sortDir;
  }

  public String getSortField() {
    return sortInfo.sortField;
  }

  /**
   * Returns the current sort state of this Store.
   * 
   * @return the sort state
   */
  public SortInfo getSortState() {
    return sortInfo;
  }

  public int getOffset() {
    // TODO Auto-generated method stub
    return 0;
  }

  /**
   * Gets the total number of records in the dataset as returned by the server.
   * <p>
   * If using paging, for this to be accurate, the data object used by the
   * Reader must contain the dataset size. For remote data sources, this is
   * provided by a query on the server.
   * </p>
   * 
   * @return the number of records as passed from the server
   */
  public int getTotalCount() {
    return totalCount;
  }

  /**
   * returns the index of the item in this store.
   * 
   * @param item the item
   * @return the index
   */
  public int indexOf(ModelData item) {
    if (item instanceof Record) {
      return items.indexOf(item);
    } else {
      int i = 0;
      for (Record record : items) {
        if (record.getModel().equals(item)) {
          return i;
        }
        i++;
      }
    }
    return -1;
  }

  /**
   * Inserts the model to the Store at the given index and fires the <i>Add</i>
   * event.
   * <p>
   * The input can contain anything that extends ModelStorage.
   * </p>
   * <p>
   * If the input element is not a Record, it will be wrapped in a Record.
   * </p>
   * 
   * @param records the records to insert
   * @param index the insert location
   */
  @SuppressWarnings("unchecked")
  public List<Record> insert(List<? extends ModelData> records, int index) {
    List<Record> list = new ArrayList<Record>();
    for (ModelData m : records) {
      Record r = m instanceof Record ? (Record) m : new Record(m);
      r.join(this);
      list.add(r);
    }
    items.addAll(index, list);
    for (Record r : list) {
      r.join(this);
    }
    fireStoreEvent(Store.Add, list, null, index);
    return list;
  }

  /**
   * Inserts the model to the Store at the given index and fires the <i>Add</i>
   * event.
   * 
   * <p>
   * If the model is not a Record it will be wapped in a record
   * </p>
   * 
   * @param model the record to insert
   * @param index the insert location
   */
  public Record insert(ModelData model, int index) {
    ArrayList<ModelData> list = new ArrayList<ModelData>();
    list.add(model);
    List<Record> inserted = insert(list, index);
    return inserted.get(0);
  }

  /**
   * Returns true if this store is currently filtered.
   * 
   * @return true if the store is filtered
   */
  public boolean isFiltered() {
    return filters != null && filters.size() > 0;
  }

  public void load() {
    C config = (isReuseLoadConfig() && lastLoadConfig != null) ? lastLoadConfig
        : newLoadConfig();
    final C loadConfig = prepareLoadConfig(config);
    LoadEvent evt = new LoadEvent(this, loadConfig, null);
    if (fireEvent(BeforeLoad, evt)) {
      loadData(loadConfig, new DataCallback() {
        public void setResult(LoadResult result) {
          onLoadResult(loadConfig, result);
        }
      });
    }
  }

  public void load(int offset, int limit) {
    this.offset = offset;
    this.limit = limit;
    load();
  }

  /**
   * Loads the Record cache.
   * 
   * @param options options for the load call
   * @return whether the load fired (if <i>BeforeLoad</i> failed).
   */
  public boolean load(LoadConfig options) {
    if (options == null) {
      options = new BaseLoadConfig();
    }
    if (fireEvent(BeforeLoad)) {
      storeOptions(options);
      if (sortInfo != null && remoteSort) {

      }
      return true;

    } else {
      return false;
    }
  }

  /**
   * Cancel outstanding changes on all changed records.
   */
  public void rejectChanges() {

  }

  /**
   * Reloads the Record cache from the configured Proxy using the configured
   * Reader and the options from the last load operation performed.
   */
  public void reload() {
    load(lastLoadConfig);
  }

  /**
   * Remove a Model (or a Record) from the Store and fires the <i>Remove</i>
   * event.
   * 
   * @param model the model to remove
   */
  public void remove(ModelData model) {
    int index = indexOf(model);
    if (index > -1) {
      Record record = items.remove(index);
      fireStoreEvent(Store.Remove, null, record, index);
    }
  }

  /**
   * Remove all Records from the Store and fires the <i>Clear</i> event.
   */
  public void removeAll() {
    items.clear();
    fireEvent(Store.Clear);
  }

  public void removeFilter(StoreFilter filter) {
    if (filters != null) {
      filters.remove(filter);
    }
  }

  public void removeStoreListener(StoreListener listener) {
    removeListener(Store.BeforeLoad, listener);
    removeListener(Store.DataChanged, listener);
    removeListener(Store.Add, listener);
    removeListener(Store.Remove, listener);
    removeListener(Store.Update, listener);
    removeListener(Store.Clear, listener);
  }

  /**
   * Sets the default sort column and order to be used by the next load
   * operation.
   * 
   * @param field the name of the field to sort by
   * @param dir the sort order, ASC or DESC
   */
  public void setDefaultSort(String field, SortDir dir) {
    sortInfo = new SortInfo(field, dir);
  }

  public void setRemoteSort(boolean remoteSort) {

  }

  public void setSortDir(SortDir dir) {
    sortInfo.sortDir = dir;
  }

  public void setSortField(String field) {

  }

  /**
   * Sort the Records.
   * <p>
   * If remote sorting is used, the sort is performed on the server, and the
   * cache is reloaded. If local sorting is used, the cache is sorted
   * internally.
   * </p>
   * 
   * @param fieldName
   * @param dir
   */
  public void sort(String fieldName, int dir) {

  }

  /**
   * Sums the value of <i>property</i> for each record between start and end
   * and returns the result.
   * 
   * @param property
   * @param start
   * @param end
   * @return the total
   */
  public float sum(String property, int start, int end) {
    return 0f;
  }

  protected void afterCommit(Record record) {
    modified.remove(record);
    fireStoreEvent(Store.Update, Record.COMMIT, record, indexOf(record));
  }

  protected void afterEdit(Record record) {
    if (!modified.contains(record)) {
      modified.add(record);
    }
    fireStoreEvent(Store.Update, Record.EDIT, record, indexOf(record));
  }

  protected void afterReject(Record record) {
    modified.remove(record);
    fireStoreEvent(Store.Update, Record.REJECT, record, indexOf(record));
  }

  protected void loadData(C config, DataCallback callback) {

  }

  /**
   * template method to allow custom BaseLoader subclasses to provide their own
   * implementation of LoadConfig
   */
  protected C newLoadConfig() {
    return (C) new BaseLoadConfig();
  }

  protected void onLoadResult(C loadConfig, LoadResult loadResult) {
    LoadEvent evt = new LoadEvent(this, loadConfig, loadResult);
    if (loadResult.isSuccess()) {
      fireEvent(Load, evt);
    } else {
      fireEvent(LoadException, evt);
    }
  }

  /**
   * template method to allow custom BaseLoader subclasses to prepare the load
   * config prior to loading data
   */
  protected C prepareLoadConfig(C config) {
    config.setOffset(offset);
    config.setLimit(limit);
    // config.setSortField(sortField);
    // config.setSortDir(sortDir);
    return config;
  }

  private void fireStoreEvent(int type, int operation, Record record, int index) {
    StoreEvent se = new StoreEvent();
    se.store = this;
    se.operation = operation;
    se.record = record;
    se.index = index;
    fireEvent(type, se);
  }

  private void fireStoreEvent(int type, List<Record> records, Record record, int index) {
    StoreEvent se = new StoreEvent();
    se.store = this;
    se.records = records;
    se.record = record;
    se.index = index;
    fireEvent(type, se);
  }

  private void storeOptions(LoadConfig options) {

  }

  public void setReuseLoadConfig(boolean reuseLoadConfig) {
    this.reuseLoadConfig = reuseLoadConfig;
  }

  public boolean isReuseLoadConfig() {
    return reuseLoadConfig;
  }

}
