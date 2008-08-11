/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.data;

import com.extjs.gxt.ui.client.Style.SortDir;

/**
 * Default implementation of the <code>ListLoader</code> interface.
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
 *
 * @param <C> the list load config type
 * @param <D> the list load result type
 */
public class BaseListLoader<C extends ListLoadConfig, D extends ListLoadResult> extends BaseLoader<C, D> implements
    ListLoader<C> {

  private boolean remoteSort;
  private String sortField;
  private SortDir sortDir = SortDir.NONE;

  /**
   * Creates a new loader instance with the given proxy. A reader is not
   * specified and will not be passed to the proxy at load time.
   * 
   * @param proxy the data proxy
   */
  public BaseListLoader(DataProxy proxy) {
    super(proxy);
  }

  /**
   * Creates a new loader instance.
   * 
   * @param proxy the data proxy
   * @param reader the data reader
   */
  public BaseListLoader(DataProxy proxy, DataReader reader) {
    super(proxy, reader);
  }

  public SortDir getSortDir() {
    return sortDir;
  }

  public String getSortField() {
    return sortField;
  }

  public boolean isRemoteSort() {
    return remoteSort;
  }

  public void setRemoteSort(boolean remoteSort) {
    this.remoteSort = remoteSort;
  }

  public void setSortDir(SortDir sortDir) {
    this.sortDir = sortDir == null ? SortDir.NONE : sortDir;
  }

  public void setSortField(String sortField) {
    this.sortField = sortField;
  }

  /**
   * Use the specified LoadConfig for all load calls. The {@link #reuseConfig}
   * will be set to true.
   */
  public void useLoadConfig(C loadConfig) {
    setReuseLoadConfig(true);
    setLastConfigInternal(loadConfig);
    sortDir = loadConfig.getSortInfo().getSortDir();
    sortField = loadConfig.getSortInfo().getSortField();
  }

  /**
   * Template method to allow custom BaseLoader subclasses to provide their own
   * implementation of LoadConfig
   */
  protected C newLoadConfig() {
    return (C)new BaseListLoadConfig();
  }

  /**
   * Template method to allow custom subclasses to prepare the load config prior
   * to loading data
   */
  protected C prepareLoadConfig(C config) {
    super.prepareLoadConfig(config);
    config.getSortInfo().setSortField(sortField);
    config.getSortInfo().setSortDir(sortDir);
    return config;
  }
  
  private native void setLastConfigInternal(C lastConfig) /*-{
    this.@com.extjs.gxt.ui.client.data.BaseLoader::lastConfig = lastConfig;
  }-*/;

}
