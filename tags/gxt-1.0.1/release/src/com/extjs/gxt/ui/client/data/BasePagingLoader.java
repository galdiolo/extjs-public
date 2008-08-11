/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.data;

/**
 * Default implementation of the <code>PagingLoader</code> interface.
 * 
 * @param <C> the paging load config type
 * @param <D> the paging load result type
 */
public class BasePagingLoader<C extends PagingLoadConfig, D extends PagingLoadResult> extends
    BaseListLoader<C, D> implements PagingLoader<C> {

  protected int offset = 0;
  protected int limit = 50;
  protected int totalCount;

  /**
   * Creates a new paging loader instance.
   * 
   * @param proxy the data proxy
   */
  public BasePagingLoader(DataProxy<C, D> proxy) {
    super(proxy);
  }

  /**
   * Creates a new paging loader instance.
   * 
   * @param proxy the data proxy
   * @param reader the data reader
   */
  public BasePagingLoader(DataProxy<C, D> proxy, DataReader reader) {
    super(proxy, reader);
  }

  public int getLimit() {
    return limit;
  }

  public int getOffset() {
    return offset;
  }

  public int getTotalCount() {
    return totalCount;
  }

  public void load(int offset, int limit) {
    this.offset = offset;
    this.limit = limit;
    load();
  }

  public void setLimit(int limit) {
    this.limit = limit;
  }

  public void setOffset(int offset) {
    this.offset = offset;
  }

  /**
   * Use the specified LoadConfig for all load calls. The {@link #reuseConfig}
   * will be set to true.
   */
  public void useLoadConfig(C loadConfig) {
    super.useLoadConfig(loadConfig);
    offset = loadConfig.getOffset();
    limit = loadConfig.getLimit();
  }

  @Override
  protected C newLoadConfig() {
    return (C) new BasePagingLoadConfig();
  }

  @Override
  protected void onLoadSuccess(C loadConfig, D result) {
    LoadEvent evt = new LoadEvent(this, loadConfig, result);
    totalCount = result.getTotalLength();
    offset = result.getOffset();
    fireEvent(Load, evt);
  }

  @Override
  protected C prepareLoadConfig(C config) {
    config = super.prepareLoadConfig(config);
    config.setOffset(offset);
    config.setLimit(limit);
    return config;
  }

}
