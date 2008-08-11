/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.data;

import java.io.Serializable;

/**
 * Default implementation of the <code>PagingLoadConfig</code>.
 */
public class BasePagingLoadConfig extends BaseListLoadConfig implements PagingLoadConfig,
    Serializable {

  protected int offset, limit;

  /**
   * Creates a new paging load config.
   */
  public BasePagingLoadConfig() {

  }

  /**
   * Creates a new paging load config.
   * 
   * @param offset the offset
   * @param limit the limit
   */
  public BasePagingLoadConfig(int offset, int limit) {
    setOffset(offset);
    setLimit(limit);
  }

  /**
   * Sets the config limit.
   * 
   * @param limit the limit
   */
  public void setLimit(int limit) {
    this.limit = limit;
  }

  /**
   * Sets the configs offset.
   * 
   * @param offset the offset
   */
  public void setOffset(int offset) {
    this.offset = offset;
  }

  public int getLimit() {
    return limit;
  }

  public int getOffset() {
    return offset;
  }

}
