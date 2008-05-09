/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.data;

import java.io.Serializable;

import com.extjs.gxt.ui.client.Style.SortDir;

/**
 * Default <code>LoadConfig</code> implementation.
 * 
 * @see LoadConfig
 */
public class BaseLoadConfig extends BaseModelData implements LoadConfig, Serializable {

  /**
   * The offset property name (defaults to 'offset').
   */
  public static final String OFFSET = "offset";

  /**
   * The limit property name (defaults to 'limit').
   */
  public static final String LIMIT = "limit";

  /**
   * The sort direction property name (defaults to 'sortDir').
   */
  public static final String SORT_DIR = "sortDir";
  
  /**
   * The sort field property name (defaults to 'sortField').
   */
  public static final String SORT_FIELD = "sortField";

  /**
   * The default limit (defautls to 0).
   */
  public static final int DEFAULT_LIMIT = 0;

  /**
   * The default offset (defaults to 0).
   */
  public static final int DEFAULT_OFFSET = 0;

  // Cause the RPC system to generate serializers for this type
  // this is only needed becase of how BaseModel uses a special RpcFieldMap
  protected SortDir rpcField_sortDir;

  /*
   * (non-Javadoc)
   * 
   * @see com.extjs.gxt.ui.client.data.LoadConfig#getLimit()
   */
  public int getLimit() {
    return (Integer) get(LIMIT, DEFAULT_LIMIT);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.extjs.gxt.ui.client.data.LoadConfig#getOffset()
   */
  public int getOffset() {
    return (Integer) get(OFFSET, DEFAULT_OFFSET);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.extjs.gxt.ui.client.data.LoadConfig#getSortDir()
   */
  public SortDir getSortDir() {
    return (SortDir) get(SORT_DIR);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.extjs.gxt.ui.client.data.LoadConfig#getSortField()
   */
  public String getSortField() {
    return (String) get(SORT_FIELD);
  }

  /**
   * Sets the config limit.
   * 
   * @param limit the limit
   */
  public void setLimit(int limit) {
    set(LIMIT, limit);
  }

  /**
   * Sets the configs offset.
   * 
   * @param offset the offset
   */
  public void setOffset(int offset) {
    set(OFFSET, offset);
  }

  /**
   * Sets the configs sort direction.
   * 
   * @param sortDir the sort direction
   */
  public void setSortDir(SortDir sortDir) {
    set(SORT_DIR, sortDir);
  }

  /**
   * Sets the configs sort field.
   * 
   * @param sortField the sort field
   */
  public void setSortField(String sortField) {
    set(SORT_FIELD, sortField);
  }

}
