/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.data;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.util.Observable;

/**
 * Default implementation of the <code>Loader</code> interface.
 * 
 * <dl>
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>BeforeLoad</b> : LoadEvent(loader, config)<br>
 * <div>Fires before a load operation. Listeners can set the <code>doit</code>
 * field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>loader : this</li>
 * <li>config : the load config</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Load</b> : LoadEvent(loader, config, result)<br>
 * <div>Fires after the button is selected.</div>
 * <ul>
 * <li>loader : this</li>
 * <li>config : the load config</li>
 * <li>result : the load result</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>LoadException</b> : LoadEvent(loader, config, result)<br>
 * <div>Fires after the button is selected.</div>
 * <ul>
 * <li>loader : this</li>
 * <li>config : the load config</li>
 * <li>result : the load result</li>
 * </ul>
 * </dd>
 * </dl>
 */
public abstract class BaseLoader<C extends BaseLoadConfig> extends Observable implements
    Loader {

  /**
   * True to reuse the initial load config object (defaults to false).
   */
  public boolean reuseLoadConfig;

  protected int offset = 0;
  protected int limit = 50;
  protected C lastLoadConfig;

  private boolean remoteSort;
  private String sortField;
  private SortDir sortDir = SortDir.NONE;

  /*
   * (non-Javadoc)
   * 
   * @see com.extjs.gxt.ui.client.data.Loader#getOffset()
   */
  public int getOffset() {
    return offset;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.extjs.gxt.ui.client.data.Loader#getPageSize()
   */
  public int getPageSize() {
    return limit;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.extjs.gxt.ui.client.data.Loader#getRemoteSort()
   */
  public boolean getRemoteSort() {
    return remoteSort;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.extjs.gxt.ui.client.data.Loader#getSortDir()
   */
  public SortDir getSortDir() {
    return sortDir;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.extjs.gxt.ui.client.data.Loader#getSortField()
   */
  public String getSortField() {
    return sortField;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.extjs.gxt.ui.client.data.Loader#load()
   */
  public void load() {
    C config = (reuseLoadConfig && lastLoadConfig != null) ? lastLoadConfig
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

  /*
   * (non-Javadoc)
   * 
   * @see com.extjs.gxt.ui.client.data.Loader#load(int, int)
   */
  public void load(int offset, int limit) {
    this.offset = offset;
    this.limit = limit;
    load();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.extjs.gxt.ui.client.data.Loader#setRemoteSort(boolean)
   */
  public void setRemoteSort(boolean remoteSort) {
    this.remoteSort = remoteSort;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.extjs.gxt.ui.client.data.Loader#setSortDir(com.extjs.gxt.ui.client.Style.SortDir)
   */
  public void setSortDir(SortDir sortDir) {
    this.sortDir = sortDir == null ? SortDir.NONE : sortDir;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.extjs.gxt.ui.client.data.Loader#setSortField(java.lang.String)
   */
  public void setSortField(String sortField) {
    this.sortField = sortField;
  }

  /**
   * Use the specified LoadConfig for all load calls. The
   * {@link #reuseLoadConfig} will be set to true.
   */
  public void useLoadConfig(C loadConfig) {
    reuseLoadConfig = true;
    lastLoadConfig = loadConfig;
    offset = loadConfig.getOffset();
    limit = loadConfig.getLimit();
    sortDir = loadConfig.getSortDir();
    sortField = loadConfig.getSortField();
  }

  /**
   * Subclasses must implement and return the remote data to the callback, based
   * on the given load config. The viewer's setInput method will be called,
   * passing the data being returned from the callback.
   * 
   * @param config the load config
   * @param callback the callback
   */
  protected abstract void loadData(C config, DataCallback callback);

  /**
   * Template method to allow custom BaseLoader subclasses to provide their own
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
   * Template method to allow custom BaseLoader subclasses to prepare the load
   * config prior to loading data
   */
  protected C prepareLoadConfig(C config) {
    config.setOffset(offset);
    config.setLimit(limit);
    config.setSortField(sortField);
    config.setSortDir(sortDir);
    return config;
  }

}
